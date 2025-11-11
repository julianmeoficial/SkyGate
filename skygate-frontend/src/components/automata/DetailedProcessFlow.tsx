import { useEffect, useRef } from 'react';
import { useStore } from '../../store';
import InputIndicator, { InputType } from './InputIndicator';
import StateIndicator from './StateIndicator';
import { CheckCircleIcon, XCircleIcon, ClockIcon } from '@heroicons/react/24/solid';
import type { Flight, AutomataState } from '../../types/backend.types';
import clsx from 'clsx';

interface DetailedProcessFlowProps {
    flight: Flight;
}

interface ProcessStep {
    state: AutomataState;
    label: string;
    description: string;
    input?: InputType;
    outputs?: string[];
    isBifurcation?: boolean;
    bifurcationPaths?: {
        success: { input: InputType; state: AutomataState; label: string };
        failure: { input: InputType; state: AutomataState; label: string };
    };
}

const PROCESS_STEPS: ProcessStep[] = [
    {
        state: 'S0',
        label: 'Sistema Listo',
        description: 'Sistema inicializado y esperando',
        outputs: ['O1: Sistema OK'],
    },
    {
        state: 'S1',
        label: 'Vuelo Detectado',
        description: 'Wide Body / Jumbo / Narrow Body',
        input: 'I1',
        outputs: ['O2: Tipo detectado'],
    },
    {
        state: 'S1',
        label: 'Confirmación',
        description: 'Tipo de aeronave confirmado',
        input: 'I2',
        outputs: ['O2: Confirmado'],
    },
    {
        state: 'S1',
        label: 'Decisión de Gate',
        description: 'Verificación de disponibilidad',
        isBifurcation: true,
        bifurcationPaths: {
            success: { input: 'I3', state: 'S4', label: 'Gate Disponible' },
            failure: { input: 'I4', state: 'S6', label: 'Sin Gates' },
        },
    },
    {
        state: 'S4',
        label: 'Gate Asignado',
        description: 'Gate asignado exitosamente',
        outputs: ['O3: Gate #', 'O5: LED Verde'],
    },
    {
        state: 'S5',
        label: 'Estacionado',
        description: 'Aeronave en plataforma',
        input: 'I5',
        outputs: ['O5: LED activo'],
    },
    {
        state: 'S0',
        label: 'Finalizado',
        description: 'Aeronave partió, gate liberado',
        input: 'I6',
        outputs: ['O1: Sistema listo', 'O5: LED apagado'],
    },
];

export default function DetailedProcessFlow({ flight }: DetailedProcessFlowProps) {
    const currentState = useStore(state => state.getFlightState(flight.id)) || flight.automataState;
    const stepsRef = useRef<HTMLDivElement>(null);

    useEffect(() => {
        if (typeof window !== 'undefined' && window.gsap && stepsRef.current) {
            window.gsap.fromTo(
                stepsRef.current.children,
                { opacity: 0, x: -30 },
                { opacity: 1, x: 0, duration: 0.4, stagger: 0.1, ease: 'power2.out' }
            );
        }
    }, [flight.id]);

    const getStepStatus = (step: ProcessStep, index: number): 'completed' | 'current' | 'pending' => {
        const stateOrder: Record<AutomataState, number> = {
            S0: 0,
            S1: 1,
            S2: 1,
            S3: 1,
            S4: 3,
            S5: 4,
            S6: 3,
        };

        const currentOrder = stateOrder[currentState];
        const stepOrder = stateOrder[step.state];

        if (step.isBifurcation) {
            if (currentState === 'S4' || currentState === 'S6') return 'completed';
            if (currentState === 'S1' || currentState === 'S2' || currentState === 'S3') return 'current';
            return 'pending';
        }

        if (currentState === 'S6' && step.state === 'S4') return 'pending';
        if (currentState === 'S4' && step.state === 'S6') return 'pending';

        if (stepOrder < currentOrder) return 'completed';
        if (stepOrder === currentOrder) return 'current';
        return 'pending';
    };

    return (
        <div className="space-y-6 mt-8">
            <div className="flex items-center justify-between">
                <div>
                    <h2 className="text-2xl font-bold text-white">Proceso Completo del Autómata</h2>
                    <p className="text-gray-400 mt-1">
                        Vuelo: <span className="text-white font-semibold">{flight.flightNumber}</span>
                    </p>
                </div>
                <StateIndicator state={currentState} size="lg" />
            </div>

            <div ref={stepsRef} className="space-y-4">
                {PROCESS_STEPS.map((step, index) => {
                    const status = getStepStatus(step, index);

                    if (step.isBifurcation) {
                        return (
                            <div
                                key={`bifurcation-${index}`}
                                className="relative bg-yellow-500/5 border-2 border-yellow-500/30 rounded-lg p-6"
                            >
                                <div className="absolute -top-3 left-6 px-3 py-1 bg-yellow-500 text-gray-900 text-xs font-bold rounded-full flex items-center gap-1">
                                    <svg className="w-4 h-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13 10V3L4 14h7v7l9-11h-7z" />
                                    </svg>
                                    BIFURCACIÓN
                                </div>
                                <p className="text-sm text-gray-300 font-medium mb-4 mt-2">{step.description}</p>
                                <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                    <div className="bg-green-500/5 border-2 border-green-500/30 rounded-lg p-4">
                                        <div className="flex items-center gap-2 mb-2">
                                            <CheckCircleIcon className="w-5 h-5 text-green-400" />
                                            <span className="text-xs font-bold text-green-400 uppercase">Éxito</span>
                                        </div>
                                        <InputIndicator input={step.bifurcationPaths!.success.input} />
                                        <p className="text-sm text-gray-300 font-semibold mt-2">
                                            → {step.bifurcationPaths!.success.label}
                                        </p>
                                    </div>
                                    <div className="bg-red-500/5 border-2 border-red-500/30 rounded-lg p-4">
                                        <div className="flex items-center gap-2 mb-2">
                                            <XCircleIcon className="w-5 h-5 text-red-400" />
                                            <span className="text-xs font-bold text-red-400 uppercase">Error</span>
                                        </div>
                                        <InputIndicator input={step.bifurcationPaths!.failure.input} />
                                        <p className="text-sm text-gray-300 font-semibold mt-2">
                                            → {step.bifurcationPaths!.failure.label}
                                        </p>
                                    </div>
                                </div>
                            </div>
                        );
                    }

                    return (
                        <div key={`step-${index}`} className="relative">
                            <div
                                className={clsx(
                                    'relative border-2 rounded-lg p-5 transition-all duration-300',
                                    status === 'completed' && 'bg-green-500/5 border-green-500/40',
                                    status === 'current' && 'bg-blue-500/10 border-blue-500 scale-105',
                                    status === 'pending' && 'bg-gray-800/20 border-gray-700/50'
                                )}
                            >
                                <div className="flex items-start gap-4">
                                    <div className="flex-shrink-0">
                                        {status === 'completed' && (
                                            <CheckCircleIcon className="w-8 h-8 text-green-400" />
                                        )}
                                        {status === 'current' && (
                                            <div className="relative">
                                                <div className="absolute w-8 h-8 bg-blue-500/30 rounded-full animate-ping" />
                                                <ClockIcon className="relative w-8 h-8 text-blue-400" />
                                            </div>
                                        )}
                                        {status === 'pending' && (
                                            <div className="w-8 h-8 rounded-full bg-gray-700 flex items-center justify-center">
                                                <span className="text-gray-500 text-sm font-bold">•</span>
                                            </div>
                                        )}
                                    </div>

                                    <div className="flex-1 space-y-3">
                                        <div className="flex items-start justify-between gap-4">
                                            <div className="flex-1">
                                                <h3 className={clsx(
                                                    'text-lg font-bold mb-1',
                                                    status === 'completed' && 'text-green-400',
                                                    status === 'current' && 'text-blue-400',
                                                    status === 'pending' && 'text-gray-400'
                                                )}>
                                                    {step.label}
                                                </h3>
                                                <p className="text-sm text-gray-400">{step.description}</p>
                                            </div>
                                            {step.input && (
                                                <InputIndicator input={step.input} />
                                            )}
                                        </div>

                                        {step.outputs && (
                                            <div className="flex flex-wrap gap-2">
                                                {step.outputs.map((output, i) => (
                                                    <span
                                                        key={i}
                                                        className="px-2.5 py-1 text-xs font-medium bg-gray-800/50 text-gray-300 rounded border border-gray-700"
                                                    >
                                                        {output}
                                                    </span>
                                                ))}
                                            </div>
                                        )}
                                    </div>
                                </div>
                            </div>

                            {index < PROCESS_STEPS.length - 1 && !PROCESS_STEPS[index + 1].isBifurcation && (
                                <div className="flex justify-center py-2">
                                    <svg className="w-6 h-6 text-gray-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2.5} d="M19 14l-7 7m0 0l-7-7m7 7V3" />
                                    </svg>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
