import { axiosInstance } from '../axios.config';
import type { ApiResponse, Flight, AircraftType } from '../../types/backend.types';

export interface DetectFlightRequest {
    flightNumber: string;
    aircraftType: AircraftType;
    origin: string;
    destination: string;
    airline?: string;
}

export const flightService = {
    detectFlight: async (data: DetectFlightRequest): Promise<ApiResponse<Flight>> => {
        const response = await axiosInstance.post<ApiResponse<Flight>>('/flights/detect', data);
        return response.data;
    },

    simulateFlight: async (): Promise<ApiResponse<Flight>> => {
        const response = await axiosInstance.post<ApiResponse<Flight>>('/flights/simulate');
        return response.data;
    },

    getAll: async (activeOnly: boolean = true): Promise<ApiResponse<Flight[]>> => {
        const response = await axiosInstance.get<ApiResponse<Flight[]>>('/flights', {
            params: { activeOnly },
        });
        return response.data;
    },

    getById: async (id: number): Promise<ApiResponse<Flight>> => {
        const response = await axiosInstance.get<ApiResponse<Flight>>(`/flights/${id}`);
        return response.data;
    },

    getByFlightNumber: async (flightNumber: string): Promise<ApiResponse<Flight>> => {
        const response = await axiosInstance.get<ApiResponse<Flight>>(`/flights/number/${flightNumber}`);
        return response.data;
    },

    getByAutomataState: async (state: string): Promise<ApiResponse<Flight[]>> => {
        const response = await axiosInstance.get<ApiResponse<Flight[]>>(`/flights/automata-state/${state}`);
        return response.data;
    },

    simulateArrival: async (id: number): Promise<ApiResponse<void>> => {
        const response = await axiosInstance.post<ApiResponse<void>>(`/flights/${id}/arrival`);
        return response.data;
    },

    simulateDeparture: async (id: number): Promise<ApiResponse<void>> => {
        const response = await axiosInstance.post<ApiResponse<void>>(`/flights/${id}/departure`);
        return response.data;
    },
};
