export type AircraftIconName = 'narrow-body' | 'wide-body' | 'jumbo';
export type UIIconName =
    | 'dashboard'
    | 'airport'
    | 'boarding'
    | 'chart'
    | 'clock-time'
    | 'activity-status'
    | 'location'
    | 'radar'
    | 'runway'
    | 'tower';

export type IconName = AircraftIconName | UIIconName;

export const iconMap: Record<IconName, string> = {
    'dashboard': '/assets/icons/dashboard.png',
    'airport': '/assets/icons/airport.png',
    'boarding': '/assets/icons/boarding.png',
    'chart': '/assets/icons/chart.png',
    'clock-time': '/assets/icons/clock-time.png',
    'activity-status': '/assets/icons/activity-status.png',
    'location': '/assets/icons/location.png',
    'radar': '/assets/icons/radar.png',
    'runway': '/assets/icons/runway.png',
    'tower': '/assets/icons/tower.png',
    'narrow-body': '/assets/icons/NarrowBody.png',
    'wide-body': '/assets/icons/WideBody.png',
    'jumbo': '/assets/icons/Jumbo.png',
};
