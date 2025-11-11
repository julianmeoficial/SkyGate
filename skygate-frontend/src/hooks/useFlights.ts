import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { flightService, DetectFlightRequest } from '../api/services/flight.service';
import { useStore } from '../store';

export function useFlights(activeOnly: boolean = true) {
    const queryClient = useQueryClient();
    const addDropdownNotification = useStore(state => state.addDropdownNotification);

    const flightsQuery = useQuery({
        queryKey: ['flights', { activeOnly }],
        queryFn: () => flightService.getAll(activeOnly),
        refetchInterval: 5000,
    });

    const detectMutation = useMutation({
        mutationFn: (data: DetectFlightRequest) => flightService.detectFlight(data),
        onSuccess: (response) => {
            queryClient.invalidateQueries({ queryKey: ['flights'] });

            addDropdownNotification({
                type: 'success',
                message: `Vuelo ${response.data.flightNumber} detectado exitosamente`,
                timestamp: Date.now(),
                read: false,
            });
        },
        onError: (error: any) => {
            addDropdownNotification({
                type: 'error',
                message: error.response?.data?.message || 'Error al detectar vuelo',
                timestamp: Date.now(),
                read: false,
            });
        },
    });

    const simulateFlightMutation = useMutation({
        mutationFn: () => flightService.simulateFlight(),
        onSuccess: (response) => {
            queryClient.invalidateQueries({ queryKey: ['flights'] });

            addDropdownNotification({
                type: 'success',
                message: `Vuelo ${response.data.flightNumber} simulado exitosamente`,
                timestamp: Date.now(),
                read: false,
            });
        },
    });

    const arrivalMutation = useMutation({
        mutationFn: (id: number) => flightService.simulateArrival(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['flights'] });

            addDropdownNotification({
                type: 'info',
                message: 'Vuelo ha llegado al gate',
                timestamp: Date.now(),
                read: false,
            });
        },
    });

    const departureMutation = useMutation({
        mutationFn: (id: number) => flightService.simulateDeparture(id),
        onSuccess: () => {
            queryClient.invalidateQueries({ queryKey: ['flights'] });

            addDropdownNotification({
                type: 'info',
                message: 'Vuelo ha partido - Gate liberado',
                timestamp: Date.now(),
                read: false,
            });
        },
    });

    return {
        flights: flightsQuery.data?.data || [],
        isLoading: flightsQuery.isLoading,
        isError: flightsQuery.isError,
        error: flightsQuery.error,

        detectFlight: detectMutation.mutate,
        isDetecting: detectMutation.isPending,

        simulateFlight: simulateFlightMutation.mutate,
        isSimulatingFlight: simulateFlightMutation.isPending,

        simulateArrival: arrivalMutation.mutate,
        isSimulatingArrival: arrivalMutation.isPending,

        simulateDeparture: departureMutation.mutate,
        isSimulatingDeparture: departureMutation.isPending,
    };
}
