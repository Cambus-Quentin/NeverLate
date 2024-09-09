import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import TimeZoneService from "../services/TimeZoneService";
import ComparisonList from "./ComparisonTimeZoneList";
import { useTranslation } from "react-i18next";

const TimeZoneEdit = ({ classes }) => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [timezone, setTimezone] = useState(null);
  const [isEditing, setIsEditing] = useState(false);
  const [error, setError] = useState(null);
  const [labelError, setLabelError] = useState(false);
  const [offsetError, setOffsetError] = useState(false);
  const { i18n } = useTranslation();

  const fetchTimeZone = async () => {
    try {
      const response = await TimeZoneService.getTimeZoneById(id);
      setTimezone(response.data);
    } catch (err) {
      setError("errors.timezoneFetchError");
      console.error(err);
    }
  };

  useEffect(() => {
    fetchTimeZone();
  }, [id]);

  // Suppression timezone
  const handleDelete = async () => {
    try {
      await TimeZoneService.deleteTimeZone(id);
      navigate("/");
    } catch (err) {
      setError("errors.timezoneDeleteError");
      console.error(err);
    }
  };

  // Sauvegarde timezone
  const handleSave = async () => {
    if (validateForm()) {
      try {
        await TimeZoneService.updateTimeZone(id, timezone);
        setIsEditing(false);
      } catch (err) {
        setError("errors.timezoneUpdateError");
        console.error(err);
      }
    }
  };

  const validateForm = () => {
    // format (min: 3, max: 100)
    const labelIncorrect =
      timezone.label.length < 3 || timezone.label.length > 100;
    let isValid = !labelIncorrect;
    setLabelError(labelIncorrect);

    // format: +/-HH:MM
    const offsetPattern = /^(?:\+|-)[0-9]{2}:[0-9]{2}$/;
    setOffsetError(!offsetPattern.test(timezone.offset));

    return isValid && offsetPattern.test(timezone.offset);
  };

  const toggleEdit = () => {
    setIsEditing(!isEditing);
  };

  return (
    <div
      className={`min-h-screen flex items-center justify-center ${classes.bgInvertClass} ${classes.textInvertClass}`}
    >
      <div
        className={`w-full p-8 m-16  border-4 border-black dark:border-white border-double`}
      >
        <h2 className="text-3xl font-bold text-center mb-6">
          {i18n.t("timezoneForm.editTitle")}
        </h2>

        {error && (
          <p className="text-red-500 text-center mb-4">{i18n.t(error)}</p>
        )}
        {timezone ? (
          <div className="space-y-4">
            <div className="max-w-[50%] m-auto justify-center place-self-center flex flex-col">
              <div>
                <label
                  className={`block font-bold mb-2 ${classes.textInvertClass}`}
                >
                  {i18n.t("timezoneForm.name")}
                </label>
                <input
                  type="text"
                  value={timezone.label}
                  onChange={(e) =>
                    setTimezone({ ...timezone, label: e.target.value })
                  }
                  className={`w-full p-2 border ${classes.inputClass}`}
                  disabled={!isEditing}
                />
              </div>
              {labelError && (
                <p className="text-red-500 text-center mb-4">
                  {i18n.t("errors.nameValidationError")}
                </p>
              )}

              <div>
                <label
                  className={`block font-bold mb-2 ${classes.textInvertClass}`}
                >
                  {i18n.t("timezoneForm.city")}
                </label>
                <input
                  type="text"
                  value={timezone.city}
                  onChange={(e) =>
                    setTimezone({ ...timezone, city: e.target.value })
                  }
                  className={`w-full p-2 border ${classes.inputClass}`}
                  disabled={!isEditing}
                />
              </div>

              <div>
                <label
                  className={`block font-bold mb-2 ${classes.textInvertClass}`}
                >
                  {i18n.t("timezoneForm.offset")}
                </label>
                <input
                  type="text"
                  value={timezone.offset}
                  onChange={(e) =>
                    setTimezone({ ...timezone, offset: e.target.value })
                  }
                  className={`w-full p-2 border ${classes.inputClass}`}
                  disabled={!isEditing}
                />
              </div>

              {offsetError && (
                <p className="text-red-500 text-center mb-4">
                  {i18n.t("errors.offsetValidationError")}
                </p>
              )}

              <div className="flex justify-between flex-col sm:flex-row ">
                {isEditing ? (
                  <button
                    onClick={handleSave}
                    className={`px-4 py-2 ${classes.buttonClass}`}
                  >
                    {i18n.t("timezoneForm.save")}
                  </button>
                ) : (
                  <button
                    onClick={toggleEdit}
                    className={`px-4 py-2 ${classes.buttonClass}`}
                  >
                    {i18n.t("timezoneForm.edit")}
                  </button>
                )}
                <button
                  onClick={handleDelete}
                  className="px-4 py-2 bg-red-500 text-white hover:bg-red-700 transition duration-300 m-1 border-4 border-transparent hover:border-double hover:border-black dark:hover:border-white"
                >
                  {i18n.t("timezoneForm.delete")}
                </button>
              </div>
            </div>
            <ComparisonList
              timezone={timezone}
              classes={classes}
              isEditing={isEditing}
            />
          </div>
        ) : (
          <p>Chargement du fuseau horaire...</p>
        )}
      </div>
    </div>
  );
};

export default TimeZoneEdit;
