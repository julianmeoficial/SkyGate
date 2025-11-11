interface ImportMetaEnv {
    readonly VITE_API_URL?: string;
    readonly VITE_WS_URL?: string;
    readonly VITE_API_TIMEOUT?: string;
    readonly DEV: boolean;
    readonly PROD: boolean;
}

interface ImportMeta {
    readonly env: ImportMetaEnv;
}

export const ENV = {
    API_URL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api/v1',
    WS_URL: import.meta.env.VITE_WS_URL || 'http://localhost:8080/ws',
    API_TIMEOUT: parseInt(import.meta.env.VITE_API_TIMEOUT || '10000'),
    IS_DEV: import.meta.env.DEV,
    IS_PROD: import.meta.env.PROD,
} as const;
