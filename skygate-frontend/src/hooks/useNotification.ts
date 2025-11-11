import { useStore } from '../store';
import type { NotificationType } from '../store/slices/notification.slice';

export function useNotification() {
    const addToast = useStore((state) => state.addToast);
    const addDropdownNotification = useStore((state) => state.addDropdownNotification);
    const removeToast = useStore((state) => state.removeToast);
    const removeDropdownNotification = useStore((state) => state.removeDropdownNotification);

    const notify = {
        success: (message: string, autoClose = true, duration = 5000) => {
            return addToast({
                type: 'success',
                message,
                timestamp: Date.now(),
                read: false,
                autoClose,
                duration,
            });
        },

        error: (message: string, autoClose = true, duration = 7000) => {
            return addToast({
                type: 'error',
                message,
                timestamp: Date.now(),
                read: false,
                autoClose,
                duration,
            });
        },

        warning: (message: string, autoClose = true, duration = 6000) => {
            return addToast({
                type: 'warning',
                message,
                timestamp: Date.now(),
                read: false,
                autoClose,
                duration,
            });
        },

        info: (message: string, autoClose = true, duration = 5000) => {
            return addToast({
                type: 'info',
                message,
                timestamp: Date.now(),
                read: false,
                autoClose,
                duration,
            });
        },

        custom: (
            message: string,
            type: NotificationType,
            options?: { autoClose?: boolean; duration?: number }
        ) => {
            return addToast({
                type,
                message,
                timestamp: Date.now(),
                read: false,
                autoClose: options?.autoClose !== false,
                duration: options?.duration || 5000,
            });
        },

        dropdown: (message: string, type: NotificationType = 'info') => {
            return addDropdownNotification({
                type,
                message,
                timestamp: Date.now(),
                read: false,
            });
        },

        removeToast: (id: string) => {
            removeToast(id);
        },

        removeDropdown: (id: string) => {
            removeDropdownNotification(id);
        },
    };

    return notify;
}
