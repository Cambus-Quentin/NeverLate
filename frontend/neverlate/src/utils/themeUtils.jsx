export const getClassesForMode = (isDarkMode) => ({
  bgClass: 'bg-primary dark:bg-secondary  disabled:bg-gray-700',
  bgHoverClass: 'hover:bg-secondary dark:hover:bg-primary disabled:bg-gray-700',
  bgInvertClass: 'bg-secondary dark:bg-primary disabled:bg-gray-700',
  textClass: 'text-secondary dark:text-primary',
  textInvertClass: 'text-primary dark:text-secondary',
  textHoverClass: 'hover:text-primary dark:hover:text-secondary',
  buttonClass: 'bg-primary dark:bg-secondary text-secondary dark:text-primary hover:bg-secondary dark:hover:bg-primary hover:text-primary dark:hover:text-secondary m-1 border-4 border-transparent hover:border-double hover:border-black dark:hover:border-white',
  inputClass: 'disabled:bg-gray-500 border-gray-600 focus:outline-none focus:ring-2 focus:ring-black dark:focus:ring-white text-black'
});