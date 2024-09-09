module.exports = {
  content: [
    './src/**/*.{js,jsx,ts,tsx}',
  ],
  darkMode: 'class',
  theme: {
    extend: {
      fontFamily: {
        sans: ['Rubik', 'sans-serif'],  //
      },
      colors: {
        primary: {
          DEFAULT: '#000000',
          light: '#ffffff',  
        },
        secondary: {
          DEFAULT: '#ffffff',
          light: '#000000',
        },
      },
    },
  },
  plugins: [],
}
