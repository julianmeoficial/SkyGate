import React from 'react';

interface ButtonNeumorphismProps {
    children?: React.ReactNode;
    onClick?: () => void;
    icon?: React.ReactNode;
    disabled?: boolean;
    type?: 'button' | 'submit' | 'reset';
}

const ButtonNeumorphism: React.FC<ButtonNeumorphismProps> = ({
                                                                 children = 'Button',
                                                                 onClick,
                                                                 icon,
                                                                 disabled = false,
                                                                 type = 'button'
                                                             }) => {
    return (
        <div className="flex justify-center items-center border-2 border-radius border-white-200 overflow-hidden p-1 rounded-full shadow-lg">
            <button
                type={type}
                onClick={onClick}
                disabled={disabled}
                className="bg-gradient-to-r from-[#e9e9e9] via-[#e9e9e950] to-[#fff] group w-50 h-16 inline-flex transition-all duration-300 overflow-visible p-1 rounded-full disabled:opacity-50 disabled:cursor-not-allowed"
            >
                <div className="w-full h-full bg-gradient-to-t from-[#ececec] to-[#fff] overflow-hidden shadow-[0_0_1px_rgba(0,0,0,0.07),0_0_1px_rgba(0,0,0,0.05),0_3px_3px_rgba(0,0,0,0.25),0_1px_3px_rgba(0,0,0,0.12)] p-1 rounded-full hover:shadow-none duration-300">
                    <div className="w-full h-full text-xl gap-x-0.5 gap-y-0.5 justify-center text-[#101010] bg-gradient-to-r from-[#f4f4f4] to-[#fefefe] group-hover:bg-gradient-to-r group-hover:from-[#e2e2e2] group-hover:to-[#fefefe] duration-200 items-center text-[18px] font-medium gap-4 inline-flex overflow-hidden px-4 py-2 rounded-full group-hover:text-blue-600">
                        {icon && <span className="flex items-center justify-center">{icon}</span>}
                        <span className="ml-2">{children}</span>
                    </div>
                </div>
            </button>
        </div>
    );
};

export default ButtonNeumorphism;
