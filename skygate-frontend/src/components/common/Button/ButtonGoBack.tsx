import React from 'react';

interface ButtonGoBackProps {
    children?: React.ReactNode;
    onClick?: () => void;
    disabled?: boolean;
    type?: 'button' | 'submit' | 'reset';
}

const ButtonGoBack: React.FC<ButtonGoBackProps> = ({
                                                       children = 'Atras',
                                                       onClick,
                                                       disabled = false,
                                                       type = 'button'
                                                   }) => {
    return (
        <button
            type={type}
            onClick={onClick}
            disabled={disabled}
            className="bg-white text-center w-48 rounded-2xl h-14 relative text-black text-xl font-semibold border-4 border-white group disabled:opacity-50 disabled:cursor-not-allowed"
        >
            <div className="bg-green-400 rounded-xl h-12 w-14 grid place-items-center absolute left-0 top-0 group-hover:w-full z-10 duration-500">
                <svg
                    width="25px"
                    height="25px"
                    viewBox="0 0 1024 1024"
                    xmlns="http://www.w3.org/2000/svg"
                >
                    <path
                        fill="#000000"
                        d="M224 480h640a32 32 0 1 1 0 64H224a32 32 0 0 1 0-64z"
                    />
                    <path
                        fill="#000000"
                        d="m237.248 512 265.408 265.344a32 32 0 0 1-45.312 45.312l-288-288a32 32 0 0 1 0-45.312l288-288a32 32 0 1 1 45.312 45.312L237.248 512z"
                    />
                </svg>
            </div>
            <p className="translate-x-4">{children}</p>
        </button>
    );
};

export default ButtonGoBack;
