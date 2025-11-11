import axios, { AxiosError, AxiosResponse, InternalAxiosRequestConfig } from 'axios';
import { ENV } from '../config/env.config';

export const axiosInstance = axios.create({
    baseURL: ENV.API_URL,
    timeout: ENV.API_TIMEOUT,
    headers: {
        'Content-Type': 'application/json',
    },
});

axiosInstance.interceptors.request.use(
    (config: InternalAxiosRequestConfig) => {
        const token = localStorage.getItem('skygate_token');
        if (token && config.headers) {
            config.headers.Authorization = `Bearer ${token}`;
        }

        if (ENV.IS_DEV) {
            console.log(`[API Request] ${config.method?.toUpperCase()} ${config.url}`);
        }

        return config;
    },
    (error: AxiosError) => {
        console.error('[API Request Error]', error);
        return Promise.reject(error);
    }
);

axiosInstance.interceptors.response.use(
    (response: AxiosResponse) => {
        if (ENV.IS_DEV) {
            console.log(`[API Response] ${response.config.url}`, response.data);
        }
        return response;
    },
    (error: AxiosError) => {
        if (error.response) {
            const status = error.response.status;

            switch (status) {
                case 401:
                    console.error('[API] Unauthorized - Token inválido o expirado');
                    localStorage.removeItem('skygate_token');
                    window.location.href = '/login';
                    break;
                case 403:
                    console.error('[API] Forbidden - No tienes permisos');
                    break;
                case 404:
                    console.error('[API] Not Found - Recurso no encontrado');
                    break;
                case 409:
                    console.error('[API] Conflict - Conflicto en la operación');
                    break;
                case 500:
                    console.error('[API] Internal Server Error');
                    break;
                default:
                    console.error(`[API] Error ${status}:`, error.response.data);
            }
        } else if (error.request) {
            console.error('[API] No response from server - Backend desconectado');
        } else {
            console.error('[API] Error:', error.message);
        }

        return Promise.reject(error);
    }
);

export default axiosInstance;
