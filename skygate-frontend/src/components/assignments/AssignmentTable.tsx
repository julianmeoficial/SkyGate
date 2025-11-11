import { useEffect, useRef } from 'react';
import { formatDistanceToNow } from 'date-fns';
import { es } from 'date-fns/locale';
import type { Assignment } from '../../types/backend.types';
import clsx from 'clsx';

interface AssignmentTableProps {
    assignments: Assignment[];
    showActions?: boolean;
}

export default function AssignmentTable({ assignments, showActions = false }: AssignmentTableProps) {
    const tableRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && tableRef.current) {
            const rows = tableRef.current.querySelectorAll('tbody tr');
            window.gsap.fromTo(
                rows,
                { opacity: 0, x: -20 },
                { opacity: 1, x: 0, duration: 0.3, stagger: 0.05, ease: 'power2.out' }
            );
        }
    }, [assignments.length]);

    return (
        <div ref={tableRef} className="overflow-x-auto">
            <table className="w-full">
                <thead>
                <tr className="border-b border-gray-800">
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Vuelo
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Gate
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Tipo
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Asignado
                    </th>
                    <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                        Estado
                    </th>
                    {showActions && (
                        <th className="px-4 py-3 text-left text-xs font-bold text-gray-400 uppercase tracking-wider">
                            LED
                        </th>
                    )}
                </tr>
                </thead>
                <tbody className="divide-y divide-gray-800/50">
                {assignments.map((assignment) => (
                    <tr
                        key={assignment.id}
                        className="hover:bg-gray-800/20 transition-colors duration-200 group"
                    >
                        <td className="px-4 py-4">
                            <div className="space-y-1">
                                <div className="font-bold text-white text-sm group-hover:text-blue-400 transition-colors duration-200">
                                    {assignment.flight.flightNumber}
                                </div>
                                <div className="flex items-center gap-2 text-xs text-gray-400">
                                    <span className="font-medium">{assignment.flight.origin}</span>
                                    <svg className="w-3 h-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 7l5 5m0 0l-5 5m5-5H6" />
                                    </svg>
                                    <span className="font-medium">{assignment.flight.destination}</span>
                                </div>
                            </div>
                        </td>
                        <td className="px-4 py-4">
                            <div className="inline-flex items-center gap-2 px-3 py-1.5 bg-gray-800/60 rounded-lg border border-gray-700/50">
                                <svg className="w-4 h-4 text-blue-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                                <span className="text-white font-bold text-sm">{assignment.gate.gateNumber}</span>
                                <span className="text-xs text-gray-500">T{assignment.gate.terminal}</span>
                            </div>
                        </td>
                        <td className="px-4 py-4">
                                <span className="px-2.5 py-1 text-xs font-semibold bg-purple-500/10 text-purple-400 rounded border border-purple-500/30">
                                    {assignment.flight.aircraftType}
                                </span>
                        </td>
                        <td className="px-4 py-4">
                            <div className="flex items-center gap-2 text-sm text-gray-400">
                                <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                </svg>
                                {formatDistanceToNow(new Date(assignment.assignedAt), {
                                    addSuffix: true,
                                    locale: es,
                                })}
                            </div>
                        </td>
                        <td className="px-4 py-4">
                                <span
                                    className={clsx(
                                        'inline-flex items-center gap-1.5 px-2.5 py-1 text-xs font-bold rounded-full border',
                                        assignment.isActive
                                            ? 'bg-green-500/10 text-green-400 border-green-500/40'
                                            : 'bg-gray-800/60 text-gray-400 border-gray-700'
                                    )}
                                >
                                    {assignment.isActive && (
                                        <span className="relative flex h-2 w-2">
                                            <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-green-400 opacity-75" />
                                            <span className="relative inline-flex rounded-full h-2 w-2 bg-green-500" />
                                        </span>
                                    )}
                                    {assignment.isActive ? 'Activo' : 'Finalizado'}
                                </span>
                        </td>
                        {showActions && (
                            <td className="px-4 py-4">
                                <div className="flex items-center gap-2">
                                    <div
                                        className={clsx(
                                            'w-3 h-3 rounded-full transition-all duration-300',
                                            assignment.isActive
                                                ? 'bg-green-500 shadow-lg shadow-green-500/50 animate-pulse'
                                                : 'bg-gray-700'
                                        )}
                                    />
                                    <span className="text-xs text-gray-500">
                                            {assignment.isActive ? 'ON' : 'OFF'}
                                        </span>
                                </div>
                            </td>
                        )}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
}
