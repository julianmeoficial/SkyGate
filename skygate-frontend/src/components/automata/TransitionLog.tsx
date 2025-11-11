import { useEffect, useRef } from 'react';
import { formatDistanceToNow } from 'date-fns';
import { es } from 'date-fns/locale';
import { ArrowRightIcon } from '@heroicons/react/24/outline';
import InputIndicator, { InputType } from './InputIndicator';
import type { AutomataTransition } from '../../types/backend.types';
import clsx from 'clsx';

interface TransitionLogProps {
    transitions: AutomataTransition[];
}

export default function TransitionLog({ transitions }: TransitionLogProps) {
    const listRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (listRef.current && typeof window !== 'undefined' && window.gsap && transitions.length > 0) {
            const latestItem = listRef.current.querySelector('[data-transition-log-item="0"]');
            if (latestItem) {
                window.gsap.fromTo(
                    latestItem,
                    { opacity: 0, x: -30, scale: 0.95 },
                    { opacity: 1, x: 0, scale: 1, duration: 0.5, ease: 'back.out(1.4)' }
                );
            }
        }
    }, [transitions]);

    if (transitions.length === 0) {
        return (
            <div className="text-center py-16">
                <div className="w-20 h-20 mx-auto mb-4 rounded-full bg-gray-800/50 flex items-center justify-center">
                    <svg className="w-10 h-10 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-6 9l2 2 4-4" />
                    </svg>
                </div>
                <p className="text-lg font-semibold text-gray-400">No hay transiciones registradas</p>
                <p className="text-sm text-gray-500 mt-2">Las transiciones aparecerán aquí en tiempo real</p>
            </div>
        );
    }

    return (
        <div ref={listRef} className="space-y-3 max-h-[600px] overflow-y-auto custom-scrollbar">
            {transitions.map((transition, index) => (
                <div
                    key={`${transition.timestamp}-${index}`}
                    data-transition-log-item={index}
                    className="bg-gray-800/60 hover:bg-gray-800/80 border border-gray-700/50 hover:border-gray-600 rounded-lg p-4 transition-all duration-200 hover:shadow-lg group"
                >
                    <div className="flex items-start justify-between gap-4 mb-3">
                        <div className="flex items-center gap-2">
                            <div className="w-2 h-2 rounded-full bg-cyan-500 animate-pulse shadow-lg shadow-cyan-500/50" />
                            <span className="text-xs font-medium text-gray-400">
                                {formatDistanceToNow(new Date(transition.timestamp), {
                                    addSuffix: true,
                                    locale: es,
                                })}
                            </span>
                        </div>
                        <span className="text-xs font-bold text-white bg-gray-700/60 px-2 py-1 rounded">
                            {transition.flightNumber}
                        </span>
                    </div>

                    <div className="flex items-center gap-3">
                        <span className="px-2.5 py-1 text-xs font-bold bg-gray-700 text-white rounded border border-gray-600">
                            {transition.fromState}
                        </span>
                        <ArrowRightIcon className="w-5 h-5 text-purple-400 group-hover:scale-125 transition-transform duration-200" />
                        <span className="px-2.5 py-1 text-xs font-bold bg-gradient-to-r from-purple-600 to-blue-600 text-white rounded shadow-lg">
                            {transition.toState}
                        </span>
                        {transition.input && (
                            <div className="ml-auto">
                                <InputIndicator input={transition.input as InputType} />
                            </div>
                        )}
                    </div>

                    {transition.event && (
                        <div className="mt-3 pt-3 border-t border-gray-700/50">
                            <span className="text-xs text-gray-400">
                                Evento: <span className="text-gray-300 font-medium">{transition.event}</span>
                            </span>
                        </div>
                    )}
                </div>
            ))}
        </div>
    );
}
