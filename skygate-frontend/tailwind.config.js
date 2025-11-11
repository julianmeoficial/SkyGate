/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        // Estados del Autómata (S0-S6)
        automata: {
          s0: '#64748b', // Slate-500 - Sistema Inicializado
          s1: '#3b82f6', // Blue-500 - Wide Body Detectado
          s2: '#a855f7', // Purple-500 - Jumbo Detectado
          s3: '#06b6d4', // Cyan-500 - Narrow Body Detectado
          s4: '#eab308', // Yellow-500 - Gate Asignado
          s5: '#22c55e', // Green-500 - Aeronave Estacionada
          s6: '#ef4444', // Red-500 - Sin Gates (Error)
        },

        // Estados de Gates
        gate: {
          free: '#22c55e',       // Verde - Disponible
          assigned: '#eab308',   // Amarillo - Asignado
          occupied: '#ef4444',   // Rojo - Ocupado
          maintenance: '#f97316', // Naranja - Mantenimiento
          reserved: '#3b82f6',   // Azul - Reservado
        },

        // LEDs Virtuales (con brillo para animaciones)
        led: {
          green: '#22c55e',
          yellow: '#eab308',
          red: '#ef4444',
          blue: '#3b82f6',
          off: '#4b5563',
        },

        // Tipos de Aeronaves
        aircraft: {
          narrow: '#06b6d4',  // Cyan - Narrow Body
          wide: '#3b82f6',    // Blue - Wide Body
          jumbo: '#a855f7',   // Purple - Jumbo
        },

        // Tema Base (Dark Mode)
        background: {
          primary: '#0f172a',   // Slate-900
          secondary: '#1e293b', // Slate-800
          tertiary: '#334155',  // Slate-700
        },
      },

      // Animaciones personalizadas
      keyframes: {
        'pulse-led': {
          '0%, 100%': {
            opacity: '1',
            boxShadow: '0 0 10px currentColor'
          },
          '50%': {
            opacity: '0.7',
            boxShadow: '0 0 20px currentColor'
          },
        },
        'slide-in-right': {
          '0%': {
            transform: 'translateX(100%)',
            opacity: '0'
          },
          '100%': {
            transform: 'translateX(0)',
            opacity: '1'
          },
        },
        'slide-in-down': {
          '0%': {
            transform: 'translateY(-100%)',
            opacity: '0'
          },
          '100%': {
            transform: 'translateY(0)',
            opacity: '1'
          },
        },
        'fade-in': {
          '0%': { opacity: '0' },
          '100%': { opacity: '1' },
        },
        'bounce-in': {
          '0%': {
            transform: 'scale(0.3)',
            opacity: '0'
          },
          '50%': {
            transform: 'scale(1.05)'
          },
          '70%': {
            transform: 'scale(0.9)'
          },
          '100%': {
            transform: 'scale(1)',
            opacity: '1'
          },
        },
        'glow-pulse': {
          '0%, 100%': {
            boxShadow: '0 0 10px currentColor, 0 0 20px currentColor'
          },
          '50%': {
            boxShadow: '0 0 20px currentColor, 0 0 40px currentColor'
          },
        },
      },

      animation: {
        'pulse-led': 'pulse-led 2s cubic-bezier(0.4, 0, 0.6, 1) infinite',
        'slide-in-right': 'slide-in-right 0.3s ease-out',
        'slide-in-down': 'slide-in-down 0.3s ease-out',
        'fade-in': 'fade-in 0.3s ease-in',
        'bounce-in': 'bounce-in 0.6s cubic-bezier(0.68, -0.55, 0.265, 1.55)',
        'glow-pulse': 'glow-pulse 2s ease-in-out infinite',
      },

      // Sombras personalizadas para efectos de glow
      boxShadow: {
        'led-green': '0 0 15px rgba(34, 197, 94, 0.6)',
        'led-yellow': '0 0 15px rgba(234, 179, 8, 0.6)',
        'led-red': '0 0 15px rgba(239, 68, 68, 0.6)',
        'led-blue': '0 0 15px rgba(59, 130, 246, 0.6)',
        'glow-s0': '0 0 20px rgba(100, 116, 139, 0.5)',
        'glow-s1': '0 0 20px rgba(59, 130, 246, 0.5)',
        'glow-s2': '0 0 20px rgba(168, 85, 247, 0.5)',
        'glow-s3': '0 0 20px rgba(6, 182, 212, 0.5)',
        'glow-s4': '0 0 20px rgba(234, 179, 8, 0.5)',
        'glow-s5': '0 0 20px rgba(34, 197, 94, 0.5)',
        'glow-s6': '0 0 20px rgba(239, 68, 68, 0.5)',
      },

      // Gradientes personalizados
      backgroundImage: {
        'gradient-automata': 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)',
        'gradient-success': 'linear-gradient(135deg, #667eea 0%, #22c55e 100%)',
        'gradient-error': 'linear-gradient(135deg, #f093fb 0%, #ef4444 100%)',
        'gradient-dark': 'linear-gradient(135deg, #0f172a 0%, #1e293b 100%)',
      },
    },
  },
  plugins: [],
}
