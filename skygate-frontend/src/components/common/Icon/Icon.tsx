import React, { useEffect, useRef } from 'react';
import gsap from 'gsap';
import type { IconName, AircraftIconName } from './iconTypes';
import { iconMap } from './iconTypes';

interface IconProps {
    name: IconName;
    size?: 'xs' | 'sm' | 'md' | 'lg' | 'xl' | '2xl';
    className?: string;
    alt?: string;
    animated?: boolean;
}

const sizeMap = {
    xs: 'w-3 h-3',
    sm: 'w-4 h-4',
    md: 'w-6 h-6',
    lg: 'w-8 h-8',
    xl: 'w-12 h-12',
    '2xl': 'w-16 h-16',
};

const aircraftIcons: AircraftIconName[] = ['narrow-body', 'wide-body', 'jumbo'];

export const Icon: React.FC<IconProps> = ({
                                              name,
                                              size = 'md',
                                              className = '',
                                              alt,
                                              animated = false
                                          }) => {
    const iconRef = useRef<HTMLImageElement>(null);
    const isAircraft = aircraftIcons.includes(name as AircraftIconName);

    useEffect(() => {
        if (animated && isAircraft && iconRef.current) {
            const timeline = gsap.timeline({ repeat: -1, yoyo: true });

            timeline.to(iconRef.current, {
                y: -8,
                duration: 1.5,
                ease: 'power1.inOut',
            });

            return () => {
                timeline.kill();
            };
        }
    }, [animated, isAircraft]);

    const iconSrc = iconMap[name];

    if (!iconSrc) {
        console.warn(`Icon "${name}" no encontrado en iconMap`);
        return null;
    }

    return (
        <img
            ref={iconRef}
            src={iconSrc}
            alt={alt || name}
            className={`${sizeMap[size]} ${className} object-contain`}
            loading="lazy"
        />
    );
};
