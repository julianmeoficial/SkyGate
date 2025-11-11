import { useState, useEffect, useRef } from 'react';
import { useGates } from '../../hooks/useGates';
import GatePanel from '../../components/gates/GatePanel';

export default function GateManagement() {
    const { gates, gatesByTerminal, isLoading } = useGates();
    const [selectedTerminal, setSelectedTerminal] = useState<string | null>(null);
    const terminals = Object.keys(gatesByTerminal);
    const headerRef = useRef<HTMLDivElement>(null);
    const filtersRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap) {
            if (headerRef.current) {
                window.gsap.fromTo(
                    headerRef.current,
                    { opacity: 0, y: -30 },
                    { opacity: 1, y: 0, duration: 0.6, ease: 'power3.out' }
                );
            }
            if (filtersRef.current) {
                window.gsap.fromTo(
                    filtersRef.current.children,
                    { opacity: 0, x: -20 },
                    {
                        opacity: 1,
                        x: 0,
                        duration: 0.4,
                        stagger: 0.08,
                        ease: 'power2.out',
                        delay: 0.3
                    }
                );
            }
        }
    }, []);

    return (
        <div className="p-8 space-y-6">
            <div ref={headerRef} className="flex items-start justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-white mb-2">Gestión de Gates</h1>
                    <p className="text-gray-400">Administración de puertas de embarque por terminal</p>
                </div>
                <div className="flex items-center gap-3 px-4 py-2 bg-emerald-500/10 rounded-lg border border-emerald-500/30">
                    <div className="w-2 h-2 rounded-full bg-emerald-500 animate-pulse" />
                    <span className="text-sm font-semibold text-white">{gates.length} Gates</span>
                </div>
            </div>

            <div ref={filtersRef} className="flex flex-wrap gap-3 items-center">
                <span className="text-sm font-semibold text-gray-500 uppercase tracking-wider">Filtrar:</span>
                <button
                    onClick={() => setSelectedTerminal(null)}
                    className={`relative px-3 lg:px-4 py-1.5 lg:py-2 rounded-lg font-semibold text-xs lg:text-sm transition-all duration-300 overflow-hidden group ${
                        selectedTerminal === null
                            ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white shadow-lg shadow-blue-500/30 scale-105'
                            : 'bg-gray-800/80 text-gray-400 hover:text-white border border-gray-700/50 hover:border-gray-600 hover:bg-gray-700/90'
                    }`}
                >
                    {selectedTerminal === null && (
                        <span className="absolute inset-0 bg-gradient-to-r from-blue-400/20 to-purple-400/20 animate-pulse" />
                    )}
                    <span className="relative flex items-center gap-1.5">
                        <svg className="w-3 h-3 lg:w-4 lg:h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M4 6h16M4 12h16M4 18h16" />
                        </svg>
                        Todas
                    </span>
                </button>

                {terminals.map((terminal) => (
                    <button
                        key={terminal}
                        onClick={() => setSelectedTerminal(terminal)}
                        className={`relative px-3 lg:px-4 py-1.5 lg:py-2 rounded-lg font-semibold text-xs lg:text-sm transition-all duration-300 overflow-hidden group ${
                            selectedTerminal === terminal
                                ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white shadow-lg shadow-blue-500/30 scale-105'
                                : 'bg-gray-800/80 text-gray-400 hover:text-white border border-gray-700/50 hover:border-gray-600 hover:bg-gray-700/90'
                        }`}
                    >
                        {selectedTerminal === terminal && (
                            <span className="absolute inset-0 bg-gradient-to-r from-blue-400/20 to-purple-400/20 animate-pulse" />
                        )}
                        <span className="relative flex items-center gap-1.5">
                            <span className="flex items-center justify-center w-4 h-4 lg:w-5 lg:h-5 rounded bg-white/20 text-[10px] lg:text-xs">
                                T
                            </span>
                            Terminal {terminal}
                        </span>
                    </button>
                ))}
            </div>

            <div className="relative">
                {isLoading ? (
                    <div className="flex flex-col items-center justify-center py-20 space-y-6">
                        <div className="relative">
                            <div className="w-16 h-16 border-4 border-gray-700 border-t-blue-500 rounded-full animate-spin" />
                            <div className="absolute inset-0 w-16 h-16 border-4 border-transparent border-t-purple-500 rounded-full animate-spin animation-delay-150" />
                        </div>
                        <div className="text-center space-y-2">
                            <p className="text-lg font-semibold text-gray-300 animate-pulse">Cargando gates...</p>
                            <p className="text-sm text-gray-500">Obteniendo información de las terminales</p>
                        </div>
                    </div>
                ) : (
                    <GatePanel
                        gates={selectedTerminal === null ? gates : gatesByTerminal[selectedTerminal] || []}
                        groupByTerminal={selectedTerminal === null}
                    />
                )}
            </div>
        </div>
    );
}
