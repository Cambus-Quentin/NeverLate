import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import TimeZoneService from "../services/TimeZoneService";
import { useTranslation } from "react-i18next";

const CreateTimeZone = ({ classes }) => {
  const [label, setLabel] = useState("");
  const [city, setCity] = useState("");
  const [offset, setOffset] = useState("");
  const [error, setError] = useState(false);
  const [labelError, setLabelError] = useState(false);
  const [offsetError, setOffsetError] = useState(false);
  const navigate = useNavigate();
  const { i18n } = useTranslation();

  const validateForm = () => {
    // Format label (min: 3, max: 100)
    const labelIncorrect = label.length < 3 || label.length > 100;
    setLabelError(labelIncorrect);
    let isValid = !labelIncorrect;

    // Format: +/-HH:MM
    const offsetPattern = /^(?:\+|-)[0-9]{2}:[0-9]{2}$/;
    setOffsetError(!offsetPattern.test(offset));

    return isValid && offsetPattern.test(offset);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    if (validateForm()) {
      try {
        await TimeZoneService.createTimeZone({
          label,
          city,
          offset,
        });
        navigate("/");
      } catch (err) {
        setError(i18n.t("errors.timezoneCreateError"));
        console.error(err);
      }
    }
  };

  return (
    <div
      className={`min-h-screen flex items-center justify-center ${classes.bgInvertClass} ${classes.textInvertClass}`}
    >
      <div
        className={`w-full max-w-md p-8 border-4 border-black dark:border-white border-double ${classes.bgInvertClass} ${classes.textInvertClass}`}
      >
        <h2 className="text-3xl font-bold text-center mb-6">
          {i18n.t("timezoneForm.createTitle")}
        </h2>

        {error && (
          <p className="text-red-500 text-center mb-4">
            {i18n.t("errors.timezoneCreateError")}
          </p>
        )}

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label
              className={`block font-bold mb-2 ${classes.textInvertClass}`}
            >
              {i18n.t("timezoneForm.name")}
            </label>
            <input
              type="text"
              value={label}
              onChange={(e) => setLabel(e.target.value)}
              className={`w-full p-2 border ${classes.inputClass}`}
              required
              placeholder={i18n.t("timezoneForm.namePlaceholder")}
            />
            {labelError && (
              <p className="text-red-500 text-sm mt-1">
                {i18n.t("errors.nameValidationError")}
              </p>
            )}
          </div>

          <div>
            <label
              className={`block font-bold mb-2 ${classes.textInvertClass}`}
            >
              {i18n.t("timezoneForm.city")}
            </label>
            <input
              type="text"
              value={city}
              onChange={(e) => setCity(e.target.value)}
              className={`w-full p-2 border ${classes.inputClass}`}
              placeholder={i18n.t("timezoneForm.cityPlaceholder")}
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
              value={offset}
              onChange={(e) => setOffset(e.target.value)}
              className={`w-full p-2 border mb-4 ${classes.inputClass}`}
              required
              placeholder={i18n.t("timezoneForm.offsetPlaceholder")}
            />
            {offsetError && (
              <p className="text-red-500 text-sm mt-1">
                {i18n.t("errors.offsetValidationError")}
              </p>
            )}
          </div>

          <button
            className={`w-full p-3 font-bold ${classes.buttonClass}  transition duration-300`}
          >
            {i18n.t("timezoneForm.create")}
          </button>
        </form>
      </div>
    </div>
  );
};

export default CreateTimeZone;
