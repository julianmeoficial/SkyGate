import React from 'react';

interface CardGradientProfileProps {
    title?: string;
    subtitle?: string;
    buttonText?: string;
    onButtonClick?: () => void;
}

const CardGradientProfile: React.FC<CardGradientProfileProps> = ({
                                                                     title = 'Card title',
                                                                     subtitle = '4 popular types of cards in UI design.',
                                                                     buttonText = 'See more',
                                                                     onButtonClick
                                                                 }) => {
    return (
        <div className="w-60 h-80 bg-neutral-800 rounded-3xl text-neutral-300 p-4 flex flex-col items-start justify-center gap-3 hover:bg-gray-900 hover:shadow-2xl hover:shadow-sky-400 transition-shadow">
            <div className="w-52 h-40 bg-sky-300 rounded-2xl"></div>
            <div>
                <p className="font-extrabold">{title}</p>
                <p>{subtitle}</p>
            </div>
            <button
                onClick={onButtonClick}
                className="bg-sky-700 font-extrabold p-2 px-6 rounded-xl hover:bg-sky-500 transition-colors"
            >
                {buttonText}
            </button>
        </div>
    );
};

export default CardGradientProfile;
