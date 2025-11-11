import { create } from 'zustand';
import { createAutomataSlice, AutomataSlice } from './slices/automata.slice';
import { createNotificationSlice, NotificationSlice } from './slices/notification.slice';
import { createGateSlice, GateSlice } from './slices/gate.slice';

export type RootState = AutomataSlice & NotificationSlice & GateSlice;

export const useStore = create<RootState>()((...args) => ({
    ...createAutomataSlice(...args),
    ...createNotificationSlice(...args),
    ...createGateSlice(...args),
}));
