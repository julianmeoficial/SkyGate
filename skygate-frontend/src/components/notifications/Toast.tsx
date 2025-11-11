import { useEffect } from 'react';
import { XMarkIcon, CheckCircleIcon, ExclamationCircleIcon, InformationCircleIcon, ExclamationTriangleIcon } from '@heroicons/react/24/outline';

interface ToastProps {
    id: string;
    type: 'success' | 'error' | 'warning' | 'info';
    message: string;
    onClose: () => void;
    autoClose?: boolean;
    duration?: number;
}

export const Toast = ({ id, type, message, onClose, autoClose = true, duration = 3000 }: ToastProps) => {
    useEffect(() => {
        if (autoClose) {
            const timer = setTimeout(() => {
                onClose();
            }, duration);

            return () => clearTimeout(timer);
        }
    }, [autoClose, duration, onClose]);

    const getToastStyles = () => {
        const styles = {
            success: {
                bg: 'bg-green-50 border-green-200',
                icon: <CheckCircleIcon className="w-6 h-6 text-green-500" />,
                text: 'text-green-800',
            },
            error: {
                bg: 'bg-red-50 border-red-200',
                icon: <ExclamationCircleIcon className="w-6 h-6 text-red-500" />,
                text: 'text-red-800',
            },
            warning: {
                bg: 'bg-yellow-50 border-yellow-200',
                icon: <ExclamationTriangleIcon className="w-6 h-6 text-yellow-500" />,
                text: 'text-yellow-800',
            },
            info: {
                bg: 'bg-blue-50 border-blue-200',
                icon: <InformationCircleIcon className="w-6 h-6 text-blue-500" />,
                text: 'text-blue-800',
            },
        };
        return styles[type];
    };

    const style = getToastStyles();

    return (
        <div
            className={`${style.bg} border rounded-lg shadow-lg p-4 min-w-80 backdrop-blur-sm bg-opacity-95 animate-slide-in-right`}
        >
            <div className="flex items-start gap-3">
                <div className="flex-shrink-0">{style.icon}</div>
                <div className="flex-1">
                    <p className={`text-sm font-medium ${style.text}`}>{message}</p>
                </div>
                <button
                    onClick={onClose}
                    className="flex-shrink-0 text-gray-400 hover:text-gray-600 transition-colors"
                    aria-label="Cerrar notificación"
                >
                    <XMarkIcon className="w-5 h-5" />
                </button>
            </div>
        </div>
    );
};
