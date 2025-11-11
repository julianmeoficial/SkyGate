import { NavLink } from 'react-router-dom';
import {
    HomeIcon,
    BuildingOffice2Icon,
    CpuChipIcon,
    ClipboardDocumentListIcon,
    ChartBarIcon,
} from '@heroicons/react/24/outline';
import clsx from 'clsx';

function AirplaneIcon({ className }: { className?: string }) {
    return (
        <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 19l9 2-9-18-9 18 9-2zm0 0v-8" />
        </svg>
    );
}

function DiagramIcon({ className }: { className?: string }) {
    return (
        <svg className={className} fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 20l-5.447-2.724A1 1 0 013 16.382V5.618a1 1 0 011.447-.894L9 7m0 13l6-3m-6 3V7m6 10l4.553 2.276A1 1 0 0021 18.382V7.618a1 1 0 00-.553-.894L15 4m0 13V4m0 0L9 7" />
        </svg>
    );
}

export default function Sidebar() {
    const navigation = [
        { name: 'Dashboard', href: '/', icon: HomeIcon },
        { name: 'Vuelos', href: '/vuelos', icon: AirplaneIcon },
        { name: 'Gates', href: '/gates', icon: BuildingOffice2Icon },
        { name: 'Autómata', href: '/automata', icon: CpuChipIcon },
        { name: 'Diagrama de Proceso', href: '/process-diagram', icon: DiagramIcon },
        { name: 'Asignaciones', href: '/asignaciones', icon: ClipboardDocumentListIcon },
        { name: 'Monitoreo', href: '/monitoreo', icon: ChartBarIcon },
    ];

    return (
        <aside className="w-64 bg-gray-950 border-r border-gray-800 flex flex-col">
            <div className="p-6">
                <div className="flex items-center gap-3">
                    <div className="w-10 h-10 bg-gradient-to-br from-blue-500 to-purple-600 rounded-lg flex items-center justify-center">
                        <AirplaneIcon className="w-6 h-6 text-white" />
                    </div>
                    <div>
                        <h2 className="text-lg font-bold text-white">SkyGate</h2>
                        <p className="text-xs text-gray-400">Control AFD</p>
                    </div>
                </div>
            </div>

            <nav className="flex-1 px-3 py-4">
                <ul className="space-y-2">
                    {navigation.map((item) => (
                        <li key={item.name}>
                            <NavLink
                                to={item.href}
                                className={({ isActive }) =>
                                    clsx(
                                        'flex items-center gap-3 px-3 py-2 rounded-lg text-sm font-medium transition-colors',
                                        isActive
                                            ? 'bg-blue-600 text-white'
                                            : 'text-gray-400 hover:bg-gray-800 hover:text-white'
                                    )
                                }
                            >
                                <item.icon className="w-5 h-5" />
                                {item.name}
                            </NavLink>
                        </li>
                    ))}
                </ul>
            </nav>

            <div className="p-4 border-t border-gray-800">
                <p className="text-xs text-gray-500 text-center">
                    v1.1.0 - Sistema Operando
                </p>
            </div>
        </aside>
    );
}
