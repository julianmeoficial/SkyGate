import { useQuery } from '@tanstack/react-query';
import { gateService } from '../api/services/gate.service';
import { useStore } from '../store';
import type { Gate } from '../types/backend.types';

export function useGates(activeOnly: boolean = true) {
    const terminalFilter = useStore(state => state.terminalFilter);

    const gatesQuery = useQuery({
        queryKey: ['gates', { activeOnly, terminal: terminalFilter }],
        queryFn: async () => {
            if (terminalFilter) {
                return await gateService.getByTerminal(terminalFilter);
            }
            return await gateService.getAll(activeOnly);
        },
        refetchInterval: 5000,
    });

    const gates = gatesQuery.data?.data || [];

    const gatesByTerminal = gates.reduce((acc, gate) => {
        if (!acc[gate.terminal]) {
            acc[gate.terminal] = [];
        }
        acc[gate.terminal].push(gate);
        return acc;
    }, {} as Record<string, Gate[]>);

    return {
        gates,
        gatesByTerminal,
        isLoading: gatesQuery.isLoading,
        isError: gatesQuery.isError,
        error: gatesQuery.error,
    };
}
