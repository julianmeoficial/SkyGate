import { axiosInstance } from '../axios.config';
import type { ApiResponse, Gate, GateStatus, Assignment } from '../../types/backend.types';

export const gateService = {
    getAll: async (activeOnly: boolean = true): Promise<ApiResponse<Gate[]>> => {
        const response = await axiosInstance.get<ApiResponse<Gate[]>>('/gates', {
            params: { activeOnly },
        });
        return response.data;
    },

    getById: async (id: number): Promise<ApiResponse<Gate>> => {
        const response = await axiosInstance.get<ApiResponse<Gate>>(`/gates/${id}`);
        return response.data;
    },

    getByGateNumber: async (gateNumber: string): Promise<ApiResponse<Gate>> => {
        const response = await axiosInstance.get<ApiResponse<Gate>>(`/gates/number/${gateNumber}`);
        return response.data;
    },

    getByTerminal: async (terminal: string): Promise<ApiResponse<Gate[]>> => {
        const response = await axiosInstance.get<ApiResponse<Gate[]>>(`/gates/terminal/${terminal}`);
        return response.data;
    },

    getByType: async (type: string): Promise<ApiResponse<Gate[]>> => {
        const response = await axiosInstance.get<ApiResponse<Gate[]>>(`/gates/type/${type}`);
        return response.data;
    },

    getByStatus: async (status: string): Promise<ApiResponse<Gate[]>> => {
        const response = await axiosInstance.get<ApiResponse<Gate[]>>(`/gates/status/${status}`);
        return response.data;
    },

    getAvailable: async (): Promise<ApiResponse<Gate[]>> => {
        const response = await axiosInstance.get<ApiResponse<Gate[]>>('/gates/available');
        return response.data;
    },

    updateStatus: async (id: number, status: GateStatus): Promise<ApiResponse<Gate>> => {
        const response = await axiosInstance.put<ApiResponse<Gate>>(`/gates/${id}/status`, { status });
        return response.data;
    },

    setMaintenance: async (id: number): Promise<ApiResponse<Gate>> => {
        const response = await axiosInstance.put<ApiResponse<Gate>>(`/gates/${id}/status`, {
            status: 'MAINTENANCE'
        });
        return response.data;
    },

    clearMaintenance: async (id: number): Promise<ApiResponse<Gate>> => {
        const response = await axiosInstance.put<ApiResponse<Gate>>(`/gates/${id}/status`, {
            status: 'FREE'
        });
        return response.data;
    },
};
