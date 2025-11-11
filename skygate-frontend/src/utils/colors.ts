import type { AutomataState, GateStatus } from '../types/backend.types';

export const automataStateColors: Record<AutomataState, string> = {
    S0: 'text-automata-s0',
    S1: 'text-automata-s1',
    S2: 'text-automata-s2',
    S3: 'text-automata-s3',
    S4: 'text-automata-s4',
    S5: 'text-automata-s5',
    S6: 'text-automata-s6',
};

export const automataStateBgColors: Record<AutomataState, string> = {
    S0: 'bg-automata-s0',
    S1: 'bg-automata-s1',
    S2: 'bg-automata-s2',
    S3: 'bg-automata-s3',
    S4: 'bg-automata-s4',
    S5: 'bg-automata-s5',
    S6: 'bg-automata-s6',
};

export const automataStateGlowColors: Record<AutomataState, string> = {
    S0: 'shadow-glow-s0',
    S1: 'shadow-glow-s1',
    S2: 'shadow-glow-s2',
    S3: 'shadow-glow-s3',
    S4: 'shadow-glow-s4',
    S5: 'shadow-glow-s5',
    S6: 'shadow-glow-s6',
};

export const gateStatusColors: Record<GateStatus, string> = {
    FREE: 'text-gate-free',
    ASSIGNED: 'text-gate-assigned',
    OCCUPIED: 'text-gate-occupied',
    MAINTENANCE: 'text-gate-maintenance',
    RESERVED: 'text-blue-400',
};

export const gateStatusBgColors: Record<GateStatus, string> = {
    FREE: 'bg-gate-free',
    ASSIGNED: 'bg-gate-assigned',
    OCCUPIED: 'bg-gate-occupied',
    MAINTENANCE: 'bg-gate-maintenance',
    RESERVED: 'bg-blue-500',
};

export const ledStatusColors: Record<GateStatus, string> = {
    FREE: 'bg-led-green shadow-led-green',
    ASSIGNED: 'bg-led-yellow shadow-led-yellow',
    OCCUPIED: 'bg-led-red shadow-led-red',
    MAINTENANCE: 'bg-gray-500',
    RESERVED: 'bg-led-blue shadow-led-blue',
};
