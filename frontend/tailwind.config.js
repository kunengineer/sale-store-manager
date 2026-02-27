/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,jsx,ts,tsx}'],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        sans: [
          'system-ui',
          '-apple-system',
          '"SF Pro Text"',
          '"Segoe UI"',
          'sans-serif',
        ],
      },
      colors: {
        brand: {
          50: '#ecfdf9',
          100: '#d1faee',
          500: '#14b8a6',
          600: '#0d9488',
          700: '#0f766e',
        },
      },
      borderRadius: {
        lg: '0.75rem',
        xl: '1rem',
      },
    },
  },
  plugins: [],
}
