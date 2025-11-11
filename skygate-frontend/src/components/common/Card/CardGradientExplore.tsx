import React from 'react';

interface CardGradientExploreProps {
    title?: string;
    description?: string;
    buttonText?: string;
    onButtonClick?: () => void;
}

const CardGradientExplore: React.FC<CardGradientExploreProps> = ({
                                                                     title = 'Heading',
                                                                     description = 'Lorem ipsum dolor sit amet consectetur adipisicing elit. Vero dolorum blanditiis pariatur sequi magni.',
                                                                     buttonText = 'Explore Now',
                                                                     onButtonClick
                                                                 }) => {
    return (
        <div className="relative h-auto w-80 border-2 border-purple-500 border-opacity-50 rounded-3xl bg-gradient-to-br from-purple-900 via-purple-700 via-opacity-80 to-purple-900 to-opacity-20 text-white font-sans p-6 flex justify-center items-left flex-col gap-4 backdrop-blur-xl hover:shadow-2xl hover:shadow-purple-500 hover:shadow-opacity-30 transition-all duration-500 group hover:-translate-y-1">
            <div className="absolute inset-0 bg-gradient-to-br from-purple-600 from-opacity-30 via-fuchsia-500 via-opacity-20 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500 rounded-3xl"></div>
            <div className="absolute inset-0 bg-radial-gradient opacity-0 group-hover:animate-pulse"></div>
            <div className="absolute top-4 right-4 flex gap-2">
                <div className="w-2 h-2 rounded-full bg-purple-300 bg-opacity-50"></div>
                <div className="w-2 h-2 rounded-full bg-purple-300 bg-opacity-30"></div>
                <div className="w-2 h-2 rounded-full bg-purple-300 bg-opacity-10"></div>
            </div>
            <div className="relative z-10 transition-transform duration-300 group-hover:-translate-y-1 space-y-3">
                <h1 className="text-4xl font-bold bg-gradient-to-r from-white via-purple-100 to-purple-200 bg-clip-text text-transparent">
                    {title}
                </h1>
                <p className="text-sm text-purple-100 text-opacity-90 leading-relaxed font-light">
                    {description}
                </p>
            </div>
            <button
                onClick={onButtonClick}
                className="relative h-fit w-fit px-6 py-3 mt-2 border border-purple-300 border-opacity-30 rounded-full flex justify-center items-center gap-3 overflow-hidden group-btn hover:border-purple-300 hover:border-opacity-50 hover:shadow-lg hover:shadow-purple-500 hover:shadow-opacity-20 active:scale-95 transition-all duration-300 backdrop-blur-xl bg-purple-500 bg-opacity-10"
            >
                <div className="absolute inset-0 bg-gradient-to-r from-purple-600 from-opacity-40 via-fuchsia-500 via-opacity-40 to-purple-600 to-opacity-40 -translate-x-full group-hover:translate-x-full transition-transform duration-700"></div>
                <p className="relative z-10 font-medium tracking-wide">{buttonText}</p>
                <svg
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                    strokeWidth={2}
                    stroke="currentColor"
                    className="relative z-10 w-5 h-5 group-hover:translate-x-2 transition-transform duration-300"
                >
                    <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M13.5 4.5 21 12m0 0-7.5 7.5M21 12H3"
                    />
                </svg>
            </button>
            <div className="absolute bottom-4 left-4 w-8 h-8 rounded-full bg-gradient-to-br from-purple-400 from-opacity-20 to-transparent blur-sm group-hover:animate-pulse"></div>
        </div>
    );
};

export default CardGradientExplore;
