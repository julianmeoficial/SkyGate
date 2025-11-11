import { useEffect, useRef } from 'react';
import { useQuery } from '@tanstack/react-query';
import { monitoringService } from '../../api/services/monitoring.service';
import { useStore } from '../../store';
import SystemHealthCard from '../../components/monitoring/SystemHealthCard';
import RealtimeMetrics from '../../components/monitoring/RealtimeMetrics';

export default function MonitoringView() {
    const { data: stats, isLoading } = useQuery({
        queryKey: ['monitoring-stats'],
        queryFn: monitoringService.getDashboardStats,
        refetchInterval: 3000,
    });

    const transitionHistory = useStore(state => state.transitionHistory);
    const headerRef = useRef<HTMLDivElement>(null);
    const healthRef = useRef<HTMLDivElement>(null);
    const metricsRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap) {
            if (headerRef.current) {
                window.gsap.fromTo(
                    headerRef.current,
                    { opacity: 0, y: -30 },
                    { opacity: 1, y: 0, duration: 0.6, ease: 'power3.out' }
                );
            }
            if (healthRef.current) {
                window.gsap.fromTo(
                    healthRef.current,
                    { opacity: 0, scale: 0.95 },
                    { opacity: 1, scale: 1, duration: 0.5, ease: 'back.out(1.2)', delay: 0.2 }
                );
            }
            if (metricsRef.current) {
                window.gsap.fromTo(
                    metricsRef.current,
                    { opacity: 0, y: 30 },
                    { opacity: 1, y: 0, duration: 0.5, ease: 'power2.out', delay: 0.3 }
                );
            }
        }
    }, []);

    return (
        <div className="p-8 space-y-8">
            <div ref={headerRef} className="space-y-2">
                <div className="flex items-center gap-3">
                    <div className="w-1 h-8 bg-gradient-to-b from-green-500 to-emerald-500 rounded-full" />
                    <h1 className="text-3xl font-bold text-white">Monitoreo del Sistema</h1>
                    <div className="flex items-center gap-2 px-3 py-1.5 bg-green-500/10 rounded-full border border-green-500/30">
                        <div className="w-2 h-2 bg-green-500 rounded-full animate-pulse" />
                        <span className="text-xs font-semibold text-green-400">En Vivo</span>
                    </div>
                </div>
                <p className="text-gray-400 text-sm ml-4">Estado en tiempo real y métricas del sistema SkyGate</p>
            </div>

            {isLoading ? (
                <div className="flex flex-col items-center justify-center py-20 space-y-4">
                    <div className="relative">
                        <div className="w-16 h-16 border-4 border-gray-800 border-t-green-500 rounded-full animate-spin" />
                        <div className="absolute inset-0 w-16 h-16 border-4 border-transparent border-t-emerald-500 rounded-full animate-spin animation-delay-150" />
                    </div>
                    <p className="text-gray-400 font-medium">Cargando métricas...</p>
                </div>
            ) : (
                <div className="space-y-6">
                    <div ref={healthRef}>
                        <SystemHealthCard stats={stats?.data} />
                    </div>
                    <div ref={metricsRef}>
                        <RealtimeMetrics />
                    </div>
                </div>
            )}
        </div>
    );
}
