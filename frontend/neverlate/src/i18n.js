import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import LanguageDetector from "i18next-browser-languagedetector";
import en from "./locales/en.json";
import fr from "./locales/fr.json";

i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources: {
      FR: { translation: fr },
      EN: { translation: en },
    },
    fallbackLng: "FR",
    interpolation: { escapeValue: false },
  });

export default i18n;
