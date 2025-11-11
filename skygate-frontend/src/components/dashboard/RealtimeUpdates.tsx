import { useEffect, useState } from 'react';
import { ClockIcon } from '@heroicons/react/24/outline';
import { useFlights } from '../../hooks/useFlights';
import LoaderDoubleSpinner from '../common/Loader/LoaderDoubleSpinner';

interface RealtimeUpdatesProps {
    isLoading?: boolean;
}

export default function RealtimeUpdates({ isLoading = false }: RealtimeUpdatesProps) {
    const { flights } = useFlights();
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        if (!isLoading) {
            setTimeout(() => setVisible(true), 300);
        }
    }, [isLoading]);

    if (isLoading) {
        return (
            <div className="bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg">
                <LoaderDoubleSpinner />
            </div>
        );
    }

    return (
        <div className={`bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg transition-all duration-500 ${
            visible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
        }`}>
            <div className="flex items-center gap-2 mb-4">
                <ClockIcon className="w-5 h-5 text-blue-500" />
                <h3 className="text-lg font-semibold text-white">Actualizaciones en Tiempo Real</h3>
            </div>

            <div className="space-y-2">
                <p className="text-sm text-gray-400">Total de vuelos monitoreados: <span className="text-white font-semibold">{flights.length}</span></p>
                <p className="text-xs text-gray-500">Última actualización: {new Date().toLocaleTimeString()}</p>
            </div>
        </div>
    );
}
