import { useState } from 'react';
import { XMarkIcon, WrenchScrewdriverIcon } from '@heroicons/react/24/outline';
import type { Gate } from '../types/backend.types';

interface MaintenanceModalProps {
    gate: Gate;
    onClose: () => void;
    onConfirm: (gateId: number, reason: string) => void;
    isLoading?: boolean;
}

export default function MaintenanceModal({ gate, onClose, onConfirm, isLoading }: MaintenanceModalProps) {
    const [reason, setReason] = useState('');
    const isCurrentlyInMaintenance = gate.status === 'MAINTENANCE';

    const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        if (!isCurrentlyInMaintenance && reason.trim().length < 5) {
            return;
        }
        onConfirm(gate.id, reason);
    };

    return (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
            <div className="bg-gray-900 rounded-xl border border-gray-700 p-6 max-w-md w-full mx-4">
                <div className="flex items-center justify-between mb-6">
                    <div className="flex items-center gap-3">
                        <div className="w-10 h-10 rounded-lg bg-orange-500/10 flex items-center justify-center">
                            <WrenchScrewdriverIcon className="w-6 h-6 text-orange-500" />
                        </div>
                        <div>
                            <h3 className="text-lg font-semibold text-white">
                                {isCurrentlyInMaintenance ? 'Liberar Gate' : 'Marcar en Mantenimiento'}
                            </h3>
                            <p className="text-sm text-gray-400">Gate {gate.gateNumber}</p>
                        </div>
                    </div>
                    <button
                        onClick={onClose}
                        className="text-gray-400 hover:text-white transition-colors"
                        disabled={isLoading}
                    >
                        <XMarkIcon className="w-6 h-6" />
                    </button>
                </div>

                <form onSubmit={handleSubmit} className="space-y-4">
                    {!isCurrentlyInMaintenance && (
                        <div>
                            <label htmlFor="reason" className="block text-sm font-medium text-gray-300 mb-2">
                                Motivo del Mantenimiento
                            </label>
                            <textarea
                                id="reason"
                                value={reason}
                                onChange={(e) => setReason(e.target.value)}
                                rows={4}
                                placeholder="Describe el motivo del mantenimiento..."
                                className="w-full px-4 py-2 bg-gray-800 border border-gray-700 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-orange-500 focus:border-transparent resize-none"
                                required
                                minLength={5}
                                disabled={isLoading}
                            />
                            <p className="text-xs text-gray-500 mt-1">Mínimo 5 caracteres</p>
                        </div>
                    )}

                    {isCurrentlyInMaintenance && (
                        <div className="bg-blue-500/10 border border-blue-500/20 rounded-lg p-4">
                            <p className="text-sm text-blue-400">
                                Este gate está actualmente en mantenimiento. Al confirmar, el gate volverá al estado LIBRE
                                y estará disponible para asignaciones.
                            </p>
                        </div>
                    )}

                    <div className="bg-yellow-500/10 border border-yellow-500/20 rounded-lg p-4">
                        <p className="text-xs text-yellow-400">
                            {isCurrentlyInMaintenance
                                ? 'El gate pasará de MANTENIMIENTO a LIBRE'
                                : 'El gate quedará marcado como NO DISPONIBLE hasta que se complete el mantenimiento'}
                        </p>
                    </div>

                    <div className="flex gap-3 pt-2">
                        <button
                            type="button"
                            onClick={onClose}
                            className="flex-1 px-4 py-2 bg-gray-800 text-white rounded-lg hover:bg-gray-700 transition-colors"
                            disabled={isLoading}
                        >
                            Cancelar
                        </button>
                        <button
                            type="submit"
                            className={`flex-1 px-4 py-2 rounded-lg font-medium transition-colors ${
                                isCurrentlyInMaintenance
                                    ? 'bg-blue-600 hover:bg-blue-700 text-white'
                                    : 'bg-orange-600 hover:bg-orange-700 text-white'
                            } disabled:opacity-50 disabled:cursor-not-allowed`}
                            disabled={isLoading || (!isCurrentlyInMaintenance && reason.trim().length < 5)}
                        >
                            {isLoading ? 'Procesando...' : isCurrentlyInMaintenance ? 'Liberar Gate' : 'Marcar Mantenimiento'}
                        </button>
                    </div>
                </form>
            </div>
        </div>
    );
}
