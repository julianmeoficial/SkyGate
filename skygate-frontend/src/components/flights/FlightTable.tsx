import { useStore } from '../../store';
import { useFlights } from '../../hooks/useFlights';
import { ArrowDownIcon, ArrowUpIcon, EyeIcon } from '@heroicons/react/24/outline';
import type { Flight } from '../../types/backend.types';
import { automataStateColors, automataStateBgColors } from '../../utils/colors';
import clsx from 'clsx';

interface FlightTableProps {
    flights: Flight[];
    onSelectFlight?: (flightId: number) => void;
    selectedFlightId?: number | null;
}

export default function FlightTable({ flights, onSelectFlight, selectedFlightId }: FlightTableProps) {
    const { simulateArrival, simulateDeparture } = useFlights();
    const getFlightState = useStore(state => state.getFlightState);

    return (
        <div className="overflow-x-auto">
            <table className="w-full">
                <thead>
                <tr className="border-b border-gray-800">
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Vuelo
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Tipo
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Ruta
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Estado AFD
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Acciones
                    </th>
                </tr>
                </thead>
                <tbody className="divide-y divide-gray-800/50">
                {flights.map((flight) => {
                    const currentState = getFlightState(flight.id) || flight.automataState;
                    const aircraftType = (flight as any).aircraft?.aircraftType || 'N/A';

                    return (
                        <tr
                            key={flight.id}
                            onClick={() => onSelectFlight?.(flight.id)}
                            className={clsx(
                                'hover:bg-gray-800/40 transition-all duration-200 cursor-pointer',
                                selectedFlightId === flight.id && 'bg-blue-500/10 border-l-4 border-blue-500'
                            )}
                        >
                            <td className="px-4 py-4">
                                <div className="space-y-1">
                                    <div className="font-bold text-white text-sm">
                                        {flight.flightNumber}
                                    </div>
                                    <div className="text-xs text-gray-400">
                                        {flight.airline}
                                    </div>
                                </div>
                            </td>
                            <td className="px-4 py-4">
                                    <span className="px-2.5 py-1 text-xs font-semibold bg-purple-500/10 text-purple-400 rounded border border-purple-500/30">
                                        {aircraftType}
                                    </span>
                            </td>
                            <td className="px-4 py-4">
                                <div className="flex items-center gap-2 text-sm text-gray-300">
                                    <span className="font-medium">{flight.origin}</span>
                                    <svg className="w-4 h-4 text-gray-500" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
                                    </svg>
                                    <span className="font-medium">{flight.destination}</span>
                                </div>
                            </td>
                            <td className="px-4 py-4">
                                <span
                                    className={clsx(
                                        'inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-bold rounded-full text-white',  // ← text-white forzado
                                        automataStateBgColors[currentState]
                                    )}
                                     >
                                    {currentState}
                                </span>
                            </td>
                            <td className="px-4 py-4">
                                <div className="flex items-center gap-2" onClick={(e) => e.stopPropagation()}>
                                    <button
                                        onClick={() => onSelectFlight?.(flight.id)}
                                        className="p-2 hover:bg-gray-700 rounded-lg transition-colors"
                                        title="Ver detalles"
                                    >
                                        <EyeIcon className="w-4 h-4 text-gray-400" />
                                    </button>
                                    {currentState === 'S4' && (
                                        <button
                                            onClick={() => simulateArrival(flight.id)}
                                            className="flex items-center gap-1 px-3 py-1.5 text-xs font-medium bg-green-500/10 text-green-400 hover:bg-green-500/20 rounded-lg transition-colors border border-green-500/30"
                                            title="Simular llegada"
                                        >
                                            <ArrowDownIcon className="w-3 h-3" />
                                            Llegada
                                        </button>
                                    )}
                                    {currentState === 'S5' && (
                                        <button
                                            onClick={() => simulateDeparture(flight.id)}
                                            className="flex items-center gap-1 px-3 py-1.5 text-xs font-medium bg-blue-500/10 text-blue-400 hover:bg-blue-500/20 rounded-lg transition-colors border border-blue-500/30"
                                            title="Simular partida"
                                        >
                                            <ArrowUpIcon className="w-3 h-3" />
                                            Partida
                                        </button>
                                    )}
                                </div>
                            </td>
                        </tr>
                    );
                })}
                </tbody>
            </table>
        </div>
    );
}
