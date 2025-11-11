import { useStore } from '../store';

export function useAutomata() {
    const currentStates = useStore(state => state.currentStates);
    const transitionHistory = useStore(state => state.transitionHistory);
    const setCurrentState = useStore(state => state.setCurrentState);
    const addTransition = useStore(state => state.addTransition);
    const clearHistory = useStore(state => state.clearHistory);
    const getFlightState = useStore(state => state.getFlightState);

    return {
        currentStates,
        transitionHistory,
        setCurrentState,
        addTransition,
        clearHistory,
        getFlightState,
    };
}
