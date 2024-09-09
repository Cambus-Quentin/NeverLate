import React, { useEffect, useState, useContext } from "react";
import TimeZoneService from "../services/TimeZoneService";
import TimeZoneCard from "./TimeZoneCard";
import { LanguageContext } from "../context/LanguageContext";
import { useTranslation } from "react-i18next";

const ComparisonList = ({ timezone, classes, isEditing }) => {
  const [allTimezones, setAllTimezones] = useState([]);
  const [error, setError] = useState(false);
  const [comparisonResults, setComparisonResults] = useState([]);
  const [selectedTime, setSelectedTime] = useState('');
  const { language } = useContext(LanguageContext);
  const { i18n } = useTranslation();

  useEffect(() => {
    const fetchTimeZones = async () => {
      try {
        const response = await TimeZoneService.getAllTimeZones();
        setAllTimezones(response.data);
      } catch (err) {
        setError(true);
        console.error(err);
      }
    };

    fetchTimeZones();
  }, []);

  useEffect(() => {
    if (!isEditing) {
      calculateTimeZoneComparison();
    }
  }, [language, selectedTime, isEditing]);

  const isValidDateTime = (dateTime) => {
    // Validation format date
    const dateTimePattern = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}$/;
    return dateTimePattern.test(dateTime);
  };

  const calculateTimeZoneComparison = () => {
    if (!selectedTime || !timezone || !isValidDateTime(selectedTime)) return [];

    const baseDate = new Date(selectedTime);

    const [currentOffsetHours, currentOffsetMinutes] = timezone.offset
      .split(":")
      .map(Number);

    const currentOffsetMillis =
      currentOffsetHours * 3600000 + (currentOffsetMinutes || 0) * 60000;

    const gmtBaseDate = new Date(baseDate.getTime() - currentOffsetMillis);

    const results = allTimezones
      .filter((tz) => tz.id !== timezone.id)
      .map((tz) => {
        const [offsetHours, offsetMinutes] = tz.offset.split(":").map(Number);
        const offsetMillis =
          offsetHours * 3600000 + (offsetMinutes || 0) * 60000;
        const localDateTime = new Date(gmtBaseDate.getTime() + offsetMillis);

        // Formattage resultat
        return {
          ...tz,
          localDateTime:
            language === "FR"
              ? localDateTime.toLocaleString("fr-FR", {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour: '2-digit',
                minute: '2-digit',
              })
              : localDateTime.toLocaleString("en-GB", {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
                hour12: true,
                hour: '2-digit',
                minute: '2-digit',
              }),
        };
      });

    setComparisonResults(results);
  };

  return (
    <div>
      <div className="mt-6">
        <div className="m-auto w-[50%]">
          <h2 className="text-3xl font-bold text-center mb-6">
            {i18n.t("comparison.title")}
          </h2>
          <h3 className="font-bold mb-2">
            {i18n.t("comparison.dateSelector")}
          </h3>
          <input
            type="datetime-local"
            value={selectedTime}
            onChange={(e) => setSelectedTime(e.target.value)}
            className={`w-full mb-4 p-2 border disabled:bg-gray-500 border-gray-600 focus:outline-none focus:ring-2 focus:ring-black dark:focus:ring-white text-black`}
            disabled={isEditing}
          />
        </div>
      </div>

      {error && (
        <p className="text-red-500 text-center mb-4">
          {i18n.t("errors.timezoneFetchError")}
        </p>
      )}

      <h3 className="font-bold mt-4">{i18n.t("comparison.subtitle")}</h3>
      <ul className="mt-4">
        {comparisonResults.length > 0 && isValidDateTime(selectedTime) ? (
          comparisonResults.map((timezone) => (
            <TimeZoneCard
              key={timezone.id}
              timezone={timezone}
              currentTimes={{ [timezone.id]: timezone.localDateTime }}
              displayActionButton={false}
              handleDelete={() => { }}
              handlePin={() => { }}
              classes={classes}
              isPinned={false}
            />
          ))
        ) : (
          <li>{i18n.t('comparison.compareFormatError')}</li>
        )}
      </ul>
    </div>
  );
};

export default ComparisonList;
