import { ClockIcon } from '@heroicons/react/24/outline';
import { useFlights } from '../../hooks/useFlights';

export default function WaitingFlightsBadge() {
    const { flights } = useFlights(true);
    const waitingFlights = flights.filter(f => f.automataState === 'S6' && f.status === 'WAITING');

    if (waitingFlights.length === 0) {
        return null;
    }

    return (
        <div className="fixed bottom-4 right-4 bg-yellow-500/10 border border-yellow-500/20 rounded-lg p-4 shadow-xl backdrop-blur-sm z-40 max-w-xs">
            <div className="flex items-center gap-3">
                <div className="flex-shrink-0 w-10 h-10 rounded-full bg-yellow-500/20 flex items-center justify-center">
                    <ClockIcon className="w-6 h-6 text-yellow-500 animate-pulse" />
                </div>
                <div>
                    <p className="text-sm font-semibold text-yellow-400">
                        {waitingFlights.length} Vuelo{waitingFlights.length !== 1 ? 's' : ''} en Espera
                    </p>
                    <p className="text-xs text-yellow-300/70">
                        Esperando disponibilidad de gates
                    </p>
                </div>
            </div>
            <div className="mt-2 space-y-1 max-h-32 overflow-y-auto">
                {waitingFlights.map(flight => (
                    <div key={flight.id} className="text-xs text-yellow-200/80 flex items-center gap-2">
                        <div className="w-1.5 h-1.5 rounded-full bg-yellow-400 animate-pulse" />
                        {flight.flightNumber} - {flight.aircraftType}
                    </div>
                ))}
            </div>
        </div>
    );
}
