import { SignalIcon, SignalSlashIcon } from '@heroicons/react/24/outline';
import { useWebSocket } from '../../api/websocket/useWebSocket';
import LoaderDoubleSpinner from '../common/Loader/LoaderDoubleSpinner';
import { useEffect, useState } from 'react';

interface SystemStatusProps {
    isLoading?: boolean;
}

export default function SystemStatus({ isLoading = false }: SystemStatusProps) {
    const { isConnected } = useWebSocket();
    const [showStatus, setShowStatus] = useState(false);

    useEffect(() => {
        const timer = setTimeout(() => {
            setShowStatus(true);
        }, 300);

        return () => clearTimeout(timer);
    }, []);

    if (isLoading) {
        return (
            <div className="bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg">
                <LoaderDoubleSpinner />
            </div>
        );
    }

    const displayConnected = showStatus && isConnected;

    return (
        <div className="bg-gray-800 rounded-xl border border-gray-700 p-6 shadow-lg">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-lg font-semibold text-white">Estado del Sistema</h2>
                    <p className="text-sm text-gray-400 mt-1">Monitoreo en tiempo real</p>
                </div>

                <div className="flex items-center gap-3">
                    <div className="flex items-center gap-2 px-4 py-2 rounded-lg bg-gray-900/50 border border-gray-700">
                        {displayConnected ? (
                            <>
                                <div className="relative flex items-center">
                                    <div className="absolute inset-0 bg-green-500 rounded-full blur-md opacity-50 animate-pulse" />
                                    <SignalIcon className="relative w-5 h-5 text-green-500" />
                                </div>
                                <div>
                                    <p className="text-xs text-gray-400">WebSocket</p>
                                    <p className="text-sm font-semibold text-green-400">Conectado</p>
                                </div>
                            </>
                        ) : (
                            <>
                                <SignalSlashIcon className="w-5 h-5 text-red-500" />
                                <div>
                                    <p className="text-xs text-gray-400">WebSocket</p>
                                    <p className="text-sm font-semibold text-red-400">
                                        {showStatus ? 'Desconectado' : 'Conectando...'}
                                    </p>
                                </div>
                            </>
                        )}
                    </div>

                    <div className="flex items-center gap-2 px-4 py-2 rounded-lg bg-blue-500/10 border border-blue-500/30">
                        <div className="w-2 h-2 rounded-full bg-blue-500 animate-pulse" />
                        <span className="text-sm font-medium text-blue-400">Sistema Activo</span>
                    </div>
                </div>
            </div>
        </div>
    );
}
