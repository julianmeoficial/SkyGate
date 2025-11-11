import clsx from 'clsx';

export type InputType = 'I1' | 'I2' | 'I3' | 'I4' | 'I5' | 'I6';

interface InputIndicatorProps {
    input: InputType;
    isActive?: boolean;
    showLabel?: boolean;
    size?: 'sm' | 'md' | 'lg';
}

const INPUT_CONFIG: Record<InputType, { label: string; color: string; icon: string; description: string }> = {
    I1: {
        label: 'Detección',
        color: 'text-blue-400 bg-blue-500/10 border-blue-500/30',
        icon: '✈️',
        description: 'Vuelo detectado',
    },
    I2: {
        label: 'Confirmación',
        color: 'text-cyan-400 bg-cyan-500/10 border-cyan-500/30',
        icon: '✓',
        description: 'Tipo confirmado',
    },
    I3: {
        label: 'Gate OK',
        color: 'text-green-400 bg-green-500/10 border-green-500/30',
        icon: '✓',
        description: 'Gate disponible',
    },
    I4: {
        label: 'Sin Gates',
        color: 'text-red-400 bg-red-500/10 border-red-500/30',
        icon: '✗',
        description: 'No hay gates',
    },
    I5: {
        label: 'Arribo',
        color: 'text-yellow-400 bg-yellow-500/10 border-yellow-500/30',
        icon: '🛬',
        description: 'Aeronave llega',
    },
    I6: {
        label: 'Salida',
        color: 'text-purple-400 bg-purple-500/10 border-purple-500/30',
        icon: '🛫',
        description: 'Aeronave parte',
    },
};

const SIZE_CLASSES = {
    sm: 'px-2 py-1 text-xs',
    md: 'px-3 py-1.5 text-sm',
    lg: 'px-4 py-2 text-base',
};

export default function InputIndicator({
                                           input,
                                           isActive = false,
                                           showLabel = true,
                                           size = 'md',
                                       }: InputIndicatorProps) {
    const config = INPUT_CONFIG[input];

    return (
        <div
            className={clsx(
                'inline-flex items-center gap-2 border-2 rounded-lg font-semibold transition-all duration-300',
                config.color,
                SIZE_CLASSES[size],
                isActive ? 'animate-pulse shadow-lg scale-110' : 'opacity-60'
            )}
            title={config.description}
        >
            <span className="text-lg">{config.icon}</span>
            <span className="font-mono">{input}</span>
            {showLabel && <span className="hidden sm:inline">- {config.label}</span>}
        </div>
    );
}
