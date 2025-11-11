import { axiosInstance } from '../axios.config';
import type { ApiResponse, DashboardStats } from '../../types/backend.types';

export const monitoringService = {
    getDashboardStats: async (): Promise<ApiResponse<DashboardStats>> => {
        const response = await axiosInstance.get<ApiResponse<DashboardStats>>('/monitoring/dashboard-stats');
        return response.data;
    },

    getSystemHealth: async (): Promise<ApiResponse<any>> => {
        const response = await axiosInstance.get<ApiResponse<any>>('/monitoring/health');
        return response.data;
    },
};
