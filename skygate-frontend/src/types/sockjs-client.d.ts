declare module 'sockjs-client' {
    export default class SockJS {
        constructor(url: string, _reserved?: unknown, options?: {
            transports?: string[];
            timeout?: number;
        });
        onopen: ((event: Event) => void) | null;
        onmessage: ((event: MessageEvent) => void) | null;
        onclose: ((event: CloseEvent) => void) | null;
        onerror: ((event: Event) => void) | null;
        send(data: string): void;
        close(code?: number, reason?: string): void;
    }
}
