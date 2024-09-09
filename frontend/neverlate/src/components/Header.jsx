import React, { useContext, useEffect, useState } from "react";
import { AuthContext } from "../context/AuthContext";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom";
import { MdLogout, MdOutlineWbSunny } from "react-icons/md";
import { IoMdMoon } from "react-icons/io";
import { LanguageContext } from "../context/LanguageContext";
import { useTranslation } from "react-i18next";

function Header({ isDarkMode, toggleTheme, classes }) {
  const { currentUser, logout } = useContext(AuthContext);  // Utilisation directe du context
  const { language, setLanguage } = useContext(LanguageContext);
  const navigate = useNavigate();
  const { i18n } = useTranslation();

  const handleLanguageSwitch = (lang) => {
    const newLanguage = lang ? lang : language === "FR" ? "EN" : "FR";
    setLanguage(newLanguage);
    i18n.changeLanguage(newLanguage);
  };

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <header className={`${classes.bgClass} ${classes.textClass}`}>
      <div className="flex justify-between items-center p-4">
        <div className="flex items-center space-x-2">
          <button
            onClick={toggleTheme}
            className="p-2 transition duration-300 ease-in-out transform hover:scale-110"
          >
            {isDarkMode ? <MdOutlineWbSunny /> : <IoMdMoon />}
          </button>

          <button
            className="p-2 transition duration-300 hover:text-gray-400 sm:hidden"
            onClick={handleLanguageSwitch}
          >
            {language}
          </button>

          <div className="hidden sm:flex space-x-2">
            <button
              className={`p-2 ${language === "FR"
                ? `${classes.bgInvertClass} ${classes.textInvertClass}`
                : `${classes.bgClass} ${classes.textClass}`} hover:text-gray-400`}
              onClick={() => handleLanguageSwitch("FR")}
            >
              FR
            </button>
            <button
              className={`p-2 ${language === "EN"
                ? `${classes.bgInvertClass} ${classes.textInvertClass}`
                : `${classes.bgClass} ${classes.textClass}`} hover:text-gray-400`}
              onClick={() => handleLanguageSwitch("EN")}
            >
              EN
            </button>
          </div>
        </div>

        <Link
          to={`/`}
          className={`absolute left-1/2 transform -translate-x-1/2 text-xl font-semibold tracking-wide ${classes.textClass}`}
        >
          <h1 className="text-xl sm:text-6xl font-extrabold">NeverLate</h1>
        </Link>


        {currentUser && (
          <div className="flex items-center space-x-2">

            <span className="hidden sm:inline-block">{currentUser.sub}</span>

            <button
              onClick={handleLogout}
              className="flex items-center space-x-1 p-2 hover:text-red-600 transition duration-300"
            >
              <MdLogout className="text-2xl" />
              <span className="hidden md:inline-block">
                {i18n.t("authentication.logout")}
              </span>
            </button>
          </div>
        )}
      </div>
    </header>
  );
}

export default Header;
