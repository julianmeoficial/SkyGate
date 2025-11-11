import { useState } from 'react';
import { BellIcon } from '@heroicons/react/24/outline';
import { useStore } from '../../store';
import { NotificationDropdown } from '../notifications/NotificationDropdown';
import { useWebSocket } from '../../api/websocket/useWebSocket';
import clsx from 'clsx';

export default function Navbar() {
    const [showNotifications, setShowNotifications] = useState(false);
    const dropdownNotifications = useStore(state => state.dropdownNotifications);
    const { status } = useWebSocket();

    const unreadCount = dropdownNotifications.filter(n => !n.read).length;

    const statusColor: Record<string, string> = {
        CONNECTED: 'bg-green-500',
        CONNECTING: 'bg-yellow-500',
        DISCONNECTED: 'bg-red-500',
        ERROR: 'bg-red-600',
    };

    const statusText: Record<string, string> = {
        CONNECTED: 'Sistema Conectado',
        CONNECTING: 'Conectando...',
        DISCONNECTED: 'Sistema Desconectado',
        ERROR: 'Error de Conexión',
    };

    return (
        <nav className="bg-gray-950 border-b border-gray-800 px-6 py-4">
            <div className="flex items-center justify-between">
                <div className="flex items-center gap-4">
                    <h1 className="text-2xl font-bold text-white">SkyGate</h1>
                    <span className="text-sm text-gray-400">Sistema de Control de Gates</span>
                </div>

                <div className="flex items-center gap-6">
                    <div className="flex items-center gap-2">
                        <div className={clsx('w-2 h-2 rounded-full', statusColor[status])} />
                        <span className="text-sm text-gray-300">{statusText[status]}</span>
                    </div>

                    <NotificationDropdown />

                    <div className="flex items-center gap-3">
                        <div className="text-right">
                            <p className="text-sm font-medium text-white">Admin</p>
                            <p className="text-xs text-gray-400">Sistema</p>
                        </div>
                        <div className="w-10 h-10 rounded-full bg-gradient-to-br from-blue-500 to-purple-600 flex items-center justify-center text-white font-bold">
                            A
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    );
}
