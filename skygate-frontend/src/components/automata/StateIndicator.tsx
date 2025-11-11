import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import clsx from 'clsx';
import type { AutomataState } from '../../types/backend.types';

interface StateIndicatorProps {
    state: AutomataState;
    label?: string;
    size?: 'sm' | 'md' | 'lg';
    animated?: boolean;
}

const stateColors: Record<AutomataState, string> = {
    S0: 'bg-automata-s0 text-white',
    S1: 'bg-automata-s1 text-white',
    S2: 'bg-automata-s2 text-white',
    S3: 'bg-automata-s3 text-white',
    S4: 'bg-automata-s4 text-white',
    S5: 'bg-automata-s5 text-white',
    S6: 'bg-automata-s6 text-white',
};

const sizeClasses = {
    sm: 'px-2 py-1 text-xs',
    md: 'px-3 py-1.5 text-sm',
    lg: 'px-4 py-2 text-base',
};

export default function StateIndicator({
                                           state,
                                           label,
                                           size = 'md',
                                           animated = true,
                                       }: StateIndicatorProps) {
    const badgeRef = useRef<HTMLSpanElement>(null);

    useEffect(() => {
        if (animated && badgeRef.current && typeof window !== 'undefined' && window.gsap) {
            window.gsap.fromTo(
                badgeRef.current,
                { scale: 0.7, opacity: 0, rotation: -10 },
                { scale: 1, opacity: 1, rotation: 0, duration: 0.4, ease: 'back.out(1.7)' }
            );

            if (state === 'S4' || state === 'S5') {
                window.gsap.to(badgeRef.current, {
                    boxShadow: '0 0 20px rgba(34, 197, 94, 0.7), 0 0 40px rgba(34, 197, 94, 0.3)',
                    duration: 0.6,
                    repeat: -1,
                    yoyo: true,
                });
            }
        }
    }, [state, animated]);

    return (
        <span
            ref={badgeRef}
            className={clsx(
                'inline-flex items-center gap-2 font-bold rounded-lg transition-all duration-300 border-2 border-current/30 shadow-lg',
                stateColors[state],
                sizeClasses[size]
            )}
        >
            <span className="relative flex items-center">
                <span className="absolute w-2 h-2 bg-white/40 rounded-full animate-ping" />
                <span className="relative w-2 h-2 bg-white rounded-full" />
            </span>
            {state}
            {label && <span className="opacity-90">{label}</span>}
        </span>
    );
}
