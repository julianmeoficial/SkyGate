import SockJS from 'sockjs-client';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { ENV } from '../../config/env.config';

export type ConnectionStatus = 'CONNECTING' | 'CONNECTED' | 'DISCONNECTED' | 'ERROR';

export interface WebSocketConfig {
    url: string;
    reconnectDelay: number;
    heartbeatIncoming: number;
    heartbeatOutgoing: number;
    debug: boolean;
}

export const WS_CONFIG: WebSocketConfig = {
    url: ENV.WS_URL,
    reconnectDelay: 5000,
    heartbeatIncoming: 4000,
    heartbeatOutgoing: 4000,
    debug: ENV.IS_DEV,
};

export const WS_TOPICS = {
    AUTOMATA_TRANSITIONS: '/topic/automata/transitions',
    FLIGHTS_UPDATES: '/topic/flights/updates',
    GATES_UPDATES: '/topic/gates/updates',
    ASSIGNMENTS_UPDATES: '/topic/assignments/updates',
} as const;

export type WebSocketTopic = typeof WS_TOPICS[keyof typeof WS_TOPICS];

export interface WebSocketCallbacks {
    onConnect?: () => void;
    onDisconnect?: () => void;
    onError?: (error: Error) => void;
    onMessage?: (topic: string, message: IMessage) => void;
}

export function createStompClient(callbacks: WebSocketCallbacks): Client {
    const socket = new SockJS(WS_CONFIG.url);

    const stompClient = new Client({
        webSocketFactory: () => socket as any,
        connectHeaders: {},
        debug: (str: string) => {
            if (WS_CONFIG.debug) {
                console.log('[WebSocket]', str);
            }
        },
        reconnectDelay: WS_CONFIG.reconnectDelay,
        heartbeatIncoming: WS_CONFIG.heartbeatIncoming,
        heartbeatOutgoing: WS_CONFIG.heartbeatOutgoing,
    });

    stompClient.onConnect = () => {
        console.log('[WebSocket] Conectado exitosamente');
        callbacks.onConnect?.();
    };

    stompClient.onDisconnect = () => {
        console.log('[WebSocket] Desconectado');
        callbacks.onDisconnect?.();
    };

    stompClient.onStompError = (frame: any) => {
        console.error('[WebSocket] Error STOMP:', frame.headers['message']);
        console.error('[WebSocket] Detalles:', frame.body);
        callbacks.onError?.(new Error(frame.headers['message']));
    };

    stompClient.onWebSocketError = (event: any) => {
        console.error('[WebSocket] Error de conexión:', event);
        callbacks.onError?.(new Error('WebSocket connection error'));
    };

    return stompClient;
}
