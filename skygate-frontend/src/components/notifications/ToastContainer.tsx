import { useStore } from '../../store';
import { Toast } from './Toast';

export const ToastContainer = () => {
    const toasts = useStore((state) => state.toasts);
    const removeToast = useStore((state) => state.removeToast);

    return (
        <div className="fixed top-20 right-4 z-50 space-y-2 max-w-sm">
            {toasts.map((toast) => (
                <Toast
                    key={toast.id}
                    id={toast.id}
                    type={toast.type}
                    message={toast.message}
                    onClose={() => removeToast(toast.id)}
                    autoClose={toast.autoClose}
                    duration={toast.duration}
                />
            ))}
        </div>
    );
};
