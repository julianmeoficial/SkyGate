import { useState, useEffect, useRef } from 'react';
import { useStore } from '../../store';
import { useAutomataAnimations } from '../../hooks/useAutomataAnimations';
import AutomataTimeline from '../../components/automata/AutomataTimeline';
import TransitionLog from '../../components/automata/TransitionLog';
import StateIndicator from '../../components/automata/StateIndicator';
import clsx from 'clsx';

type TabType = 'timeline' | 'transitions' | 'states';

export default function AutomataView() {
    const [activeTab, setActiveTab] = useState<TabType>('timeline');
    const transitionHistory = useStore(state => state.transitionHistory);
    const currentStates = useStore(state => state.currentStates);
    const headerRef = useRef<HTMLDivElement>(null);
    const tabsRef = useRef<HTMLDivElement>(null);
    const contentRef = useRef<HTMLDivElement>(null);

    useAutomataAnimations();

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap) {
            if (headerRef.current) {
                window.gsap.fromTo(
                    headerRef.current,
                    { opacity: 0, y: -20 },
                    { opacity: 1, y: 0, duration: 0.5, ease: 'power2.out' }
                );
            }
            if (tabsRef.current) {
                window.gsap.fromTo(
                    tabsRef.current.children,
                    { opacity: 0, y: 10 },
                    { opacity: 1, y: 0, duration: 0.3, stagger: 0.1, ease: 'power2.out', delay: 0.2 }
                );
            }
        }
    }, []);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && contentRef.current) {
            window.gsap.fromTo(
                contentRef.current,
                { opacity: 0, x: 20 },
                { opacity: 1, x: 0, duration: 0.4, ease: 'power2.out' }
            );
        }
    }, [activeTab]);

    const tabs = [
        { id: 'timeline' as TabType, label: 'Timeline', count: null },
        { id: 'transitions' as TabType, label: 'Transiciones', count: transitionHistory.length },
        { id: 'states' as TabType, label: 'Estados Actuales', count: currentStates.size },
    ];

    return (
        <div className="p-8 space-y-6">
            <div ref={headerRef} className="flex items-start justify-between">
                <div>
                    <h1 className="text-3xl font-bold text-white mb-2">Visualización del Autómata</h1>
                    <p className="text-gray-400">Estados y transiciones del AFD en tiempo real</p>
                </div>
                <div className="flex items-center gap-3 px-4 py-2 bg-cyan-500/10 rounded-lg border border-cyan-500/30">
                    <div className="w-2 h-2 rounded-full bg-cyan-500 animate-pulse" />
                    <span className="text-sm font-semibold text-white">Sistema Activo</span>
                </div>
            </div>

            <div ref={tabsRef} className="flex border-b border-gray-800">
                {tabs.map((tab) => (
                    <button
                        key={tab.id}
                        onClick={() => setActiveTab(tab.id)}
                        className={clsx(
                            'relative flex-1 px-6 py-4 text-sm font-semibold transition-all duration-300',
                            activeTab === tab.id
                                ? 'text-white border-b-2 border-purple-500'
                                : 'text-gray-400 hover:text-white'
                        )}
                    >
                        {activeTab === tab.id && (
                            <span className="absolute inset-0 bg-purple-500/5" />
                        )}
                        <span className="relative flex items-center justify-center gap-2">
                            {tab.label}
                            {tab.count !== null && (
                                <span className="px-2 py-0.5 text-xs font-bold bg-gray-800 text-white rounded-full border border-gray-700">
                                    {tab.count}
                                </span>
                            )}
                        </span>
                    </button>
                ))}
            </div>

            <div ref={contentRef} className="automata-container">
                {activeTab === 'timeline' && (
                    <div>
                        <h2 className="text-xl font-semibold text-white mb-6">Timeline de Estados</h2>
                        <AutomataTimeline />
                    </div>
                )}

                {activeTab === 'transitions' && (
                    <div>
                        <div className="flex items-center justify-between mb-6">
                            <h2 className="text-xl font-semibold text-white">Log de Transiciones</h2>
                            <span className="text-sm text-gray-400">({transitionHistory.length} registros)</span>
                        </div>
                        <TransitionLog transitions={transitionHistory} />
                    </div>
                )}

                {activeTab === 'states' && (
                    <div>
                        <h2 className="text-xl font-semibold text-white mb-6">Estados Actuales por Vuelo</h2>
                        <div className="space-y-3">
                            {Array.from(currentStates.entries()).map(([flightId, state]) => (
                                <div
                                    key={flightId}
                                    className="bg-gray-800/40 rounded-lg p-4 border border-gray-700/50 hover:border-gray-600 transition-all duration-200"
                                >
                                    <div className="flex items-center justify-between">
                                        <span className="text-sm text-gray-300">
                                            Vuelo ID: <span className="text-white font-semibold">{flightId}</span>
                                        </span>
                                        <StateIndicator state={state} />
                                    </div>
                                </div>
                            ))}
                            {currentStates.size === 0 && (
                                <div className="text-center py-16">
                                    <div className="w-16 h-16 mx-auto mb-4 rounded-full bg-gray-800/30 flex items-center justify-center">
                                        <svg className="w-8 h-8 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4" />
                                        </svg>
                                    </div>
                                    <p className="text-gray-400 font-medium">No hay vuelos activos en el sistema</p>
                                    <p className="text-sm text-gray-500 mt-1">Los vuelos aparecerán aquí cuando sean detectados</p>
                                </div>
                            )}
                        </div>
                    </div>
                )}
            </div>
        </div>
    );
}
