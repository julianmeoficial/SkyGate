import { useMemo } from 'react';
import { XMarkIcon, ClockIcon } from '@heroicons/react/24/outline';
import { useAssignments } from '../hooks/useAssignments';
import type { Gate } from '../types/backend.types';
import { formatDistanceToNow } from 'date-fns';
import { es } from 'date-fns/locale';

interface GateHistoryModalProps {
    gate: Gate;
    onClose: () => void;
}

export default function GateHistoryModal({ gate, onClose }: GateHistoryModalProps) {
    const { assignments, isLoading } = useAssignments();

    const gateHistory = useMemo(() => {
        return assignments.filter(assignment => assignment.gate.id === gate.id);
    }, [assignments, gate.id]);

    return (
        <div className="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 p-4">
            <div className="bg-gray-900 rounded-2xl shadow-2xl w-full max-w-4xl max-h-[90vh] overflow-hidden border border-gray-800">
                <div className="bg-gradient-to-r from-blue-600 to-cyan-600 p-6 flex items-center justify-between">
                    <div className="flex items-center gap-3">
                        <ClockIcon className="w-8 h-8 text-white" />
                        <div>
                            <h2 className="text-2xl font-bold text-white">Historial de Asignaciones</h2>
                            <p className="text-blue-100 text-sm mt-1">
                                Gate {gate.gateNumber} - Terminal {gate.terminal}
                            </p>
                        </div>
                    </div>
                    <button
                        onClick={onClose}
                        className="p-2 hover:bg-white/10 rounded-lg transition-colors"
                    >
                        <XMarkIcon className="w-6 h-6 text-white" />
                    </button>
                </div>

                <div className="p-6 overflow-y-auto max-h-[calc(90vh-200px)]">
                    {isLoading ? (
                        <div className="flex flex-col items-center justify-center py-12">
                            <div className="w-12 h-12 border-4 border-gray-700 border-t-blue-500 rounded-full animate-spin" />
                            <p className="text-gray-400 mt-4">Cargando historial...</p>
                        </div>
                    ) : gateHistory.length === 0 ? (
                        <div className="bg-gray-800/40 border border-gray-700 rounded-lg p-8 text-center">
                            <ClockIcon className="w-16 h-16 text-gray-600 mx-auto mb-4" />
                            <p className="text-gray-400 text-lg">No hay historial de asignaciones</p>
                            <p className="text-gray-500 text-sm mt-2">Este gate aún no ha tenido vuelos asignados</p>
                        </div>
                    ) : (
                        <div className="space-y-4">
                            {gateHistory.map((assignment) => (
                                <div
                                    key={assignment.id}
                                    className="bg-gradient-to-br from-gray-800/60 to-gray-900/60 backdrop-blur-sm rounded-xl p-6 border border-gray-700/50 hover:border-gray-600/50 transition-all duration-300"
                                >
                                    <div className="flex items-start justify-between mb-4">
                                        <div>
                                            <h3 className="text-xl font-bold text-white">
                                                {assignment.flight.flightNumber}
                                            </h3>
                                            <p className="text-gray-400 text-sm mt-1">
                                                {assignment.flight.airline} - {assignment.flight.aircraftType || 'N/A'}
                                            </p>
                                        </div>
                                        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                                            assignment.isActive
                                                ? 'bg-green-500/10 text-green-400 border border-green-500/30'
                                                : 'bg-gray-500/10 text-gray-400 border border-gray-500/30'
                                        }`}>
                                            {assignment.isActive ? 'Activo' : 'Finalizado'}
                                        </span>
                                    </div>

                                    <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mb-4">
                                        <div>
                                            <p className="text-xs text-gray-500 uppercase tracking-wider mb-1">Asignado</p>
                                            <p className="text-sm text-gray-300 font-medium">
                                                {formatDistanceToNow(new Date(assignment.assignedAt), {
                                                    addSuffix: true,
                                                    locale: es,
                                                })}
                                            </p>
                                        </div>
                                        <div>
                                            <p className="text-xs text-gray-500 uppercase tracking-wider mb-1">Llegada</p>
                                            <p className="text-sm text-gray-300 font-medium">
                                                {assignment.actualArrival
                                                    ? formatDistanceToNow(new Date(assignment.actualArrival), {
                                                        addSuffix: true,
                                                        locale: es,
                                                    })
                                                    : 'Pendiente'}
                                            </p>
                                        </div>
                                        <div>
                                            <p className="text-xs text-gray-500 uppercase tracking-wider mb-1">Partida</p>
                                            <p className="text-sm text-gray-300 font-medium">
                                                {assignment.departureTime
                                                    ? formatDistanceToNow(new Date(assignment.departureTime), {
                                                        addSuffix: true,
                                                        locale: es,
                                                    })
                                                    : 'Pendiente'}
                                            </p>
                                        </div>
                                        <div>
                                            <p className="text-xs text-gray-500 uppercase tracking-wider mb-1">LED</p>
                                            <p className="text-sm text-gray-300 font-medium">
                                                {assignment.ledActivated ? 'Activados' : 'Desactivados'}
                                            </p>
                                        </div>
                                    </div>

                                    {assignment.notes && (
                                        <div className="bg-gray-800/40 rounded-lg p-3 border border-gray-700/50">
                                            <p className="text-xs text-gray-500 uppercase tracking-wider mb-1">Notas</p>
                                            <p className="text-sm text-gray-300">{assignment.notes}</p>
                                        </div>
                                    )}
                                </div>
                            ))}
                        </div>
                    )}
                </div>

                <div className="bg-gray-800/40 p-4 border-t border-gray-800 flex justify-end">
                    <button
                        onClick={onClose}
                        className="px-6 py-2 bg-gray-700 hover:bg-gray-600 text-white rounded-lg transition-colors font-medium"
                    >
                        Cerrar
                    </button>
                </div>
            </div>
        </div>
    );
}
