import { useState, useEffect, useRef } from 'react';
import { BellIcon, XMarkIcon, CheckIcon, TrashIcon } from '@heroicons/react/24/outline';
import { useStore } from '../../store';
import { formatDistanceToNow } from 'date-fns';
import { es } from 'date-fns/locale';

export const NotificationDropdown = () => {
    const [isOpen, setIsOpen] = useState(false);
    const dropdownRef = useRef<HTMLDivElement>(null);

    const dropdownNotifications = useStore((state) => state.dropdownNotifications);
    const markAsRead = useStore((state) => state.markAsRead);
    const removeDropdownNotification = useStore((state) => state.removeDropdownNotification);
    const clearAllDropdownNotifications = useStore((state) => state.clearAllDropdownNotifications);
    const clearReadNotifications = useStore((state) => state.clearReadNotifications);

    const unreadCount = dropdownNotifications.filter((n) => !n.read).length;

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (dropdownRef.current && !dropdownRef.current.contains(event.target as Node)) {
                setIsOpen(false);
            }
        };

        if (isOpen) {
            document.addEventListener('mousedown', handleClickOutside);
        }

        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, [isOpen]);

    const getNotificationStyles = (type: string) => {
        const styles = {
            success: 'bg-green-50 border-green-200 text-green-800',
            error: 'bg-red-50 border-red-200 text-red-800',
            warning: 'bg-yellow-50 border-yellow-200 text-yellow-800',
            info: 'bg-blue-50 border-blue-200 text-blue-800',
        };
        return styles[type as keyof typeof styles] || styles.info;
    };

    const getNotificationIcon = (type: string) => {
        const icons = {
            success: (
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-green-100 flex items-center justify-center">
                    <CheckIcon className="w-5 h-5 text-green-600" />
                </div>
            ),
            error: (
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-red-100 flex items-center justify-center">
                    <XMarkIcon className="w-5 h-5 text-red-600" />
                </div>
            ),
            warning: (
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-yellow-100 flex items-center justify-center">
                    <span className="text-yellow-600 font-bold">!</span>
                </div>
            ),
            info: (
                <div className="flex-shrink-0 w-8 h-8 rounded-full bg-blue-100 flex items-center justify-center">
                    <BellIcon className="w-5 h-5 text-blue-600" />
                </div>
            ),
        };
        return icons[type as keyof typeof icons] || icons.info;
    };

    const handleNotificationClick = (id: string, read: boolean) => {
        if (!read) {
            markAsRead(id);
        }
    };

    return (
        <div className="relative" ref={dropdownRef}>
            <button
                onClick={() => setIsOpen(!isOpen)}
                className="relative p-2 text-gray-600 hover:text-gray-900 hover:bg-gray-100 rounded-lg transition-colors"
                aria-label="Notificaciones"
            >
                <BellIcon className="w-6 h-6" />
                {unreadCount > 0 && (
                    <span className="absolute top-1 right-1 w-5 h-5 bg-red-500 text-white text-xs rounded-full flex items-center justify-center font-semibold">
                        {unreadCount > 9 ? '9+' : unreadCount}
                    </span>
                )}
            </button>

            {isOpen && (
                <div className="absolute right-0 mt-2 w-96 bg-white rounded-lg shadow-xl border border-gray-200 z-50">
                    <div className="flex items-center justify-between p-4 border-b border-gray-200">
                        <h3 className="text-lg font-semibold text-gray-900">Notificaciones</h3>
                        <div className="flex gap-2">
                            {dropdownNotifications.some((n) => n.read) && (
                                <button
                                    onClick={clearReadNotifications}
                                    className="text-sm text-gray-600 hover:text-gray-900 transition-colors"
                                    title="Limpiar leídas"
                                >
                                    Limpiar leídas
                                </button>
                            )}
                            {dropdownNotifications.length > 0 && (
                                <button
                                    onClick={clearAllDropdownNotifications}
                                    className="text-sm text-red-600 hover:text-red-700 transition-colors"
                                    title="Limpiar todas"
                                >
                                    Limpiar todas
                                </button>
                            )}
                        </div>
                    </div>

                    <div className="max-h-96 overflow-y-auto">
                        {dropdownNotifications.length === 0 ? (
                            <div className="p-8 text-center">
                                <BellIcon className="w-12 h-12 mx-auto text-gray-400 mb-2" />
                                <p className="text-gray-500">No hay notificaciones</p>
                            </div>
                        ) : (
                            <div className="divide-y divide-gray-100">
                                {dropdownNotifications.map((notification) => (
                                    <div
                                        key={notification.id}
                                        onClick={() => handleNotificationClick(notification.id, notification.read)}
                                        className={`p-4 hover:bg-gray-50 cursor-pointer transition-colors ${
                                            !notification.read ? 'bg-blue-50' : ''
                                        }`}
                                    >
                                        <div className="flex gap-3">
                                            {getNotificationIcon(notification.type)}
                                            <div className="flex-1 min-w-0">
                                                <p className={`text-sm ${!notification.read ? 'font-semibold' : ''} text-gray-900`}>
                                                    {notification.message}
                                                </p>
                                                <p className="text-xs text-gray-500 mt-1">
                                                    {formatDistanceToNow(notification.timestamp, {
                                                        addSuffix: true,
                                                        locale: es,
                                                    })}
                                                </p>
                                            </div>
                                            <button
                                                onClick={(e) => {
                                                    e.stopPropagation();
                                                    removeDropdownNotification(notification.id);
                                                }}
                                                className="flex-shrink-0 text-gray-400 hover:text-red-500 transition-colors"
                                                title="Eliminar notificación"
                                            >
                                                <TrashIcon className="w-4 h-4" />
                                            </button>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        )}
                    </div>
                </div>
            )}
        </div>
    );
};
