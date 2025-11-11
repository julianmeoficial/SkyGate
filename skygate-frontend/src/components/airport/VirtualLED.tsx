import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import { GateStatus } from '../../types/backend.types';
import { ledStatusColors } from '../../utils/colors';
import clsx from 'clsx';

interface VirtualLEDProps {
    status: GateStatus;
    size?: 'sm' | 'md' | 'lg';
    animated?: boolean;
}

export default function VirtualLED({ status, size = 'md', animated = true }: VirtualLEDProps) {
    const ledRef = useRef<HTMLDivElement>(null);

    const sizeMap = {
        sm: 'w-3 h-3',
        md: 'w-4 h-4',
        lg: 'w-6 h-6',
    };

    useEffect(() => {
        if (animated && ledRef.current && status !== 'MAINTENANCE') {
            gsap.to(ledRef.current, {
                scale: 1.1,
                duration: 0.75,
                repeat: -1,
                yoyo: true,
                ease: 'sine.inOut',
            });
        }

        return () => {
            if (ledRef.current) {
                gsap.killTweensOf(ledRef.current);
            }
        };
    }, [status, animated]);

    return (
        <div
            ref={ledRef}
            className={clsx(
                'rounded-full transition-all duration-300',
                sizeMap[size],
                ledStatusColors[status],
                animated && status !== 'MAINTENANCE' && 'animate-pulse-led'
            )}
            title={status}
        />
    );
}
