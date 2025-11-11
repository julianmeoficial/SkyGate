import { useEffect, useState } from 'react';
import { useStore } from '../../store';
import StateIndicator from '../automata/StateIndicator';
import InputIndicator, { InputType } from '../automata/InputIndicator';
import type { Flight, AutomataState } from '../../types/backend.types';
import { CheckCircleIcon, ClockIcon, XCircleIcon } from '@heroicons/react/24/solid';
import clsx from 'clsx';

interface FlightProcessVisualizationProps {
    flight: Flight;
}

const PROCESS_STEPS: {
    state: AutomataState;
    label: string;
    description: string;
    input?: InputType;
}[] = [
    { state: 'S0', label: 'Inicio', description: 'Sistema inicializado' },
    { state: 'S1', label: 'Detección', description: 'Vuelo detectado', input: 'I1' },
    { state: 'S1', label: 'Confirmación', description: 'Tipo confirmado', input: 'I2' },
    { state: 'S4', label: 'Gate Asignado', description: 'Gate disponible (I3)', input: 'I3' },
    { state: 'S5', label: 'Estacionamiento', description: 'Aeronave en gate', input: 'I5' },
    { state: 'S0', label: 'Finalizado', description: 'Aeronave partió', input: 'I6' },
];

export default function FlightProcessVisualization({ flight }: FlightProcessVisualizationProps) {
    const currentState = useStore(state => state.getFlightState(flight.id)) || flight.automataState;
    const [activeStepIndex, setActiveStepIndex] = useState(0);

    useEffect(() => {
        const stateToIndex: Record<AutomataState, number> = {
            S0: 0,
            S1: 1,
            S2: 1,
            S3: 1,
            S4: 3,
            S5: 4,
            S6: 1,
        };
        setActiveStepIndex(stateToIndex[currentState] || 0);
    }, [currentState]);

    return (
        <div className="bg-gray-900 rounded-lg p-4 border border-gray-700">
            <div className="flex items-center justify-between mb-4">
                <h3 className="text-sm font-semibold text-white">Proceso: {flight.flightNumber}</h3>
                <StateIndicator state={currentState} animated />
            </div>

            {currentState === 'S6' && (
                <div className="mb-4 p-3 bg-red-500/10 border border-red-500/30 rounded-lg">
                    <div className="flex items-center gap-2">
                        <XCircleIcon className="w-5 h-5 text-red-400" />
                        <p className="text-sm text-red-400 font-semibold">Sin Gates Disponibles (I4)</p>
                    </div>
                    <p className="text-xs text-gray-400 mt-1">El vuelo está en espera</p>
                </div>
            )}

            <div className="relative">
                {PROCESS_STEPS.map((step, index) => {
                    const isActive = index === activeStepIndex;
                    const isCompleted = index < activeStepIndex;
                    const isCurrent = index === activeStepIndex;

                    return (
                        <div key={index} className="flex items-start gap-3 mb-4 last:mb-0">
                            <div className="relative flex flex-col items-center">
                                <div
                                    className={clsx(
                                        'w-8 h-8 rounded-full flex items-center justify-center border-2 transition-all duration-300',
                                        {
                                            'bg-green-500 border-green-500': isCompleted,
                                            'bg-blue-500 border-blue-500 animate-pulse': isCurrent,
                                            'bg-gray-700 border-gray-600': !isCompleted && !isCurrent,
                                        }
                                    )}
                                >
                                    {isCompleted ? (
                                        <CheckCircleIcon className="w-5 h-5 text-white" />
                                    ) : isCurrent ? (
                                        <ClockIcon className="w-5 h-5 text-white" />
                                    ) : (
                                        <XCircleIcon className="w-5 h-5 text-gray-500" />
                                    )}
                                </div>
                                {index < PROCESS_STEPS.length - 1 && (
                                    <div
                                        className={clsx('w-0.5 h-8 mt-1 transition-colors duration-300', {
                                            'bg-green-500': isCompleted,
                                            'bg-gray-700': !isCompleted,
                                        })}
                                    />
                                )}
                            </div>

                            <div className="flex-1">
                                <div className="flex items-center justify-between mb-1">
                                    <p
                                        className={clsx('text-sm font-medium transition-colors duration-300', {
                                            'text-white': isActive || isCompleted,
                                            'text-gray-500': !isActive && !isCompleted,
                                        })}
                                    >
                                        {step.label}
                                    </p>
                                    {step.input && (
                                        <InputIndicator
                                            input={step.input}
                                            isActive={isCurrent}
                                            showLabel={false}
                                            size="sm"
                                        />
                                    )}
                                </div>
                                <p className="text-xs text-gray-500">{step.description}</p>
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}
