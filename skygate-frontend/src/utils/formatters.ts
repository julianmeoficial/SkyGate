import { formatDistanceToNow, format } from 'date-fns';
import { es } from 'date-fns/locale';

export function formatRelativeTime(date: string | Date): string {
    return formatDistanceToNow(new Date(date), {
        addSuffix: true,
        locale: es,
    });
}

export function formatDateTime(date: string | Date): string {
    return format(new Date(date), "dd/MM/yyyy HH:mm:ss", { locale: es });
}

export function formatTime(date: string | Date): string {
    return format(new Date(date), "HH:mm:ss", { locale: es });
}

export function capitalizeFirst(str: string): string {
    return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}
