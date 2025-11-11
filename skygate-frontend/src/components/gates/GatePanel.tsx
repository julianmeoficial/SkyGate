import { useState, useEffect, useRef } from 'react';
import { WrenchScrewdriverIcon, ClockIcon } from '@heroicons/react/24/outline';
import VirtualLED from '../airport/VirtualLED';
import { MaintenanceModal, GateHistoryModal } from '../../modal';
import type { Gate } from '../../types/backend.types';
import { gateService } from '../../api/services/gate.service';
import { useQueryClient } from '@tanstack/react-query';
import { useStore } from '../../store';
import clsx from 'clsx';

interface GatePanelProps {
    gates: Gate[];
    groupByTerminal?: boolean;
}

export default function GatePanel({ gates, groupByTerminal = false }: GatePanelProps) {
    if (groupByTerminal) {
        const gatesByTerminal = gates.reduce((acc, gate) => {
            if (!acc[gate.terminal]) {
                acc[gate.terminal] = [];
            }
            acc[gate.terminal].push(gate);
            return acc;
        }, {} as Record<string, Gate[]>);

        return (
            <div className="space-y-8">
                {Object.entries(gatesByTerminal).map(([terminal, terminalGates]) => (
                    <div key={terminal} className="space-y-4">
                        <div className="flex items-center gap-3 pb-3 border-b border-gray-800">
                            <div className="w-10 h-10 rounded-lg bg-gradient-to-br from-blue-500/20 to-purple-500/20 flex items-center justify-center">
                                <span className="text-xl font-bold text-blue-400">T{terminal}</span>
                            </div>
                            <div>
                                <h3 className="text-lg font-semibold text-white">Terminal {terminal}</h3>
                                <p className="text-sm text-gray-400">{terminalGates.length} gates</p>
                            </div>
                        </div>
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                            {terminalGates.map((gate) => (
                                <GateCard key={gate.id} gate={gate} />
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
            {gates.map((gate) => (
                <GateCard key={gate.id} gate={gate} />
            ))}
        </div>
    );
}

function GateCard({ gate }: { gate: Gate }) {
    const [showMaintenanceModal, setShowMaintenanceModal] = useState(false);
    const [showHistoryModal, setShowHistoryModal] = useState(false);
    const [isProcessing, setIsProcessing] = useState(false);
    const queryClient = useQueryClient();
    const addToast = useStore(state => state.addToast);
    const cardRef = useRef<HTMLDivElement>(null);

    const statusColors = {
        FREE: 'border-gate-free',
        ASSIGNED: 'border-gate-assigned',
        OCCUPIED: 'border-gate-occupied',
        MAINTENANCE: 'border-gate-maintenance',
        RESERVED: 'border-blue-500',
    };

    const statusText = {
        FREE: 'Disponible',
        ASSIGNED: 'Asignado',
        OCCUPIED: 'Ocupado',
        MAINTENANCE: 'Mantenimiento',
        RESERVED: 'Reservado',
    };

    const statusBgColors = {
        FREE: 'bg-emerald-500/10',
        ASSIGNED: 'bg-amber-500/10',
        OCCUPIED: 'bg-rose-500/10',
        MAINTENANCE: 'bg-orange-500/10',
        RESERVED: 'bg-blue-500/10',
    };

    const statusTextColors = {
        FREE: 'text-emerald-400',
        ASSIGNED: 'text-amber-400',
        OCCUPIED: 'text-rose-400',
        MAINTENANCE: 'text-orange-400',
        RESERVED: 'text-blue-400',
    };

    useEffect(() => {
        if (cardRef.current && typeof window !== 'undefined' && window.gsap) {
            window.gsap.fromTo(
                cardRef.current,
                { opacity: 0, y: 20, scale: 0.95 },
                {
                    opacity: 1,
                    y: 0,
                    scale: 1,
                    duration: 0.4,
                    ease: 'power2.out',
                    delay: Math.random() * 0.2
                }
            );
        }
    }, []);

    const handleMaintenanceToggle = async (gateId: number, reason: string) => {
        try {
            setIsProcessing(true);
            if (gate.status === 'MAINTENANCE') {
                await gateService.clearMaintenance(gateId);
                addToast({
                    type: 'success',
                    message: `Gate ${gate.gateNumber} liberado de mantenimiento`,
                    timestamp: Date.now(),
                    read: false,
                    autoClose: true,
                    duration: 3000,
                });
            } else {
                await gateService.setMaintenance(gateId);
                addToast({
                    type: 'info',
                    message: `Gate ${gate.gateNumber} marcado en mantenimiento: ${reason}`,
                    timestamp: Date.now(),
                    read: false,
                    autoClose: true,
                    duration: 4000,
                });
            }
            queryClient.invalidateQueries({ queryKey: ['gates'] });
            setShowMaintenanceModal(false);
        } catch (error: any) {
            console.error('Error al cambiar estado de mantenimiento:', error);
            addToast({
                type: 'error',
                message: error.response?.data?.message || 'Error al actualizar el gate',
                timestamp: Date.now(),
                read: false,
                autoClose: true,
                duration: 5000,
            });
        } finally {
            setIsProcessing(false);
        }
    };

    const canToggleMaintenance = gate.status === 'FREE' || gate.status === 'MAINTENANCE';

    return (
        <>
            <div
                ref={cardRef}
                className={clsx(
                    'relative bg-gradient-to-br from-gray-900/95 via-gray-900/90 to-gray-800/95 rounded-xl border-2 transition-all duration-300',
                    statusColors[gate.status],
                    'hover:shadow-lg hover:shadow-gray-900/50 hover:-translate-y-1 hover:border-opacity-80',
                    'backdrop-blur-sm overflow-hidden group'
                )}
            >
                <div className="absolute inset-0 bg-gradient-to-br from-white/[0.02] to-transparent pointer-events-none" />

                <div className="absolute top-0 right-0 w-32 h-32 bg-gradient-to-br from-blue-500/5 to-purple-500/5 rounded-bl-full blur-2xl group-hover:scale-150 transition-transform duration-700" />

                <div className="relative p-5 space-y-4">
                    <div className="flex items-start justify-between">
                        <div className="flex items-center gap-3">
                            <div className="w-12 h-12 rounded-lg bg-gradient-to-br from-gray-800 to-gray-900 flex items-center justify-center shadow-inner border border-gray-700/50">
                                <span className="text-2xl font-bold text-white">{gate.gateNumber}</span>
                            </div>
                            <div className="flex flex-col">
                                <span className="text-xs font-medium text-gray-500 uppercase tracking-wider">Gate</span>
                                <span className="text-sm font-medium text-gray-400">T{gate.terminal}</span>
                            </div>
                        </div>
                        <VirtualLED status={gate.status} />
                    </div>

                    <div className="space-y-2 py-2">
                        <div className="flex items-center justify-between text-sm">
                            <span className="text-gray-500 font-medium">Tipo:</span>
                            <span className="text-gray-300 font-semibold">{gate.gateType}</span>
                        </div>
                        <div className="flex items-center justify-between text-sm">
                            <span className="text-gray-500 font-medium">Terminal:</span>
                            <span className="text-gray-300 font-semibold">{gate.terminal}</span>
                        </div>
                    </div>

                    <div className={clsx(
                        'px-3 py-2 rounded-lg text-center font-semibold text-sm transition-all duration-300',
                        statusBgColors[gate.status],
                        statusTextColors[gate.status],
                        'border border-current/20'
                    )}>
                        {statusText[gate.status]}
                    </div>

                    <div className="flex gap-2 pt-2">
                        <button
                            onClick={() => setShowMaintenanceModal(true)}
                            disabled={!canToggleMaintenance}
                            className={clsx(
                                'flex-1 flex items-center justify-center gap-1.5 px-3 py-2.5 rounded-lg text-xs font-semibold transition-all duration-200',
                                canToggleMaintenance
                                    ? gate.status === 'MAINTENANCE'
                                        ? 'bg-blue-500/15 text-blue-400 hover:bg-blue-500/25 border border-blue-500/30 hover:border-blue-500/50 hover:shadow-md hover:shadow-blue-500/20'
                                        : 'bg-orange-500/15 text-orange-400 hover:bg-orange-500/25 border border-orange-500/30 hover:border-orange-500/50 hover:shadow-md hover:shadow-orange-500/20'
                                    : 'bg-gray-800/80 text-gray-600 cursor-not-allowed border border-gray-700/50'
                            )}
                            title={
                                canToggleMaintenance
                                    ? gate.status === 'MAINTENANCE'
                                        ? 'Liberar de mantenimiento'
                                        : 'Marcar en mantenimiento'
                                    : 'No disponible'
                            }
                        >
                            <WrenchScrewdriverIcon className="w-4 h-4" />
                            <span>{gate.status === 'MAINTENANCE' ? 'Liberar' : 'Mant.'}</span>
                        </button>
                        <button
                            onClick={() => setShowHistoryModal(true)}
                            className="flex-1 flex items-center justify-center gap-1.5 px-3 py-2.5 rounded-lg text-xs font-semibold bg-gray-800/80 text-gray-400 hover:bg-gray-700/90 hover:text-white transition-all duration-200 border border-gray-700/50 hover:border-gray-600 hover:shadow-md"
                            title="Ver historial"
                        >
                            <ClockIcon className="w-4 h-4" />
                            <span>Historial</span>
                        </button>
                    </div>
                </div>
            </div>

            {showMaintenanceModal && (
                <MaintenanceModal
                    gate={gate}
                    onClose={() => setShowMaintenanceModal(false)}
                    onConfirm={handleMaintenanceToggle}
                    isLoading={isProcessing}
                />
            )}
            {showHistoryModal && (
                <GateHistoryModal
                    gate={gate}
                    onClose={() => setShowHistoryModal(false)}
                />
            )}
        </>
    );
}
