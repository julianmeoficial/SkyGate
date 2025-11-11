import { useEffect, useRef, useMemo } from 'react';
import { BuildingOffice2Icon } from '@heroicons/react/24/outline';
import clsx from 'clsx';
import { useGates } from '../../hooks/useGates';
import { useFlights } from '../../hooks/useFlights';
import { useAssignments } from '../../hooks/useAssignments';

function AirplaneIcon({ className }: { className?: string }) {
    return (
        <svg className={className} viewBox="0 0 24 24" fill="currentColor">
            <path d="M21 16v-2l-8-5V3.5c0-.83-.67-1.5-1.5-1.5S10 2.67 10 3.5V9l-8 5v2l8-2.5V19l-2 1.5V22l3.5-1 3.5 1v-1.5L13 19v-5.5l8 2.5z" />
        </svg>
    );
}

export default function RealtimeMetrics() {
    const containerRef = useRef<HTMLDivElement>(null);

    const { gates } = useGates(false);
    const { flights } = useFlights(false);
    const { activeAssignments } = useAssignments();

    const stats = useMemo(() => {
        const gatesFree = gates.filter(g => g.status === 'FREE').length;
        const gatesOccupied = gates.filter(g => g.status === 'OCCUPIED').length;
        const gatesAssigned = gates.filter(g => g.status === 'ASSIGNED').length;
        const gatesMaintenance = gates.filter(g => g.status === 'MAINTENANCE').length;

        const flightsDetected = flights.filter(f => f.automataState === 'S1').length;
        const flightsWithGate = flights.filter(f => f.automataState === 'S4').length;
        const flightsParked = flights.filter(f => f.automataState === 'S5').length;
        const flightsDeparted = flights.filter(f => f.automataState === 'S0').length;

        return {
            gates: {
                free: gatesFree,
                occupied: gatesOccupied,
                assigned: gatesAssigned,
                maintenance: gatesMaintenance
            },
            flights: {
                detected: flightsDetected,
                gateAssigned: flightsWithGate,
                parked: flightsParked,
                departed: flightsDeparted
            }
        };
    }, [gates, flights]);

    const metrics = [
        {
            label: 'Gates por Estado',
            data: [
                { name: 'Libres', value: stats.gates.free, color: 'text-green-400', glowColor: 'shadow-green-500/50', borderColor: 'border-green-500/30', bgGradient: 'from-green-500/10 to-emerald-500/10' },
                { name: 'Ocupados', value: stats.gates.occupied, color: 'text-red-400', glowColor: 'shadow-red-500/50', borderColor: 'border-red-500/30', bgGradient: 'from-red-500/10 to-rose-500/10' },
                { name: 'Asignados', value: stats.gates.assigned, color: 'text-yellow-400', glowColor: 'shadow-yellow-500/50', borderColor: 'border-yellow-500/30', bgGradient: 'from-yellow-500/10 to-amber-500/10' },
                { name: 'Mantenimiento', value: stats.gates.maintenance, color: 'text-orange-400', glowColor: 'shadow-orange-500/50', borderColor: 'border-orange-500/30', bgGradient: 'from-orange-500/10 to-red-500/10' },
            ],
            icon: BuildingOffice2Icon,
            gradient: 'from-blue-600 to-cyan-600',
        },
        {
            label: 'Vuelos por Estado',
            data: [
                { name: 'Detectados', value: stats.flights.detected, color: 'text-blue-400', glowColor: 'shadow-blue-500/50', borderColor: 'border-blue-500/30', bgGradient: 'from-blue-500/10 to-cyan-500/10' },
                { name: 'Con Gate', value: stats.flights.gateAssigned, color: 'text-cyan-400', glowColor: 'shadow-cyan-500/50', borderColor: 'border-cyan-500/30', bgGradient: 'from-cyan-500/10 to-teal-500/10' },
                { name: 'Estacionados', value: stats.flights.parked, color: 'text-purple-400', glowColor: 'shadow-purple-500/50', borderColor: 'border-purple-500/30', bgGradient: 'from-purple-500/10 to-fuchsia-500/10' },
                { name: 'Partidos', value: stats.flights.departed, color: 'text-gray-400', glowColor: 'shadow-gray-500/50', borderColor: 'border-gray-500/30', bgGradient: 'from-gray-500/10 to-slate-500/10' },
            ],
            icon: AirplaneIcon,
            gradient: 'from-purple-600 to-pink-600',
        },
    ];

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && containerRef.current) {
            const cards = containerRef.current.querySelectorAll('.metric-card');

            window.gsap.fromTo(
                cards,
                { opacity: 0, y: 40, rotateX: -15 },
                {
                    opacity: 1,
                    y: 0,
                    rotateX: 0,
                    duration: 0.6,
                    stagger: 0.15,
                    ease: 'back.out(1.3)'
                }
            );

            const statCards = containerRef.current.querySelectorAll('.stat-item');
            statCards.forEach((card, index) => {
                window.gsap.fromTo(
                    card,
                    { opacity: 0, scale: 0.8, x: -30 },
                    {
                        opacity: 1,
                        scale: 1,
                        x: 0,
                        duration: 0.5,
                        ease: 'elastic.out(1, 0.5)',
                        delay: 0.4 + (index * 0.1)
                    }
                );
            });
        }
    }, []);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && containerRef.current) {
            const numbers = containerRef.current.querySelectorAll('.animated-number');
            numbers.forEach((num) => {
                const target = parseInt(num.getAttribute('data-value') || '0');
                const currentValue = parseInt(num.textContent || '0');

                if (currentValue !== target) {
                    const obj = { value: currentValue };

                    window.gsap.to(obj, {
                        value: target,
                        duration: 0.8,
                        ease: 'power2.out',
                        onUpdate: () => {
                            num.textContent = Math.round(obj.value).toString();
                        }
                    });
                }
            });

            const icons = containerRef.current.querySelectorAll('.pulse-icon');
            icons.forEach((icon) => {
                window.gsap.to(icon, {
                    scale: 1.1,
                    duration: 1,
                    repeat: -1,
                    yoyo: true,
                    ease: 'sine.inOut'
                });
            });
        }
    }, [stats]);

    const getTotal = (data: any[]) => data.reduce((sum, item) => sum + item.value, 0);

    return (
        <div ref={containerRef} className="grid grid-cols-1 lg:grid-cols-2 gap-6">
            {metrics.map((metric, idx) => {
                const total = getTotal(metric.data);
                const Icon = metric.icon;

                return (
                    <div
                        key={idx}
                        className="metric-card relative bg-gradient-to-br from-gray-800/60 to-gray-900/80 backdrop-blur-xl rounded-2xl border border-gray-700/50 overflow-hidden hover:border-gray-600/50 transition-all duration-500 hover:scale-[1.02] hover:shadow-2xl"
                    >
                        <div className="absolute inset-0 bg-gradient-to-br from-white/5 to-transparent pointer-events-none" />

                        <div className={`relative bg-gradient-to-r ${metric.gradient} p-6`}>
                            <div className="flex items-center justify-between">
                                <div className="flex items-center gap-4">
                                    <div className="pulse-icon relative p-3 bg-white/20 backdrop-blur-sm rounded-xl shadow-lg">
                                        <Icon className="w-7 h-7 text-white" />
                                        <div className="absolute inset-0 bg-white/30 rounded-xl blur-xl animate-pulse" />
                                    </div>
                                    <div>
                                        <h3 className="text-xl font-bold text-white tracking-tight">{metric.label}</h3>
                                        <p className="text-sm text-white/90 font-medium mt-1">Total: {total}</p>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="relative p-6">
                            <div className="grid grid-cols-2 gap-4">
                                {metric.data.map((item, itemIdx) => (
                                    <div
                                        key={itemIdx}
                                        className={clsx(
                                            'stat-item group relative backdrop-blur-sm rounded-xl p-5 border transition-all duration-300 hover:scale-105 hover:shadow-xl bg-gradient-to-br',
                                            item.borderColor,
                                            item.glowColor,
                                            item.bgGradient
                                        )}
                                    >
                                        <div className="absolute inset-0 bg-gradient-to-br from-white/5 to-transparent rounded-xl opacity-0 group-hover:opacity-100 transition-opacity duration-300" />

                                        <div className="relative space-y-3">
                                            <div className="flex items-center justify-between">
                                                <span className="text-xs font-bold text-gray-400 uppercase tracking-wider">
                                                    {item.name}
                                                </span>
                                                <div className={clsx('w-2.5 h-2.5 rounded-full animate-pulse', item.color.replace('text-', 'bg-'))} />
                                            </div>

                                            <div className="flex items-center justify-center py-2">
                                                <span
                                                    className={clsx('animated-number text-5xl font-black tracking-tight', item.color)}
                                                    data-value={item.value}
                                                >
                                                    {item.value}
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        </div>

                        <div className="absolute -right-10 -bottom-10 w-40 h-40 bg-gradient-to-br from-white/10 to-transparent rounded-full blur-3xl opacity-20 pointer-events-none" />
                    </div>
                );
            })}
        </div>
    );
}
