import React from 'react';

interface InputFocusRingProps {
    placeholder?: string;
    value?: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    name?: string;
    type?: string;
}

const InputFocusRing: React.FC<InputFocusRingProps> = ({
                                                           placeholder = 'Enter your name...',
                                                           value,
                                                           onChange,
                                                           name = 'text',
                                                           type = 'text'
                                                       }) => {
    return (
        <input
            className="input h-[34px] text-[14px] text-white text-opacity-60 w-[240px] bg-[#09090b] text-[#f4f4f5] px-3 py-1 rounded-lg border border-white border-opacity-10 focus:outline-none focus:ring-2 focus:ring-gray-700 focus:ring-offset-2 focus:ring-offset-[#09090b] transition-all duration-150 ease-in-out"
            name={name}
            type={type}
            placeholder={placeholder}
            value={value}
            onChange={onChange}
        />
    );
};

export default InputFocusRing;
