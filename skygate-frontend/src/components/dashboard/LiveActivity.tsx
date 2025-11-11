import { useEffect, useState } from 'react';
import { BoltIcon } from '@heroicons/react/24/solid';
import { useFlights } from '../../hooks/useFlights';
import LoaderDoubleSpinner from '../common/Loader/LoaderDoubleSpinner';

interface LiveActivityProps {
    isLoading?: boolean;
}

export default function LiveActivity({ isLoading = false }: LiveActivityProps) {
    const { flights } = useFlights();
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        if (!isLoading) {
            setTimeout(() => setVisible(true), 200);
        }
    }, [isLoading]);

    if (isLoading) {
        return (
            <div className="bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg">
                <LoaderDoubleSpinner />
            </div>
        );
    }

    const recentFlights = flights
        .sort((a, b) => new Date(b.detectedAt || 0).getTime() - new Date(a.detectedAt || 0).getTime())
        .slice(0, 5);

    return (
        <div className={`bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg transition-all duration-500 ${
            visible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
        }`}>
            <div className="flex items-center gap-2 mb-4">
                <BoltIcon className="w-5 h-5 text-yellow-500" />
                <h3 className="text-lg font-semibold text-white">Actividad Reciente</h3>
            </div>

            <div className="space-y-3">
                {recentFlights.length > 0 ? (
                    recentFlights.map((flight) => (
                        <div
                            key={flight.id}
                            className="flex items-center justify-between p-3 bg-gray-900/50 rounded-lg border border-gray-700 hover:border-gray-600 transition-colors"
                        >
                            <div>
                                <p className="text-sm font-semibold text-white">{flight.flightNumber}</p>
                                <p className="text-xs text-gray-400">{flight.airline || 'Sin aerolínea'}</p>
                            </div>
                            <div className="text-right">
                                <p className="text-xs text-gray-400">Estado</p>
                                <p className="text-sm font-medium text-blue-400">{flight.status}</p>
                            </div>
                        </div>
                    ))
                ) : (
                    <div className="text-center py-8">
                        <p className="text-gray-500">No hay actividad reciente</p>
                    </div>
                )}
            </div>
        </div>
    );
}
