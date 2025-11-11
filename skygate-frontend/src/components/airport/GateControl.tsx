import { useState } from 'react';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { gateService } from '../../api/services/gate.service';
import { Icon } from '../../components/common/Icon';
import { CheckCircleIcon, XCircleIcon } from '@heroicons/react/24/solid';
import type { Gate, GateStatus } from '../../types';

interface GateControlProps {
    gate: Gate;
    compact?: boolean;
}

export default function GateControl({ gate, compact = false }: GateControlProps) {
    const [localStatus, setLocalStatus] = useState<boolean>(gate.status === 'FREE');
    const queryClient = useQueryClient();

    const updateStatusMutation = useMutation({
        mutationFn: (newStatus: GateStatus) => gateService.updateStatus(gate.id, newStatus),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['gates'] });
        },
        onError: (error: any) => {
            console.error('Error updating gate status:', error);
            setLocalStatus(gate.status === 'FREE');
        },
    });

    const handleStatusToggle = () => {
        const newStatus: GateStatus = localStatus ? 'MAINTENANCE' : 'FREE';
        setLocalStatus(!localStatus);
        updateStatusMutation.mutate(newStatus);
    };

    if (compact) {
        return (
            <div className="flex items-center gap-2">
                <label className="relative inline-flex items-center cursor-pointer">
                    <input
                        type="checkbox"
                        checked={localStatus}
                        onChange={handleStatusToggle}
                        className="sr-only peer"
                        disabled={updateStatusMutation.isPending}
                    />
                    <div className="relative w-14 h-7 bg-slate-700 peer-checked:bg-green-600 rounded-full after:content-[''] after:absolute after:bg-white after:rounded-full after:h-5 after:w-5 after:top-1 after:left-1 after:transition-all peer-checked:after:translate-x-7 shadow-lg"></div>
                </label>
                <span className="text-sm text-slate-300">{localStatus ? 'Disponible' : 'Mantenimiento'}</span>
            </div>
        );
    }

    return (
        <div className="bg-slate-800 rounded-lg p-6 border border-slate-700 space-y-6">
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-3">
                    <Icon name="tower" size="lg" />
                    <div>
                        <h3 className="text-lg font-semibold text-slate-100">Control de Gate {gate.gateNumber}</h3>
                        <p className="text-sm text-slate-400">Terminal {gate.terminal}</p>
                    </div>
                </div>
                <div
                    className={`px-3 py-1 rounded-full text-xs font-semibold ${
                        gate.status === 'FREE'
                            ? 'bg-green-900 text-green-300'
                            : gate.status === 'MAINTENANCE'
                                ? 'bg-orange-900 text-orange-300'
                                : 'bg-red-900 text-red-300'
                    }`}
                >
                    {gate.status}
                </div>
            </div>

            <div className="space-y-4">
                <div className="flex items-center justify-between p-4 bg-slate-900 rounded-lg border border-slate-700">
                    <div className="flex items-center gap-3">
                        <div
                            className={`w-3 h-3 rounded-full ${
                                localStatus ? 'bg-green-400' : 'bg-orange-400'
                            } animate-pulse`}
                        ></div>
                        <div>
                            <p className="text-slate-200 font-medium">Estado Operativo</p>
                            <p className="text-slate-400 text-sm">
                                {localStatus ? 'Gate disponible para asignaciones' : 'Gate en mantenimiento'}
                            </p>
                        </div>
                    </div>
                    <label className="relative inline-flex items-center cursor-pointer">
                        <input
                            type="checkbox"
                            checked={localStatus}
                            onChange={handleStatusToggle}
                            className="sr-only peer"
                            disabled={updateStatusMutation.isPending}
                        />
                        <div className="relative w-20 h-10 bg-slate-700 peer-checked:bg-emerald-600 rounded-full peer-focus:ring-2 peer-focus:ring-cyan-500 after:content-[''] after:absolute after:bg-white after:rounded-full after:h-8 after:w-8 after:top-1 after:left-1 after:transition-all duration-300 peer-checked:after:translate-x-10 shadow-lg">
                            {localStatus ? (
                                <CheckCircleIcon className="absolute right-2 top-2 w-6 h-6 text-white" />
                            ) : (
                                <XCircleIcon className="absolute left-2 top-2 w-6 h-6 text-white" />
                            )}
                        </div>
                    </label>
                </div>
            </div>

            <div className="pt-4 border-t border-slate-700 grid grid-cols-2 gap-4 text-sm">
                <div>
                    <p className="text-slate-400">Tipo</p>
                    <p className="text-slate-100 font-medium">{gate.gateType}</p>
                </div>
                <div>
                    <p className="text-slate-400">Terminal</p>
                    <p className="text-slate-100 font-medium">{gate.terminal}</p>
                </div>
            </div>
        </div>
    );
}
