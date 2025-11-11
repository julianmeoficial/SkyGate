import { useEffect, useState } from 'react';
import {
    PaperAirplaneIcon,
    BuildingOffice2Icon,
    CheckCircleIcon,
    ClockIcon
} from '@heroicons/react/24/outline';
import { useFlights } from '../../hooks/useFlights';
import { useGates } from '../../hooks/useGates';
import LoaderDoubleSpinner from '../common/Loader/LoaderDoubleSpinner';

interface MetricsGridProps {
    isLoading?: boolean;
}

export default function MetricsGrid({ isLoading = false }: MetricsGridProps) {
    const { flights } = useFlights();
    const { gates } = useGates();
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        if (!isLoading) {
            setTimeout(() => setVisible(true), 100);
        }
    }, [isLoading]);

    const activeFlights = flights.filter(f => f.status !== 'DEPARTED').length;
    const totalGates = gates.length;
    const occupiedGates = gates.filter(g => g.status === 'OCCUPIED').length;
    const freeGates = gates.filter(g => g.status === 'FREE').length;

    const metrics = [
        {
            label: 'Vuelos Activos',
            value: activeFlights,
            icon: PaperAirplaneIcon,
            gradient: 'from-blue-500 to-cyan-500',
        },
        {
            label: 'Gates Totales',
            value: totalGates,
            icon: BuildingOffice2Icon,
            gradient: 'from-purple-500 to-pink-500',
        },
        {
            label: 'Gates Libres',
            value: freeGates,
            icon: CheckCircleIcon,
            gradient: 'from-green-500 to-emerald-500',
        },
        {
            label: 'Gates Ocupados',
            value: occupiedGates,
            icon: ClockIcon,
            gradient: 'from-orange-500 to-red-500',
        },
    ];

    if (isLoading) {
        return (
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
                {[...Array(4)].map((_, i) => (
                    <div key={i} className="bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg">
                        <LoaderDoubleSpinner />
                    </div>
                ))}
            </div>
        );
    }

    return (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
            {metrics.map((metric, index) => (
                <div
                    key={metric.label}
                    className={`bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg transition-all duration-500 ${
                        visible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
                    }`}
                    style={{ transitionDelay: `${index * 100}ms` }}
                >
                    <div className="flex items-center justify-between">
                        <div>
                            <p className="text-sm text-gray-400">{metric.label}</p>
                            <p className="text-3xl font-bold text-white mt-2">{metric.value}</p>
                        </div>
                        <div className={`w-14 h-14 rounded-xl bg-gradient-to-br ${metric.gradient} flex items-center justify-center shadow-lg`}>
                            <metric.icon className="w-7 h-7 text-white" />
                        </div>
                    </div>
                </div>
            ))}
        </div>
    );
}
