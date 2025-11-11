import React from 'react';
import { Icon } from '../../components/common/Icon';
import type { IconName } from '../../components/common/Icon/iconTypes';

interface AircraftIconProps {
    type: 'NARROWBODY' | 'WIDEBODY' | 'JUMBO';
    size?: 'sm' | 'md' | 'lg' | 'xl' | '2xl';
    animated?: boolean;
    className?: string;
}

const AircraftIcon: React.FC<AircraftIconProps> = ({ type, size = 'md', animated = false, className = '' }) => {
    const iconMap: Record<string, IconName> = {
        NARROWBODY: 'narrow-body',
        WIDEBODY: 'wide-body',
        JUMBO: 'jumbo',
    };

    const iconName = iconMap[type];

    return <Icon name={iconName} size={size} animated={animated} className={className} />;
};

export default AircraftIcon;
