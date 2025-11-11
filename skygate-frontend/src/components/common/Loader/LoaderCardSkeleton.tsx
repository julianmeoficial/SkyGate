import React from 'react';

const LoaderCardSkeleton: React.FC = () => {
    return (
        <div className="flex flex-col bg-neutral-300 w-56 h-64 animate-pulse rounded-xl p-4 gap-4">
            <div className="bg-neutral-400 bg-opacity-50 w-full h-32 animate-pulse rounded-md"></div>
            <div className="flex flex-col gap-2">
                <div className="bg-neutral-400 bg-opacity-50 w-full h-4 animate-pulse rounded-md"></div>
                <div className="bg-neutral-400 bg-opacity-50 w-[45%] h-4 animate-pulse rounded-md"></div>
                <div className="bg-neutral-400 bg-opacity-50 w-full h-4 animate-pulse rounded-md"></div>
                <div className="bg-neutral-400 bg-opacity-50 w-24 h-4 animate-pulse rounded-md"></div>
            </div>
        </div>
    );
};

export default LoaderCardSkeleton;
