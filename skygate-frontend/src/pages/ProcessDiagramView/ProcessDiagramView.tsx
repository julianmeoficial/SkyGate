import { useState, useEffect, useRef } from 'react';
import { useFlights } from '../../hooks/useFlights';
import DetailedProcessFlow from '../../components/automata/DetailedProcessFlow';
import { ChartBarIcon } from '@heroicons/react/24/outline';
import clsx from 'clsx';

export default function ProcessDiagramView() {
    const { flights, isLoading } = useFlights();
    const [selectedFlightId, setSelectedFlightId] = useState<number | null>(null);
    const selectedFlight = flights.find(f => f.id === selectedFlightId);
    const headerRef = useRef<HTMLDivElement>(null);
    const selectorRef = useRef<HTMLDivElement>(null);
    const flowRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap) {
            if (headerRef.current) {
                window.gsap.fromTo(
                    headerRef.current,
                    { opacity: 0, y: -20 },
                    { opacity: 1, y: 0, duration: 0.5, ease: 'power2.out' }
                );
            }
            if (selectorRef.current && flights.length > 0) {
                window.gsap.fromTo(
                    selectorRef.current.children,
                    { opacity: 0, x: -20 },
                    { opacity: 1, x: 0, duration: 0.4, stagger: 0.08, ease: 'power2.out', delay: 0.2 }
                );
            }
        }
    }, [flights.length]);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && flowRef.current && selectedFlight) {
            window.gsap.fromTo(
                flowRef.current,
                { opacity: 0, y: 20 },
                { opacity: 1, y: 0, duration: 0.5, ease: 'power2.out' }
            );
        }
    }, [selectedFlight]);

    if (isLoading) {
        return (
            <div className="flex items-center justify-center min-h-screen">
                <div className="text-center space-y-4">
                    <div className="relative">
                        <div className="w-16 h-16 border-4 border-gray-700 border-t-purple-500 rounded-full animate-spin mx-auto" />
                        <div className="absolute inset-0 w-16 h-16 border-4 border-transparent border-t-blue-500 rounded-full animate-spin animation-delay-150 mx-auto" />
                    </div>
                    <p className="text-lg font-semibold text-gray-300 animate-pulse">Cargando vuelos...</p>
                </div>
            </div>
        );
    }

    return (
        <div className="p-8 space-y-6">
            <div ref={headerRef} className="flex items-start justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-white mb-2">Diagrama de Proceso del Autómata</h1>
                    <p className="text-gray-400">Visualización detallada del flujo completo con bifurcaciones I3/I4</p>
                </div>
                {flights.length > 0 && (
                    <div className="flex items-center gap-3 px-4 py-2 bg-blue-500/10 rounded-lg border border-blue-500/30">
                        <div className="w-2 h-2 rounded-full bg-blue-500 animate-pulse" />
                        <span className="text-sm font-semibold text-white">
                            {flights.length} Vuelo{flights.length !== 1 ? 's' : ''}
                        </span>
                    </div>
                )}
            </div>

            {flights.length === 0 ? (
                <div className="text-center py-20">
                    <ChartBarIcon className="w-20 h-20 text-gray-600 mx-auto mb-4" />
                    <p className="text-xl font-semibold text-gray-400 mb-2">No hay vuelos activos</p>
                    <p className="text-gray-500">Detecta un vuelo para ver su proceso en el autómata</p>
                </div>
            ) : (
                <>
                    <div className="space-y-4">
                        <h2 className="text-xl font-bold text-white">
                            Selecciona un Vuelo <span className="text-gray-500 font-normal text-base">({flights.length} activo{flights.length !== 1 ? 's' : ''})</span>
                        </h2>
                        <div ref={selectorRef} className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-4">
                            {flights.map(flight => (
                                <button
                                    key={flight.id}
                                    onClick={() => setSelectedFlightId(flight.id)}
                                    className={clsx(
                                        'p-4 rounded-lg border-2 transition-all duration-300 text-left group',
                                        selectedFlightId === flight.id
                                            ? 'bg-purple-500/10 border-purple-500 scale-105'
                                            : 'bg-gray-800/40 border-gray-700/50 hover:border-gray-600 hover:bg-gray-800/60'
                                    )}
                                >
                                    <div className="space-y-2">
                                        <div className="flex items-center justify-between">
                                            <span className="text-lg font-bold text-white">{flight.flightNumber}</span>
                                            <span className={clsx(
                                                'px-2 py-0.5 text-xs font-bold rounded',
                                                selectedFlightId === flight.id
                                                    ? 'bg-purple-500/20 text-purple-300'
                                                    : 'bg-gray-700 text-gray-300'
                                            )}>
                                                {flight.automataState}
                                            </span>
                                        </div>
                                        <div className="flex items-center gap-2 text-sm text-gray-400">
                                            <span>{flight.origin}</span>
                                            <span>→</span>
                                            <span>{flight.destination}</span>
                                        </div>
                                        <div className="text-xs text-gray-500">{flight.aircraftType}</div>
                                    </div>
                                </button>
                            ))}
                        </div>
                    </div>

                    {selectedFlight && (
                        <div ref={flowRef}>
                            <DetailedProcessFlow flight={selectedFlight} />
                        </div>
                    )}
                </>
            )}
        </div>
    );
}
