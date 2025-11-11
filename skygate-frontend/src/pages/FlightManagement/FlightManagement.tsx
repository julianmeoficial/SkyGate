import { useState } from 'react';
import { useFlights } from '../../hooks/useFlights';
import { PlusIcon, PaperAirplaneIcon } from '@heroicons/react/24/outline';
import FlightDetectorModal from '../../components/flights/FlightDetectorModal';
import FlightTable from '../../components/flights/FlightTable';
import FlightProcessVisualization from '../../components/flights/FlightProcessVisualization';
import LoaderBounceDots from '../../components/common/Loader/LoaderBounceDots';

export default function FlightManagement() {
    const [showDetector, setShowDetector] = useState(false);
    const [selectedFlight, setSelectedFlight] = useState<number | null>(null);
    const { flights, isLoading } = useFlights();

    const activeFlight = flights.find(f => f.id === selectedFlight);

    return (
        <div className="space-y-6">
            <div className="flex items-center justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-white">Gestión de Vuelos</h1>
                    <p className="text-gray-400 mt-1">Administración y detección de vuelos en el sistema</p>
                </div>

                <button
                    onClick={() => setShowDetector(true)}
                    className="flex items-center gap-2 px-6 py-3 bg-gradient-to-r from-blue-600 to-blue-700 hover:from-blue-700 hover:to-blue-800 text-white rounded-lg transition-all shadow-lg hover:shadow-xl transform hover:scale-105"
                >
                    <PlusIcon className="w-5 h-5" />
                    Detectar Vuelo
                </button>
            </div>

            {activeFlight && (
                <FlightProcessVisualization flight={activeFlight} />
            )}

            <div className="bg-gray-800 rounded-xl border border-gray-700 overflow-hidden">
                <div className="px-6 py-4 border-b border-gray-700 bg-gray-800/50">
                    <div className="flex items-center justify-between">
                        <div className="flex items-center gap-3">
                            <div className="w-10 h-10 rounded-lg bg-blue-500/10 flex items-center justify-center">
                                <PaperAirplaneIcon className="w-5 h-5 text-blue-500" />
                            </div>
                            <div>
                                <h2 className="text-lg font-semibold text-white">Vuelos Activos</h2>
                                <p className="text-xs text-gray-400">
                                    {flights.length} {flights.length === 1 ? 'vuelo' : 'vuelos'} en el sistema
                                </p>
                            </div>
                        </div>
                        <div className="flex items-center gap-2 px-3 py-1.5 bg-gray-900/50 rounded-lg border border-gray-700">
                            <div className="w-2 h-2 rounded-full bg-green-500 animate-pulse" />
                            <span className="text-xs font-medium text-gray-300">En tiempo real</span>
                        </div>
                    </div>
                </div>

                <div className="p-6">
                    {isLoading ? (
                        <div className="flex flex-col items-center justify-center py-16">
                            <LoaderBounceDots />
                            <p className="text-gray-400 mt-6 text-sm">Cargando vuelos...</p>
                        </div>
                    ) : flights.length === 0 ? (
                        <div className="flex flex-col items-center justify-center py-16">
                            <div className="w-20 h-20 rounded-full bg-blue-500/10 flex items-center justify-center mb-6">
                                <PaperAirplaneIcon className="w-10 h-10 text-blue-500/50" />
                            </div>
                            <LoaderBounceDots />
                            <p className="text-gray-300 font-medium mt-6">Esperando detecciones...</p>
                            <p className="text-sm text-gray-500 mt-2">
                                El sistema está monitoreando vuelos entrantes
                            </p>
                            <button
                                onClick={() => setShowDetector(true)}
                                className="mt-6 px-4 py-2 bg-blue-600/20 hover:bg-blue-600/30 border border-blue-500/30 text-blue-400 rounded-lg transition-colors text-sm font-medium"
                            >
                                Detectar vuelo manualmente
                            </button>
                        </div>
                    ) : (
                        <FlightTable
                            flights={flights}
                            onSelectFlight={setSelectedFlight}
                            selectedFlightId={selectedFlight}
                        />
                    )}
                </div>
            </div>

            {showDetector && (
                <FlightDetectorModal onClose={() => setShowDetector(false)} />
            )}
        </div>
    );
}
