import { useEffect, useState } from 'react';
import clsx from 'clsx';

type DisplayType = 'info' | 'success' | 'warning' | 'error';

interface VirtualDisplayProps {
    message: string;
    subMessage?: string;
    type?: DisplayType;
    flightNumber?: string;
    showClock?: boolean;
    animated?: boolean;
}

export default function VirtualDisplay({
                                           message,
                                           subMessage,
                                           type = 'info',
                                           flightNumber,
                                           showClock = true,
                                           animated = true,
                                       }: VirtualDisplayProps) {
    const [currentTime, setCurrentTime] = useState(new Date());

    useEffect(() => {
        if (showClock) {
            const timer = setInterval(() => {
                setCurrentTime(new Date());
            }, 1000);
            return () => clearInterval(timer);
        }
    }, [showClock]);

    const typeStyles = {
        info: 'bg-blue-500/5 border-blue-500/30',
        success: 'bg-green-500/5 border-green-500/30',
        warning: 'bg-yellow-500/5 border-yellow-500/30',
        error: 'bg-red-500/5 border-red-500/30',
    };

    const ledColors = {
        info: 'bg-blue-400',
        success: 'bg-green-400',
        warning: 'bg-yellow-400',
        error: 'bg-red-400',
    };

    const textColors = {
        info: 'text-blue-400',
        success: 'text-green-400',
        warning: 'text-yellow-400',
        error: 'text-red-400',
    };

    return (
        <div
            className={clsx(
                'relative border-2 rounded-xl p-6 shadow-2xl transition-all duration-500',
                typeStyles[type],
                animated && 'animate-fade-in'
            )}
            style={{
                fontFamily: "'Courier New', monospace",
                background: 'linear-gradient(135deg, rgba(15, 23, 42, 0.9) 0%, rgba(30, 41, 59, 0.9) 100%)',
            }}
        >
            <div className="absolute top-3 right-3 flex items-center gap-2">
                <div className={clsx('w-3 h-3 rounded-full animate-pulse', ledColors[type])} />
                {showClock && (
                    <span className="text-xs text-gray-400 font-mono">
            {currentTime.toLocaleTimeString('es-ES')}
          </span>
                )}
            </div>

            {flightNumber && (
                <div className="mb-3 pb-3 border-b border-gray-700">
                    <span className="text-xs text-gray-500 uppercase tracking-wider">Vuelo</span>
                    <p className="text-sm font-bold text-white font-mono">{flightNumber}</p>
                </div>
            )}

            <div className="text-center">
                <p
                    className={clsx(
                        'text-3xl font-bold tracking-wider mb-2 transition-all duration-300',
                        textColors[type],
                        animated && 'animate-pulse'
                    )}
                    style={{
                        textShadow: `0 0 20px currentColor`,
                        letterSpacing: '0.1em',
                    }}
                >
                    {message}
                </p>

                {subMessage && (
                    <p className="text-sm text-gray-400 mt-2 font-mono">{subMessage}</p>
                )}
            </div>

            <div className="absolute bottom-2 left-2 right-2 h-1 bg-gradient-to-r from-transparent via-gray-600 to-transparent opacity-30" />
        </div>
    );
}
