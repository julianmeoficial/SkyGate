import { ReactNode } from 'react';
import Sidebar from './Sidebar';
import Navbar from './Navbar';
import WaitingFlightsBadge from '../flights/WaitingFlightsBadge';

interface MainLayoutProps {
    children: ReactNode;
}

export default function MainLayout({ children }: MainLayoutProps) {
    return (
        <div className="flex h-screen w-screen overflow-hidden bg-gray-950">
            <Sidebar />

            <div className="flex flex-col flex-1 overflow-hidden">
                <Navbar />

                <main className="flex-1 overflow-y-auto bg-gray-900 p-6">
                    <div className="mx-auto max-w-7xl">
                        {children}
                    </div>
                </main>
            </div>

            <WaitingFlightsBadge />
        </div>
    );
}
