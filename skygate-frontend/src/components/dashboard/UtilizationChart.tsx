import { useEffect, useState } from 'react';
import { ChartBarIcon } from '@heroicons/react/24/outline';
import { useGates } from '../../hooks/useGates';
import LoaderDoubleSpinner from '../common/Loader/LoaderDoubleSpinner';

interface UtilizationChartProps {
    isLoading?: boolean;
}

export default function UtilizationChart({ isLoading = false }: UtilizationChartProps) {
    const { gates } = useGates();
    const [visible, setVisible] = useState(false);

    useEffect(() => {
        if (!isLoading) {
            setTimeout(() => setVisible(true), 250);
        }
    }, [isLoading]);

    if (isLoading) {
        return (
            <div className="bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg">
                <LoaderDoubleSpinner />
            </div>
        );
    }

    const occupiedGates = gates.filter(g => g.status === 'OCCUPIED').length;
    const totalGates = gates.length;
    const utilizationPercentage = totalGates > 0 ? (occupiedGates / totalGates) * 100 : 0;

    return (
        <div className={`bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg transition-all duration-500 ${
            visible ? 'opacity-100 translate-y-0' : 'opacity-0 translate-y-4'
        }`}>
            <div className="flex items-center gap-2 mb-6">
                <ChartBarIcon className="w-5 h-5 text-purple-500" />
                <h3 className="text-lg font-semibold text-white">Utilización de Gates</h3>
            </div>

            <div className="space-y-4">
                <div className="relative">
                    <div className="flex items-center justify-between mb-2">
                        <span className="text-sm text-gray-400">Ocupación</span>
                        <span className="text-lg font-bold text-white">{utilizationPercentage.toFixed(1)}%</span>
                    </div>
                    <div className="w-full h-4 bg-gray-700 rounded-full overflow-hidden">
                        <div
                            className="h-full bg-gradient-to-r from-blue-500 to-purple-500 transition-all duration-1000"
                            style={{ width: `${utilizationPercentage}%` }}
                        />
                    </div>
                </div>

                <div className="grid grid-cols-2 gap-4 pt-4 border-t border-gray-700">
                    <div>
                        <p className="text-xs text-gray-400">Ocupados</p>
                        <p className="text-2xl font-bold text-white">{occupiedGates}</p>
                    </div>
                    <div>
                        <p className="text-xs text-gray-400">Libres</p>
                        <p className="text-2xl font-bold text-white">{totalGates - occupiedGates}</p>
                    </div>
                </div>
            </div>
        </div>
    );
}
