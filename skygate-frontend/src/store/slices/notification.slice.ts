import { StateCreator } from 'zustand';

export type NotificationType = 'success' | 'error' | 'warning' | 'info';

export interface Notification {
    id: string;
    type: NotificationType;
    message: string;
    timestamp: number;
    read: boolean;
}

export interface Toast extends Notification {
    autoClose: boolean;
    duration: number;
}

export interface NotificationSlice {
    toasts: Toast[];
    dropdownNotifications: Notification[];

    addToast: (toast: Omit<Toast, 'id'>) => string;
    addDropdownNotification: (notification: Omit<Notification, 'id'>) => string;

    removeToast: (id: string) => void;
    removeDropdownNotification: (id: string) => void;

    markAsRead: (id: string) => void;
    clearAllDropdownNotifications: () => void;
    clearReadNotifications: () => void;
}

export const createNotificationSlice: StateCreator<NotificationSlice> = (set, get) => ({
    toasts: [],
    dropdownNotifications: [],

    addToast: (toast) => {
        const id = `toast-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
        const newToast: Toast = {
            ...toast,
            id,
            timestamp: toast.timestamp || Date.now(),
            read: false,
        };

        set((state) => ({
            toasts: [...state.toasts, newToast],
        }));

        if (newToast.autoClose) {
            setTimeout(() => {
                get().removeToast(id);
            }, newToast.duration || 3000);
        }

        return id;
    },

    addDropdownNotification: (notification) => {
        const id = `notif-${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
        const newNotification: Notification = {
            ...notification,
            id,
            timestamp: notification.timestamp || Date.now(),
            read: false,
        };

        set((state) => ({
            dropdownNotifications: [newNotification, ...state.dropdownNotifications],
        }));

        return id;
    },

    removeToast: (id) => {
        set((state) => ({
            toasts: state.toasts.filter((toast) => toast.id !== id),
        }));
    },

    removeDropdownNotification: (id) => {
        set((state) => ({
            dropdownNotifications: state.dropdownNotifications.filter(
                (notification) => notification.id !== id
            ),
        }));
    },

    markAsRead: (id) => {
        set((state) => ({
            dropdownNotifications: state.dropdownNotifications.map((notification) =>
                notification.id === id ? { ...notification, read: true } : notification
            ),
        }));
    },

    clearAllDropdownNotifications: () => {
        set({ dropdownNotifications: [] });
    },

    clearReadNotifications: () => {
        set((state) => ({
            dropdownNotifications: state.dropdownNotifications.filter(
                (notification) => !notification.read
            ),
        }));
    },
});
