import { StateCreator } from 'zustand';
import type { AutomataTransition, AutomataState } from '../../types/backend.types';

export interface AutomataSlice {
    currentStates: Map<number, AutomataState>;
    transitionHistory: AutomataTransition[];

    setCurrentState: (flightId: number, state: AutomataState) => void;
    addTransition: (transition: AutomataTransition) => void;
    clearHistory: () => void;
    getFlightState: (flightId: number) => AutomataState | undefined;
}

export const createAutomataSlice: StateCreator<AutomataSlice> = (set, get) => ({
    currentStates: new Map(),
    transitionHistory: [],

    setCurrentState: (flightId, state) => {
        set((prev) => {
            const newStates = new Map(prev.currentStates);
            newStates.set(flightId, state);
            return { currentStates: newStates };
        });
    },

    addTransition: (transition) => {
        set((prev) => {
            const newHistory = [transition, ...prev.transitionHistory].slice(0, 50);

            const newStates = new Map(prev.currentStates);
            newStates.set(transition.flightId, transition.toState);

            return {
                transitionHistory: newHistory,
                currentStates: newStates,
            };
        });
    },

    clearHistory: () => {
        set({ transitionHistory: [] });
    },

    getFlightState: (flightId) => {
        return get().currentStates.get(flightId);
    },
});
