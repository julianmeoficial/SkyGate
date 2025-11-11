import React from 'react';
import { Listbox as HListbox } from '@headlessui/react';
import { CheckIcon, ChevronUpDownIcon } from '@heroicons/react/20/solid';

interface Person {
    id: number;
    name: string;
}

interface ListboxProps {
    items: Person[];
    onSelect: (item: Person) => void;
    selectedItem: Person;
}

const Listbox: React.FC<ListboxProps> = ({ items, selectedItem, onSelect }) => {
    return (
        <div className="w-56 mx-auto mt-6">
            <HListbox value={selectedItem} onChange={onSelect}>
                <div className="relative">
                    <HListbox.Button className="relative w-full cursor-default rounded-lg bg-white/10 py-2 pl-3 pr-10 text-left text-sm text-white shadow-md focus:outline-none focus-visible:ring-2 focus-visible:ring-white focus-visible:ring-opacity-75 focus-visible:ring-offset-2 focus-visible:ring-offset-teal-300">
                        <span className="block truncate">{selectedItem.name}</span>
                        <span className="pointer-events-none absolute inset-y-0 right-0 flex items-center pr-2">
                            <ChevronUpDownIcon className="h-5 w-5 text-white" aria-hidden="true" />
                        </span>
                    </HListbox.Button>
                    <HListbox.Options className="absolute mt-1 max-h-60 w-full overflow-auto rounded-md bg-white/20 py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                        {items.map((person) => (
                            <HListbox.Option
                                key={person.id}
                                className={({ active }: { active: boolean }) =>
                                    `relative cursor-default select-none py-2 pl-10 pr-4 ${
                                        active ? 'bg-teal-600 text-white' : 'text-white'
                                    }`
                                }
                                value={person}
                            >
                                {({ selected }: { selected: boolean }) => (
                                    <>
                                        <span className={`block truncate ${selected ? 'font-medium' : 'font-normal'}`}>
                                            {person.name}
                                        </span>
                                        {selected ? (
                                            <span className="absolute inset-y-0 left-0 flex items-center pl-3 text-teal-600">
                                                <CheckIcon className="h-5 w-5" aria-hidden="true" />
                                            </span>
                                        ) : null}
                                    </>
                                )}
                            </HListbox.Option>
                        ))}
                    </HListbox.Options>
                </div>
            </HListbox>
        </div>
    );
};

export default Listbox;
