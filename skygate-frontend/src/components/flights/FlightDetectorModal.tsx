import { useState } from 'react';
import {
    XMarkIcon,
    PaperAirplaneIcon,
    SparklesIcon,
    BuildingOffice2Icon,
    MapPinIcon
} from '@heroicons/react/24/outline';
import { useFlights } from '../../hooks/useFlights';
import type { AircraftType } from '../../types/backend.types';
import clsx from 'clsx';

interface FlightDetectorModalProps {
    onClose: () => void;
}

export default function FlightDetectorModal({ onClose }: FlightDetectorModalProps) {
    const { detectFlight, isDetecting } = useFlights();
    const [formData, setFormData] = useState({
        flightNumber: '',
        aircraftType: 'NARROW_BODY' as AircraftType,
        origin: '',
        destination: '',
        airline: '',
    });

    const aircraftOptions = [
        {
            value: 'NARROW_BODY',
            label: 'Narrow Body',
            description: 'Boeing 737, A320',
            gradient: 'from-cyan-500 via-blue-500 to-indigo-500',
            borderColor: 'border-cyan-500',
        },
        {
            value: 'WIDE_BODY',
            label: 'Wide Body',
            description: 'Boeing 787, A350',
            gradient: 'from-blue-500 via-indigo-500 to-purple-500',
            borderColor: 'border-blue-500',
        },
        {
            value: 'JUMBO',
            label: 'Jumbo',
            description: 'Boeing 747, A380',
            gradient: 'from-purple-500 via-pink-500 to-rose-500',
            borderColor: 'border-purple-500',
        },
    ];

    const handleSubmit = () => {
        if (!formData.flightNumber.trim()) return;
        detectFlight(formData);
        onClose();
    };

    const isFormValid = formData.flightNumber.trim().length > 0;

    return (
        <div
            className="fixed inset-0 bg-black/80 backdrop-blur-md flex items-center justify-center z-50 p-4"
            onClick={(e) => {
                if (e.target === e.currentTarget) onClose();
            }}
        >
            <div className="relative w-full max-w-4xl">
                <div className="absolute inset-0 bg-gradient-to-br from-blue-600/20 via-purple-600/20 to-pink-600/20 rounded-3xl blur-3xl" />

                <div className="relative bg-gray-900 rounded-3xl shadow-2xl overflow-hidden border border-gray-700/50">
                    <div className="px-8 py-6 border-b border-gray-700/50 bg-gradient-to-r from-gray-900 via-gray-800 to-gray-900">
                        <div className="flex items-center justify-between">
                            <div className="flex items-center gap-4">
                                <div className="w-16 h-16 rounded-2xl bg-gradient-to-br from-blue-600 to-purple-600 flex items-center justify-center shadow-lg">
                                    <PaperAirplaneIcon className="w-8 h-8 text-white rotate-45" />
                                </div>
                                <div>
                                    <h3 className="text-2xl font-bold text-white">Detectar Nuevo Vuelo</h3>
                                    <p className="text-sm text-gray-400 mt-0.5">Sistema de detección automática de aeronaves</p>
                                </div>
                            </div>
                            <button
                                onClick={onClose}
                                className="text-gray-400 hover:text-white hover:bg-white/10 p-2.5 rounded-xl transition-all duration-200"
                                disabled={isDetecting}
                            >
                                <XMarkIcon className="w-6 h-6" />
                            </button>
                        </div>
                    </div>

                    <div className="p-8 space-y-8">
                        <div className="space-y-3">
                            <label className="flex items-center gap-2 text-sm font-semibold text-white">
                                <SparklesIcon className="w-4 h-4 text-blue-400" />
                                Número de Vuelo
                                <span className="text-red-400">*</span>
                            </label>
                            <input
                                type="text"
                                value={formData.flightNumber}
                                onChange={(e) => setFormData({ ...formData, flightNumber: e.target.value.toUpperCase() })}
                                placeholder="Ejemplo: AA1234"
                                className="w-full px-6 py-4 bg-gray-800/50 border-2 border-gray-700 rounded-xl text-white placeholder-gray-500 focus:outline-none focus:border-blue-500 focus:bg-gray-800/70 transition-all duration-200 text-lg font-medium"
                            />
                        </div>

                        <div className="grid grid-cols-3 gap-4">
                            <div className="space-y-2">
                                <label className="flex items-center gap-2 text-sm font-medium text-gray-300">
                                    <BuildingOffice2Icon className="w-4 h-4" />
                                    Aerolínea
                                </label>
                                <input
                                    type="text"
                                    value={formData.airline}
                                    onChange={(e) => setFormData({ ...formData, airline: e.target.value })}
                                    placeholder="American Airlines"
                                    className="w-full px-4 py-3 bg-gray-800/50 border border-gray-700 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:border-gray-600 focus:bg-gray-800/70 transition-all duration-200"
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="flex items-center gap-2 text-sm font-medium text-gray-300">
                                    <MapPinIcon className="w-4 h-4" />
                                    Origen
                                </label>
                                <input
                                    type="text"
                                    value={formData.origin}
                                    onChange={(e) => setFormData({ ...formData, origin: e.target.value.toUpperCase() })}
                                    placeholder="JFK"
                                    maxLength={3}
                                    className="w-full px-4 py-3 bg-gray-800/50 border border-gray-700 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:border-gray-600 focus:bg-gray-800/70 transition-all duration-200 uppercase"
                                />
                            </div>

                            <div className="space-y-2">
                                <label className="flex items-center gap-2 text-sm font-medium text-gray-300">
                                    <MapPinIcon className="w-4 h-4" />
                                    Destino
                                </label>
                                <input
                                    type="text"
                                    value={formData.destination}
                                    onChange={(e) => setFormData({ ...formData, destination: e.target.value.toUpperCase() })}
                                    placeholder="MIA"
                                    maxLength={3}
                                    className="w-full px-4 py-3 bg-gray-800/50 border border-gray-700 rounded-lg text-white placeholder-gray-500 focus:outline-none focus:border-gray-600 focus:bg-gray-800/70 transition-all duration-200 uppercase"
                                />
                            </div>
                        </div>

                        <div className="space-y-4">
                            <div className="flex items-center gap-3">
                                <div className="w-1 h-7 bg-gradient-to-b from-blue-500 to-purple-500 rounded-full" />
                                <h4 className="text-lg font-bold text-white">Tipo de Aeronave</h4>
                                <span className="text-red-400 text-sm">*</span>
                            </div>

                            <div className="grid grid-cols-3 gap-5">
                                {aircraftOptions.map((option) => (
                                    <button
                                        key={option.value}
                                        type="button"
                                        onClick={() => setFormData({ ...formData, aircraftType: option.value as AircraftType })}
                                        className={clsx(
                                            'group relative w-full p-6 rounded-2xl border-2 transition-all duration-300',
                                            'hover:scale-105 hover:-translate-y-1',
                                            formData.aircraftType === option.value
                                                ? `${option.borderColor} bg-gradient-to-br ${option.gradient} bg-opacity-10 shadow-xl`
                                                : 'border-gray-700 bg-gray-800/30 hover:border-gray-600 hover:bg-gray-800/50'
                                        )}
                                    >
                                        <div className="relative space-y-4">
                                            <div className="flex justify-center">
                                                <div className={clsx(
                                                    'w-20 h-20 rounded-2xl flex items-center justify-center transition-all duration-300',
                                                    formData.aircraftType === option.value
                                                        ? `bg-gradient-to-br ${option.gradient}`
                                                        : 'bg-gray-700/50 group-hover:bg-gray-700'
                                                )}>
                                                    <PaperAirplaneIcon className="w-12 h-12 text-white" />
                                                </div>
                                            </div>
                                            <div className="text-center space-y-1">
                                                <p className={clsx(
                                                    'font-bold text-base transition-colors',
                                                    formData.aircraftType === option.value
                                                        ? 'text-white'
                                                        : 'text-gray-300'
                                                )}>
                                                    {option.label}
                                                </p>
                                                <p className="text-xs text-gray-400">
                                                    {option.description}
                                                </p>
                                            </div>
                                        </div>

                                        {formData.aircraftType === option.value && (
                                            <div className="absolute -top-3 -right-3 w-10 h-10 bg-white rounded-full flex items-center justify-center shadow-xl">
                                                <svg className="w-6 h-6 text-green-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" />
                                                </svg>
                                            </div>
                                        )}
                                    </button>
                                ))}
                            </div>
                        </div>
                    </div>

                    <div className="px-8 py-6 bg-gray-800/30 border-t border-gray-700/50 flex gap-4">
                        <button
                            type="button"
                            onClick={onClose}
                            className="flex-1 px-6 py-3.5 bg-gray-800 hover:bg-gray-700 text-white rounded-xl transition-all duration-200 font-semibold border border-gray-700"
                            disabled={isDetecting}
                        >
                            Cancelar
                        </button>
                        <button
                            type="button"
                            onClick={handleSubmit}
                            disabled={!isFormValid || isDetecting}
                            className={clsx(
                                'flex-1 px-6 py-3.5 rounded-xl font-semibold transition-all duration-200 flex items-center justify-center gap-2',
                                isFormValid && !isDetecting
                                    ? 'bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white shadow-xl shadow-blue-500/30'
                                    : 'bg-gray-700 text-gray-500 cursor-not-allowed border border-gray-600'
                            )}
                        >
                            {isDetecting ? (
                                <>
                                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin" />
                                    <span>Detectando...</span>
                                </>
                            ) : (
                                <>
                                    <PaperAirplaneIcon className="w-5 h-5" />
                                    <span>Iniciar Detección</span>
                                </>
                            )}
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}
