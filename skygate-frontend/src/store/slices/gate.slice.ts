import { StateCreator } from 'zustand';

export interface GateSlice {
    terminalFilter: string | null;
    setTerminalFilter: (terminal: string | null) => void;
}

export const createGateSlice: StateCreator<GateSlice> = (set) => ({
    terminalFilter: null,

    setTerminalFilter: (terminal) => {
        set({ terminalFilter: terminal });
    },
});
