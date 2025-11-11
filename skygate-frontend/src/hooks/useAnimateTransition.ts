import { useEffect, useRef } from 'react';
import { gsap } from 'gsap';
import type { AutomataState } from '../types/backend.types';

export function useAnimateTransition(fromState: AutomataState, toState: AutomataState) {
    const hasAnimated = useRef(false);

    useEffect(() => {
        if (!hasAnimated.current) {
            hasAnimated.current = true;

            const timeline = gsap.timeline();

            timeline
                .to(`[data-state="${fromState}"]`, {
                    scale: 1.2,
                    boxShadow: '0 0 20px rgba(59, 130, 246, 0.8)',
                    duration: 0.3,
                })
                .to(`[data-state="${fromState}"]`, {
                    scale: 1,
                    duration: 0.2,
                })
                .to(`[data-state="${toState}"]`, {
                    scale: 1.3,
                    boxShadow: '0 0 30px rgba(34, 197, 94, 0.8)',
                    duration: 0.4,
                })
                .to(`[data-state="${toState}"]`, {
                    scale: 1,
                    boxShadow: '0 0 10px rgba(34, 197, 94, 0.5)',
                    duration: 0.3,
                });
        }

        return () => {
            hasAnimated.current = false;
        };
    }, [fromState, toState]);
}
