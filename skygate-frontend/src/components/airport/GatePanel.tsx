import VirtualLED from '../airport/VirtualLED';
import VirtualDisplay from '../airport/VirtualDisplay';
import type { Gate } from '../../types/backend.types';
import clsx from 'clsx';

interface GatePanelProps {
    gates: Gate[];
    groupByTerminal?: boolean;
    showDisplays?: boolean;
}

export default function GatePanel({ gates, groupByTerminal = false, showDisplays = true }: GatePanelProps) {
    if (groupByTerminal) {
        const gatesByTerminal: Record<string, Gate[]> = gates.reduce((acc, gate) => {
            if (!acc[gate.terminal]) {
                acc[gate.terminal] = [];
            }
            acc[gate.terminal].push(gate);
            return acc;
        }, {} as Record<string, Gate[]>);

        return (
            <div className="space-y-6">
                {Object.entries(gatesByTerminal).map(([terminal, terminalGates]) => (
                    <div key={terminal} className="bg-gray-800 rounded-xl p-6 border border-gray-700">
                        <h3 className="text-lg font-semibold text-white mb-4">Terminal {terminal}</h3>
                        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                            {terminalGates.map((gate) => (
                                <GateCard key={gate.id} gate={gate} showDisplay={showDisplays} />
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        );
    }

    return (
        <div className="bg-gray-800 rounded-xl p-6 border border-gray-700">
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {gates.map((gate) => (
                    <GateCard key={gate.id} gate={gate} showDisplay={showDisplays} />
                ))}
            </div>
        </div>
    );
}

function GateCard({ gate, showDisplay = true }: { gate: Gate; showDisplay?: boolean }) {
    const statusColors: Record<string, string> = {
        FREE: 'border-gate-free',
        ASSIGNED: 'border-gate-assigned',
        OCCUPIED: 'border-gate-occupied',
        MAINTENANCE: 'border-gate-maintenance',
        RESERVED: 'border-blue-500',
    };

    const statusText: Record<string, string> = {
        FREE: 'Disponible',
        ASSIGNED: 'Asignado',
        OCCUPIED: 'Ocupado',
        MAINTENANCE: 'Mantenimiento',
        RESERVED: 'Reservado',
    };

    const getDisplayType = (): 'success' | 'warning' | 'info' | 'error' => {
        switch (gate.status) {
            case 'FREE':
                return 'success';
            case 'ASSIGNED':
                return 'info';
            case 'OCCUPIED':
                return 'warning';
            case 'MAINTENANCE':
                return 'error';
            default:
                return 'info';
        }
    };

    return (
        <div className="space-y-3">
            <div
                className={clsx(
                    'bg-gray-900 rounded-lg p-4 border-2 transition-all hover:scale-105',
                    statusColors[gate.status]
                )}
            >
                <div className="flex items-center justify-between mb-3">
                    <span className="text-xl font-bold text-white">{gate.gateNumber}</span>
                    <VirtualLED status={gate.status} size="md" />
                </div>

                <div className="space-y-1">
                    <p className="text-xs text-gray-400">Tipo: {gate.gateType}</p>
                    <p className="text-xs text-gray-400">Terminal: {gate.terminal}</p>
                    <p
                        className={clsx('text-xs font-medium', {
                            'text-gate-free': gate.status === 'FREE',
                            'text-gate-assigned': gate.status === 'ASSIGNED',
                            'text-gate-occupied': gate.status === 'OCCUPIED',
                            'text-gate-maintenance': gate.status === 'MAINTENANCE',
                        })}
                    >
                        {statusText[gate.status]}
                    </p>
                </div>
            </div>

            {showDisplay && (
                <VirtualDisplay
                    message={gate.status === 'FREE' ? 'DISPONIBLE' : `GATE ${gate.gateNumber}`}
                    subMessage={statusText[gate.status]}
                    type={getDisplayType()}
                    showClock={gate.status === 'FREE'}
                    animated={true}
                />
            )}
        </div>
    );
}
