import React from 'react';

interface InputSimpleLabelProps {
    label?: string;
    description?: string;
    placeholder?: string;
    value?: string;
    onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
    name?: string;
    type?: string;
}

const InputSimpleLabel: React.FC<InputSimpleLabelProps> = ({
                                                               label = 'Input Name',
                                                               description = 'Some Description',
                                                               placeholder = '',
                                                               value,
                                                               onChange,
                                                               name = 'inputname',
                                                               type = 'text'
                                                           }) => {
    return (
        <div>
            <label htmlFor={name} className="block text-gray-800 font-semibold text-sm">
                {label}
            </label>
            <div className="mt-2">
                <input
                    type={type}
                    name={name}
                    id={name}
                    placeholder={placeholder}
                    value={value}
                    onChange={onChange}
                    className="block w-56 rounded-md py-1.5 px-2 ring-1 ring-inset ring-gray-400 focus:text-gray-800"
                />
            </div>
            <label className="pt-1 block text-gray-500 text-sm">{description}</label>
        </div>
    );
};

export default InputSimpleLabel;
