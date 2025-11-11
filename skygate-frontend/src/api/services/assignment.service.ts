import { axiosInstance } from '../axios.config';
import type { ApiResponse, Assignment } from '../../types/backend.types';

export const assignmentService = {
    getAll: async (activeOnly: boolean = true): Promise<ApiResponse<Assignment[]>> => {
        const response = await axiosInstance.get<ApiResponse<Assignment[]>>('/assignments', {
            params: { activeOnly },
        });
        return response.data;
    },

    getById: async (id: number): Promise<ApiResponse<Assignment>> => {
        const response = await axiosInstance.get<ApiResponse<Assignment>>(`/assignments/${id}`);
        return response.data;
    },

    getByFlight: async (flightId: number): Promise<ApiResponse<Assignment[]>> => {
        const response = await axiosInstance.get<ApiResponse<Assignment[]>>(`/assignments/flight/${flightId}`);
        return response.data;
    },

    getByGate: async (gateId: number): Promise<ApiResponse<Assignment[]>> => {
        const response = await axiosInstance.get<ApiResponse<Assignment[]>>(`/assignments/gate/${gateId}`);
        return response.data;
    },

    getActive: async (): Promise<ApiResponse<Assignment[]>> => {
        const response = await axiosInstance.get<ApiResponse<Assignment[]>>('/assignments/active');
        return response.data;
    },

    delete: async (id: number): Promise<ApiResponse<void>> => {
        const response = await axiosInstance.delete<ApiResponse<void>>(`/assignments/${id}`);
        return response.data;
    },
};
