import React, { useEffect, useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import TimeZoneCard from "./TimeZoneCard";
import {
  convertToLocalTime,
  getCurrentGMTTime,
  getSystemGMTOffset,
} from "../utils/dateUtils";
import TimeZoneService from "../services/TimeZoneService";
import { LanguageContext } from "../context/LanguageContext";
import { useTranslation } from "react-i18next";

const TimeZoneList = ({ classes }) => {
  const [timezones, setTimezones] = useState([]);
  const [systemTime, setSystemTime] = useState(new Date());
  const [currentTimes, setCurrentTimes] = useState({});
  const [pinId, setPinId] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { language } = useContext(LanguageContext);
  const { i18n } = useTranslation();

  // Fonction pour récupérer la liste des fuseaux horaires depuis l'API
  const fetchTimeZones = async () => {
    try {
      const response = await TimeZoneService.getAllTimeZones();
      setTimezones(response.data);
    } catch (err) {
      setError("errors.timezoneFetchError");
      console.error(err);
    }
  };

  useEffect(() => {
    fetchTimeZones();
  }, []);

  useEffect(() => {
    const updateTimeForAllTimeZones = () => {
      console.log(language)
      setSystemTime(new Date()); // Mettre à jour l'heure système locale

      const updatedTimes = {};
      const gmtBase = getCurrentGMTTime(); // Obtenir l'heure GMT actuelle

      timezones.forEach((timezone) => {
        // Calcul de l'heure locale pour chaque timezone
        const localTime = convertToLocalTime(gmtBase, timezone.offset);

        // Appliquer le format en fonction de la langue
        updatedTimes[timezone.id] =
          language === "FR"
            ? localTime.toLocaleTimeString("fr-FR")
            : localTime.toLocaleString("en-GB", {
              hour12: true,
              timeStyle: "short",
            });
      });
      setCurrentTimes(updatedTimes); // Mettre à jour l'état des heures locales
    };

    if (timezones.length > 0) {
      updateTimeForAllTimeZones();
    }


    const intervalId = setInterval(updateTimeForAllTimeZones, 1000);

    return () => clearInterval(intervalId); // Nettoyer l'intervalle à la destruction du composant
  }, [timezones, language]);

  // Fonction pour supprimer un fuseau horaire
  const handleDelete = async (id) => {
    try {
      await TimeZoneService.deleteTimeZone(id);
      setTimezones(timezones.filter((tz) => tz.id !== id));
    } catch (err) {
      setError("errors.timezoneDeleteError");
    }
  };

  // Fonction pour marquer un fuseau horaire comme épinglé
  const handlePin = (id) => {
    setPinId(id);
  };

  const sortedTimezones = [...timezones].sort((a, b) => {
    if (a.id === pinId) return -1;
    if (b.id === pinId) return 1;
    return 0;
  });

  return (
    <div className={`flex flex-col items-center ${classes.bgInvertClass} ${classes.textInvertClass} min-h-screen`}>
      <div className="flex flex-col items-center space-y-2">
        {/* Affichage de la date et l'heure du système local */}
        <span className="text-lg md:text-xl lg:text-2xl font-medium">
          {systemTime.toLocaleDateString(language === "fr" ? "fr-FR" : "en-GB", {
            day: "2-digit",
            month: "2-digit",
            year: "numeric",
          })}
        </span>

        <span className="text-4xl md:text-6xl lg:text-8xl font-bold leading-none" style={{ whiteSpace: "nowrap" }}>
          {language === "FR"
            ? systemTime.toLocaleTimeString("fr-FR")
            : systemTime.toLocaleString("en-GB", {
              hour12: true,
              timeStyle: "short",
            })  }
        </span>

        <span className="text-lg md:text-xl lg:text-2xl font-medium">
          {getSystemGMTOffset()}
        </span>
      </div>

      {error && <p className="text-red-500">{i18n.t(error)}</p>}

      <button
        className={`w-full py-2 text-white bg-black hover:bg-white hover:text-black dark:bg-white dark:text-black dark:hover:bg-black dark:hover:text-white transition duration-300 ease-in-out`}
        onClick={() => navigate("/create")}
      >
        {i18n.t("timezoneList.addNewTimezone")}
      </button>

      <ul className="w-full max-w-2xl">
        {sortedTimezones.length > 0 ? (
          sortedTimezones.map((timezone) => (
            <TimeZoneCard
              classes={classes}
              key={timezone.id}
              timezone={timezone}
              displayActionButton={true}
              currentTimes={currentTimes}
              handleDelete={handleDelete}
              handlePin={handlePin}
              isPinned={pinId === timezone.id}
            />
          ))
        ) : (
          <li className="text-center">{i18n.t("timezoneList.notAvailable")}</li>
        )}
      </ul>
    </div>
  );
};

export default TimeZoneList;
