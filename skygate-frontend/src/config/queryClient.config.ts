import { QueryClient } from '@tanstack/react-query';

export const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            staleTime: 5000,
            gcTime: 600000,
            retry: 3,
            refetchOnWindowFocus: true,
            refetchOnReconnect: true,
            refetchOnMount: true,
        },
        mutations: {
            retry: 1,
        },
    },
});
