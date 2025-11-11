import { useEffect, useRef } from 'react';
import { useStore } from '../../store';
import { gsap } from 'gsap';
import clsx from 'clsx';
import type { AutomataState } from '../../types/backend.types';

const STATES: AutomataState[] = ['S0', 'S1', 'S2', 'S3', 'S4', 'S5', 'S6'];

const STATE_INFO = {
    S0: { name: 'Inicializado', color: 'bg-automata-s0', description: 'Sistema listo' },
    S1: { name: 'Wide Body', color: 'bg-automata-s1', description: 'WB detectado' },
    S2: { name: 'Jumbo', color: 'bg-automata-s2', description: 'Jumbo detectado' },
    S3: { name: 'Narrow Body', color: 'bg-automata-s3', description: 'NB detectado' },
    S4: { name: 'Gate Asignado', color: 'bg-automata-s4', description: 'Gate OK' },
    S5: { name: 'Estacionado', color: 'bg-automata-s5', description: 'En plataforma' },
    S6: { name: 'Sin Gates', color: 'bg-automata-s6', description: 'Error' },
};

export default function AutomataTimeline() {
    const transitionHistory = useStore(state => state.transitionHistory);
    const stateRefs = useRef<Record<AutomataState, HTMLDivElement | null>>({} as any);
    const containerRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (containerRef.current && typeof window !== 'undefined' && window.gsap) {
            window.gsap.fromTo(
                containerRef.current.children,
                { opacity: 0, scale: 0.8, y: 20 },
                { opacity: 1, scale: 1, y: 0, duration: 0.5, stagger: 0.1, ease: 'back.out(1.3)' }
            );
        }
    }, []);

    useEffect(() => {
        if (transitionHistory.length > 0) {
            const lastTransition = transitionHistory[0];
            animateTransition(lastTransition.fromState, lastTransition.toState);
        }
    }, [transitionHistory]);

    const animateTransition = (fromState: AutomataState, toState: AutomataState) => {
        const fromElement = stateRefs.current[fromState];
        const toElement = stateRefs.current[toState];

        if (!fromElement || !toElement) return;

        const timeline = gsap.timeline();

        timeline
            .to(fromElement, {
                scale: 1.25,
                duration: 0.25,
                ease: 'power2.out',
            })
            .to(fromElement, {
                scale: 1,
                duration: 0.25,
                ease: 'power2.in',
            })
            .to(toElement, {
                scale: 1.35,
                duration: 0.3,
                ease: 'power2.out',
            }, '-=0.1')
            .to(toElement, {
                scale: 1,
                duration: 0.4,
                ease: 'elastic.out(1, 0.4)',
            });

        gsap.to(toElement, {
            boxShadow: '0 0 40px rgba(59, 130, 246, 0.8), 0 0 80px rgba(147, 51, 234, 0.4)',
            duration: 0.4,
            repeat: 4,
            yoyo: true,
        });
    };

    return (
        <div className="space-y-8">
            <div ref={containerRef} className="flex items-center justify-center gap-3 flex-wrap py-8">
                {STATES.map((state, index) => (
                    <div key={state} className="flex items-center gap-3">
                        <div className="relative group">
                            <div
                                ref={(el) => {
                                    if (el) {
                                        stateRefs.current[state] = el;
                                    }
                                }}
                                className={clsx(
                                    'w-20 h-20 rounded-2xl flex flex-col items-center justify-center text-white font-bold border-4 border-gray-700/50 transition-all duration-300 cursor-pointer relative overflow-hidden',
                                    'hover:scale-110 hover:border-gray-600 hover:shadow-2xl',
                                    STATE_INFO[state].color
                                )}
                            >
                                <div className="absolute inset-0 bg-gradient-to-br from-white/10 to-transparent pointer-events-none" />
                                <span className="text-lg font-bold relative z-10">{state}</span>
                                <span className="text-[9px] font-medium opacity-80 relative z-10 mt-0.5">{STATE_INFO[state].name.split(' ')[0]}</span>
                            </div>
                            <div className="absolute -bottom-16 left-1/2 transform -translate-x-1/2 opacity-0 group-hover:opacity-100 transition-all duration-300 pointer-events-none z-20">
                                <div className="bg-gray-900 border border-gray-700 rounded-lg px-3 py-2 shadow-2xl whitespace-nowrap">
                                    <p className="text-xs font-semibold text-white">{STATE_INFO[state].name}</p>
                                    <p className="text-[10px] text-gray-400 mt-0.5">{STATE_INFO[state].description}</p>
                                </div>
                            </div>
                        </div>
                        {index < STATES.length - 1 && state !== 'S5' && (
                            <svg className="w-6 h-6 text-gray-600 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M13 7l5 5m0 0l-5 5m5-5H6" />
                            </svg>
                        )}
                    </div>
                ))}
            </div>

            <div className="bg-gray-800/40 border border-gray-700/50 rounded-lg p-5">
                <h3 className="text-sm font-bold text-white mb-3 flex items-center gap-2">
                    <svg className="w-4 h-4 text-purple-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                    Flujo del Autómata
                </h3>
                <div className="space-y-2 text-xs text-gray-400">
                    <p>• <span className="text-purple-400 font-semibold">S0 → S1/S2/S3:</span> Detección de vuelo según tipo de aeronave</p>
                    <p>• <span className="text-blue-400 font-semibold">S1/S2/S3 → S4:</span> Asignación de gate disponible</p>
                    <p>• <span className="text-red-400 font-semibold">S1/S2/S3 → S6:</span> No hay gates disponibles (error)</p>
                    <p>• <span className="text-green-400 font-semibold">S4 → S5:</span> Aeronave llega y se estaciona</p>
                    <p>• <span className="text-gray-400 font-semibold">S5 → S0:</span> Aeronave parte y gate se libera</p>
                </div>
            </div>
        </div>
    );
}
