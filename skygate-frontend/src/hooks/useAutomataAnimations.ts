import { useEffect } from 'react';
import { useStore } from '../store';
import { gsap } from 'gsap';

export function useAutomataAnimations() {
    const transitionHistory = useStore(state => state.transitionHistory);

    useEffect(() => {
        if (transitionHistory.length > 0 && typeof window !== 'undefined' && window.gsap) {
            const lastTransition = transitionHistory[0];
            const timeline = window.gsap.timeline();

            timeline
                .to('.automata-container', {
                    backgroundColor: 'rgba(147, 51, 234, 0.15)',
                    duration: 0.3,
                    ease: 'power2.out',
                })
                .to('.automata-container', {
                    backgroundColor: 'rgba(17, 24, 39, 0.4)',
                    duration: 0.6,
                    ease: 'power2.in',
                });

            window.gsap.fromTo(
                '[data-transition-log-item="0"]',
                { x: -50, opacity: 0, scale: 0.9 },
                { x: 0, opacity: 1, scale: 1, duration: 0.5, ease: 'back.out(1.4)' }
            );
        }
    }, [transitionHistory]);
}
