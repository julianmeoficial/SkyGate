import { useEffect, useRef } from 'react';
import { CheckCircleIcon, ExclamationTriangleIcon } from '@heroicons/react/24/solid';
import type { DashboardStats } from '../../types/backend.types';

interface SystemHealthCardProps {
    stats?: DashboardStats;
}

export default function SystemHealthCard({ stats }: SystemHealthCardProps) {
    const isHealthy = stats && stats.gates.free > 0;
    const cardsRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && cardsRef.current) {
            const cards = cardsRef.current.querySelectorAll('.stat-card');
            window.gsap.fromTo(
                cards,
                { opacity: 0, scale: 0.9, y: 20 },
                { opacity: 1, scale: 1, y: 0, duration: 0.4, stagger: 0.1, ease: 'back.out(1.5)' }
            );
        }
    }, [stats]);

    return (
        <div className="bg-gradient-to-br from-gray-800/60 to-gray-900/60 backdrop-blur-sm rounded-xl p-6 border border-gray-800/50">
            <div className="flex items-center gap-3 mb-6">
                {isHealthy ? (
                    <CheckCircleIcon className="w-8 h-8 text-green-500 animate-pulse" />
                ) : (
                    <ExclamationTriangleIcon className="w-8 h-8 text-yellow-500 animate-pulse" />
                )}
                <div>
                    <h2 className="text-xl font-bold text-white">Estado del Sistema</h2>
                    <p className={`text-sm font-medium ${isHealthy ? 'text-green-400' : 'text-yellow-400'}`}>
                        {isHealthy ? 'Sistema operando normalmente' : 'Atención requerida'}
                    </p>
                </div>
            </div>

            <div ref={cardsRef} className="grid grid-cols-1 md:grid-cols-3 gap-4">
                <div className="stat-card bg-gradient-to-br from-green-500/10 to-emerald-500/10 p-5 rounded-lg border border-green-500/30 hover:border-green-500/50 transition-all duration-300">
                    <div className="text-sm font-medium text-green-400 mb-2">Gates Disponibles</div>
                    <div className="flex items-baseline gap-2">
                        <div className="text-4xl font-bold text-white">{stats?.gates.free || 0}</div>
                        <div className="text-sm text-green-400/80">libres</div>
                    </div>
                </div>

                <div className="stat-card bg-gradient-to-br from-blue-500/10 to-cyan-500/10 p-5 rounded-lg border border-blue-500/30 hover:border-blue-500/50 transition-all duration-300">
                    <div className="text-sm font-medium text-blue-400 mb-2">Vuelos Activos</div>
                    <div className="flex items-baseline gap-2">
                        <div className="text-4xl font-bold text-white">{stats?.flights.active || 0}</div>
                        <div className="text-sm text-blue-400/80">vuelos</div>
                    </div>
                </div>

                <div className="stat-card bg-gradient-to-br from-purple-500/10 to-pink-500/10 p-5 rounded-lg border border-purple-500/30 hover:border-purple-500/50 transition-all duration-300">
                    <div className="text-sm font-medium text-purple-400 mb-2">Asignaciones</div>
                    <div className="flex items-baseline gap-2">
                        <div className="text-4xl font-bold text-white">{stats?.assignments.active || 0}</div>
                        <div className="text-sm text-purple-400/80">activas</div>
                    </div>
                </div>
            </div>
        </div>
    );
}
