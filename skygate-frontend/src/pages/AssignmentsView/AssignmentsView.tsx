import { useEffect, useRef } from 'react';
import { useAssignments } from '../../hooks/useAssignments';
import AssignmentTable from '../../components/assignments/AssignmentTable';
import { ClipboardDocumentCheckIcon } from '@heroicons/react/24/outline';

export default function AssignmentsView() {
    const { assignments, activeAssignments, isLoading } = useAssignments();
    const headerRef = useRef<HTMLDivElement>(null);
    const activeRef = useRef<HTMLDivElement>(null);
    const historyRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap) {
            if (headerRef.current) {
                window.gsap.fromTo(
                    headerRef.current,
                    { opacity: 0, y: -20 },
                    { opacity: 1, y: 0, duration: 0.5, ease: 'power2.out' }
                );
            }
            if (activeRef.current) {
                window.gsap.fromTo(
                    activeRef.current,
                    { opacity: 0, y: 20 },
                    { opacity: 1, y: 0, duration: 0.4, ease: 'power2.out', delay: 0.2 }
                );
            }
            if (historyRef.current) {
                window.gsap.fromTo(
                    historyRef.current,
                    { opacity: 0, y: 20 },
                    { opacity: 1, y: 0, duration: 0.4, ease: 'power2.out', delay: 0.3 }
                );
            }
        }
    }, []);

    return (
        <div className="p-8 space-y-8">
            <div ref={headerRef} className="flex items-start justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-white mb-2">Asignaciones de Gates</h1>
                    <p className="text-gray-400">Historial y asignaciones activas de vuelos a gates</p>
                </div>
                <div className="flex items-center gap-3 px-4 py-2 bg-blue-500/10 rounded-lg border border-blue-500/30">
                    <ClipboardDocumentCheckIcon className="w-5 h-5 text-blue-400" />
                    <div className="text-right">
                        <div className="text-xs text-gray-400 font-medium">Asignaciones Activas</div>
                        <div className="text-xl font-bold text-white">{activeAssignments.length}</div>
                    </div>
                </div>
            </div>

            <div ref={activeRef} className="space-y-4">
                <div className="flex items-center gap-3">
                    <div className="w-1 h-6 bg-gradient-to-b from-blue-500 to-cyan-500 rounded-full" />
                    <h2 className="text-xl font-semibold text-white">Asignaciones Activas</h2>
                    {activeAssignments.length > 0 && (
                        <span className="px-2.5 py-1 text-xs font-bold bg-blue-500/20 text-blue-400 rounded-full border border-blue-500/40">
                            {activeAssignments.length} activa{activeAssignments.length !== 1 ? 's' : ''}
                        </span>
                    )}
                </div>

                {isLoading ? (
                    <div className="flex items-center justify-center py-12">
                        <div className="relative">
                            <div className="w-12 h-12 border-4 border-gray-700 border-t-blue-500 rounded-full animate-spin" />
                            <div className="absolute inset-0 w-12 h-12 border-4 border-transparent border-t-cyan-500 rounded-full animate-spin animation-delay-150" />
                        </div>
                        <p className="ml-4 text-gray-400 font-medium">Cargando asignaciones...</p>
                    </div>
                ) : activeAssignments.length === 0 ? (
                    <div className="text-center py-16 bg-gray-800/20 rounded-lg border border-gray-800/40">
                        <div className="w-16 h-16 mx-auto mb-4 rounded-full bg-gray-800/40 flex items-center justify-center">
                            <ClipboardDocumentCheckIcon className="w-8 h-8 text-gray-600" />
                        </div>
                        <p className="text-gray-400 font-medium">No hay asignaciones activas</p>
                        <p className="text-sm text-gray-500 mt-1">Las asignaciones aparecerán cuando se asignen gates a vuelos</p>
                    </div>
                ) : (
                    <AssignmentTable assignments={activeAssignments} showActions />
                )}
            </div>

            <div ref={historyRef} className="space-y-4">
                <div className="flex items-center gap-3">
                    <div className="w-1 h-6 bg-gradient-to-b from-purple-500 to-pink-500 rounded-full" />
                    <h2 className="text-xl font-semibold text-white">Historial de Asignaciones</h2>
                    <span className="px-2.5 py-1 text-xs font-bold bg-gray-800 text-gray-400 rounded-full border border-gray-700">
                        {assignments.length} total
                    </span>
                </div>

                {isLoading ? (
                    <div className="flex items-center justify-center py-12">
                        <div className="relative">
                            <div className="w-12 h-12 border-4 border-gray-700 border-t-purple-500 rounded-full animate-spin" />
                            <div className="absolute inset-0 w-12 h-12 border-4 border-transparent border-t-pink-500 rounded-full animate-spin animation-delay-150" />
                        </div>
                        <p className="ml-4 text-gray-400 font-medium">Cargando historial...</p>
                    </div>
                ) : (
                    <AssignmentTable assignments={assignments} />
                )}
            </div>
        </div>
    );
}
