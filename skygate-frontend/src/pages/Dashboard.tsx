import { useEffect, useState } from 'react';
import MetricsGrid from '../components/dashboard/MetricsGrid';
import SystemStatus from '../components/dashboard/SystemStatus';
import RealtimeUpdates from '../components/dashboard/RealtimeUpdates';
import LiveActivity from '../components/dashboard/LiveActivity';
import UtilizationChart from '../components/dashboard/UtilizationChart';

export default function Dashboard() {
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const timer = setTimeout(() => {
            setIsLoading(false);
        }, 800);

        return () => clearTimeout(timer);
    }, []);

    return (
        <div className="space-y-6">
            <div>
                <h1 className="text-3xl font-bold text-white">Dashboard</h1>
                <p className="text-gray-400 mt-1">Monitor de operaciones en tiempo real</p>
            </div>

            <SystemStatus isLoading={isLoading} />

            <MetricsGrid isLoading={isLoading} />

            <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
                <UtilizationChart isLoading={isLoading} />
                <LiveActivity isLoading={isLoading} />
            </div>

            <RealtimeUpdates isLoading={isLoading} />
        </div>
    );
}
