import { useEffect, useRef, useState, useCallback } from 'react';
import { Client, IMessage, StompSubscription } from '@stomp/stompjs';
import { createStompClient, WS_TOPICS, ConnectionStatus } from './socket.config';
import { useStore } from '../../store';
import type { AutomataTransition, Flight, Gate, Assignment } from '../../types/backend.types';

let globalClient: Client | null = null;
let globalSubscriptions: StompSubscription[] = [];
let isGloballyConnected = false;

export function useWebSocket() {
    const [status, setStatus] = useState<ConnectionStatus>('DISCONNECTED');
    const mountedRef = useRef(true);
    const reconnectTimeoutRef = useRef<NodeJS.Timeout | undefined>(undefined);
    const processedMessagesRef = useRef<Set<string>>(new Set());

    const addTransition = useStore(state => state.addTransition);
    const setCurrentState = useStore(state => state.setCurrentState);
    const addToast = useStore(state => state.addToast);
    const addDropdownNotification = useStore(state => state.addDropdownNotification);

    const isDuplicateMessage = useCallback((messageId: string): boolean => {
        if (processedMessagesRef.current.has(messageId)) {
            return true;
        }
        processedMessagesRef.current.add(messageId);

        if (processedMessagesRef.current.size > 1000) {
            const items = Array.from(processedMessagesRef.current);
            processedMessagesRef.current = new Set(items.slice(-500));
        }

        return false;
    }, []);

    const unsubscribeAll = useCallback(() => {
        globalSubscriptions.forEach(subscription => {
            try {
                subscription.unsubscribe();
            } catch (error) {
                console.error('[WebSocket] Error al desuscribirse:', error);
            }
        });
        globalSubscriptions = [];
    }, []);

    const setupSubscriptions = useCallback(() => {
        if (!globalClient || !globalClient.connected || !mountedRef.current) {
            return;
        }

        unsubscribeAll();

        const automataSubscription = globalClient.subscribe(WS_TOPICS.AUTOMATA_TRANSITIONS, (message: IMessage) => {
            try {
                const transition: AutomataTransition = JSON.parse(message.body);
                const messageId = `automata-${transition.flightId}-${transition.toState}-${transition.timestamp || Date.now()}`;

                if (isDuplicateMessage(messageId)) {
                    console.log('[WebSocket] Mensaje duplicado ignorado:', messageId);
                    return;
                }

                console.log('[WebSocket] Transición recibida:', transition);

                addTransition(transition);
                setCurrentState(transition.flightId, transition.toState);

                const notificationMessages: Record<string, { message: string; type: 'success' | 'error' | 'info' | 'warning' }> = {
                    S0: { message: `Sistema inicializado`, type: 'info' },
                    S1: { message: `Vuelo ${transition.flightNumber} - Wide Body detectado`, type: 'info' },
                    S2: { message: `Vuelo ${transition.flightNumber} - Jumbo detectado`, type: 'info' },
                    S3: { message: `Vuelo ${transition.flightNumber} - Narrow Body detectado`, type: 'info' },
                    S4: {
                        message: transition.event === 'WAITING_FLIGHT_REASSIGNED'
                            ? `Vuelo ${transition.flightNumber} reasignado automáticamente desde espera`
                            : `Vuelo ${transition.flightNumber} - Gate asignado exitosamente`,
                        type: 'success'
                    },
                    S5: { message: `Vuelo ${transition.flightNumber} - Aeronave estacionada en gate`, type: 'success' },
                    S6: { message: `Vuelo ${transition.flightNumber} - Sin gates disponibles, en espera`, type: 'warning' },
                };

                const notif = notificationMessages[transition.toState];
                if (notif) {
                    addDropdownNotification({
                        type: notif.type,
                        message: notif.message,
                        timestamp: Date.now(),
                        read: false,
                    });
                }
            } catch (error) {
                console.error('[WebSocket] Error parsing transition:', error);
            }
        });

        const flightsSubscription = globalClient.subscribe(WS_TOPICS.FLIGHTS_UPDATES, (message: IMessage) => {
            try {
                const flight: Flight = JSON.parse(message.body);
                const messageId = `flight-${flight.id}-${flight.status}-${Date.now()}`;

                if (isDuplicateMessage(messageId)) {
                    return;
                }

                console.log('[WebSocket] Actualización de vuelo:', flight);

                addDropdownNotification({
                    type: 'info',
                    message: `Vuelo ${flight.flightNumber} actualizado`,
                    timestamp: Date.now(),
                    read: false,
                });
            } catch (error) {
                console.error('[WebSocket] Error parsing flight:', error);
            }
        });

        const gatesSubscription = globalClient.subscribe(WS_TOPICS.GATES_UPDATES, (message: IMessage) => {
            try {
                const gate: Gate = JSON.parse(message.body);
                const messageId = `gate-${gate.id}-${gate.status}-${Date.now()}`;

                if (isDuplicateMessage(messageId)) {
                    return;
                }

                console.log('[WebSocket] Actualización de gate:', gate);

                const statusLabels: Record<string, string> = {
                    FREE: 'disponible',
                    ASSIGNED: 'asignado',
                    OCCUPIED: 'ocupado',
                    MAINTENANCE: 'en mantenimiento',
                    RESERVED: 'reservado',
                };

                addDropdownNotification({
                    type: 'info',
                    message: `Gate ${gate.gateNumber} ahora está ${statusLabels[gate.status] || gate.status}`,
                    timestamp: Date.now(),
                    read: false,
                });
            } catch (error) {
                console.error('[WebSocket] Error parsing gate:', error);
            }
        });

        const assignmentsSubscription = globalClient.subscribe(WS_TOPICS.ASSIGNMENTS_UPDATES, (message: IMessage) => {
            try {
                const assignment: Assignment = JSON.parse(message.body);
                const messageId = `assignment-${assignment.id}-${Date.now()}`;

                if (isDuplicateMessage(messageId)) {
                    return;
                }

                console.log('[WebSocket] Nueva asignación:', assignment);

                addDropdownNotification({
                    type: 'success',
                    message: `Vuelo ${assignment.flight.flightNumber} asignado a Gate ${assignment.gate.gateNumber}`,
                    timestamp: Date.now(),
                    read: false,
                });
            } catch (error) {
                console.error('[WebSocket] Error parsing assignment:', error);
            }
        });

        globalSubscriptions = [
            automataSubscription,
            flightsSubscription,
            gatesSubscription,
            assignmentsSubscription,
        ];

        console.log('[WebSocket] Suscripciones configuradas:', globalSubscriptions.length);
    }, [addTransition, setCurrentState, addDropdownNotification, unsubscribeAll, isDuplicateMessage]);

    useEffect(() => {
        mountedRef.current = true;

        if (isGloballyConnected && globalClient) {
            setStatus('CONNECTED');
            return;
        }

        if (globalClient) {
            return;
        }

        const handleConnect = () => {
            if (!mountedRef.current) return;

            isGloballyConnected = true;
            setStatus('CONNECTED');
            console.log('[WebSocket] Estado: CONECTADO');

            addToast({
                type: 'success',
                message: 'Conexión WebSocket establecida',
                timestamp: Date.now(),
                read: false,
                autoClose: true,
                duration: 3000,
            });

            setupSubscriptions();
        };

        const handleDisconnect = () => {
            isGloballyConnected = false;
            setStatus('DISCONNECTED');
            console.log('[WebSocket] Estado: DESCONECTADO');

            unsubscribeAll();

            if (mountedRef.current) {
                addToast({
                    type: 'warning',
                    message: 'Conexión WebSocket perdida. Reconectando...',
                    timestamp: Date.now(),
                    read: false,
                    autoClose: true,
                    duration: 4000,
                });
            }

            if (reconnectTimeoutRef.current) {
                clearTimeout(reconnectTimeoutRef.current);
            }

            reconnectTimeoutRef.current = setTimeout(() => {
                if (!mountedRef.current) return;

                console.log('[WebSocket] Intentando reconectar...');
                setStatus('CONNECTING');
                if (globalClient) {
                    globalClient.activate();
                }
            }, 5000);
        };

        const handleError = (error: Error) => {
            setStatus('ERROR');
            console.error('[WebSocket] Error:', error);

            if (mountedRef.current) {
                addToast({
                    type: 'error',
                    message: 'Error en conexión WebSocket',
                    timestamp: Date.now(),
                    read: false,
                    autoClose: false,
                    duration: 5000,
                });
            }
        };

        setStatus('CONNECTING');
        globalClient = createStompClient({
            onConnect: handleConnect,
            onDisconnect: handleDisconnect,
            onError: handleError,
        });

        globalClient.activate();

        return () => {
            mountedRef.current = false;

            if (reconnectTimeoutRef.current) {
                clearTimeout(reconnectTimeoutRef.current);
            }
        };
    }, [addToast, setupSubscriptions, unsubscribeAll]);

    return {
        status,
        isConnected: status === 'CONNECTED',
        isConnecting: status === 'CONNECTING',
        isDisconnected: status === 'DISCONNECTED',
        hasError: status === 'ERROR',
    };
}
