import React from 'react';

interface BadgeProps {
    text: string;
    color?: string; // Tailwind border/text color classes
}

const Badge: React.FC<BadgeProps> = ({ text, color = 'border-gray-800 text-gray-800' }) => {
    return (
        <span
            className={`inline-flex items-center gap-x-1.5 py-1.5 px-3 rounded-full text-xs font-medium border ${color}`}
        >
      {text}
    </span>
    );
};

export default Badge;
