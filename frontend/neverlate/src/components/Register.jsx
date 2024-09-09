import React, { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import AuthService from "../services/AuthService";
import { useTranslation } from "react-i18next";
import { AuthContext } from '../context/AuthContext';

const Register = ({ classes }) => {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [passwordStrength, setPasswordStrength] = useState(0);
  const navigate = useNavigate();
  const { i18n } = useTranslation();
  const { currentUser, setCurrentUser } = useContext(AuthContext);

  const evaluatePasswordStrength = (password) => {
    let strength = 0;
    if (password.length >= 8) strength += 25;
    if (/[A-Z]/.test(password)) strength += 25;
    if (/[a-z]/.test(password)) strength += 25;
    if (/[0-9]/.test(password)) strength += 25;
    return strength;
  };

  const handlePasswordChange = (e) => {
    const newPassword = e.target.value;
    setPassword(newPassword);
    setPasswordStrength(evaluatePasswordStrength(newPassword));
  };

  const getPasswordStrengthColor = () => {
    if (passwordStrength < 50) return "bg-red-500";
    if (passwordStrength >= 50 && passwordStrength < 75) return "bg-orange-500";
    return "bg-green-500";
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!username || !email || !password || !confirmPassword) {
      setErrorMessage(i18n.t("authentication.allFiledRequiredError"));
      return;
    }
    if (password !== confirmPassword) {
      setErrorMessage(i18n.t("authentication.passwordMismatch"));
      return;
    }
    AuthService.register(username, email, password)
      .then(() => {
        setCurrentUser(AuthService.getCurrentUser());
        navigate("/")
      })
      .catch(() =>
        setErrorMessage(i18n.t("authentication.createAccountError"))
      );
  };

  return (
    <div
      className={`flex items-center justify-center min-h-[90dvh] ${classes.bgInvertClass}`}
    >
      <div className="w-full max-w-xs">
        <form
          onSubmit={handleSubmit}
          className={`shadow-md rounded px-16 pt-6 pb-8 mb-4 ${classes.bgClass}`}
          style={{
            borderRadius: "42% 100% 43% 76% / 17% 23% 59% 35% ",
          }}
        >
          <h1
            className={`text-center text-2xl font-bold mb-6 ${classes.textClass}`}
          >
            {i18n.t("authentication.createAccount")}
          </h1>

          <div className="mb-4">
            <label
              className={`block text-sm font-bold mb-2 ${classes.textClass}`}
              htmlFor="username"
            >
              {i18n.t("authentication.username")}
            </label>
            <input
              type="text"
              id="username"
              name="username"
              className={`shadow appearance-none border rounded w-full py-2 px-3 text-black leading-tight focus:outline-none`}
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder={i18n.t("authentication.username")}
            />
            {errorMessage && !username && (
              <p className="text-red-500 text-xs italic">
                {i18n.t("errors.requiredField")}
              </p>
            )}
          </div>

          <div className="mb-4">
            <label
              className={`block text-sm font-bold mb-2 ${classes.textClass}`}
              htmlFor="email"
            >
              {i18n.t("authentication.email")}
            </label>
            <input
              type="email"
              id="email"
              name="email"
              className={`shadow appearance-none border rounded w-full py-2 px-3 text-black leading-tight focus:outline-none`}
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder={i18n.t("authentication.email")}
            />
            {errorMessage && !email && (
              <p className="text-red-500 text-xs italic">
                {i18n.t("errors.requiredField")}
              </p>
            )}
          </div>

          <div className="mb-4">
            <label
              className={`block text-sm font-bold mb-2 ${classes.textClass}`}
              htmlFor="password"
            >
              {i18n.t("authentication.password")}
            </label>
            <input
              type="password"
              id="password"
              name="password"
              className={`shadow appearance-none border rounded w-full py-2 px-3 text-black leading-tight focus:outline-none`}
              value={password}
              onChange={handlePasswordChange}
              placeholder="********"
            />
            {errorMessage && !password && (
              <p className="text-red-500 text-xs italic">
                {i18n.t("errors.requiredField")}
              </p>
            )}

            <div className="mt-2 w-full bg-gray-300 h-2 rounded-full">
              <div
                className={`${getPasswordStrengthColor()} h-2 rounded-full`}
                style={{ width: `${passwordStrength}%` }}
              />
            </div>
          </div>

          <div className="mb-6">
            <label
              className={`block text-sm font-bold mb-2 ${classes.textClass}`}
              htmlFor="confirmPassword"
            >
              {i18n.t("authentication.confirmPassword")}
            </label>
            <input
              type="password"
              id="confirmPassword"
              name="confirmPassword"
              className={`shadow appearance-none border rounded w-full py-2 px-3 text-black leading-tight focus:outline-none`}
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              placeholder="********"
            />
            {errorMessage && !confirmPassword && (
              <p className="text-red-500 text-xs italic">
                {i18n.t("errors.requiredField")}
              </p>
            )}
          </div>

          {errorMessage && (
            <p className="text-red-500 text-xs italic text-center mb-4">
              {errorMessage}
            </p>
          )}

          <div className="flex items-center justify-between">
            <button
              type="submit"
              className={`font-bold py-2 px-4 rounded w-full transition duration-300 ${classes.bgClass} ${classes.textClass} ${classes.bgHoverClass} ${classes.textHoverClass}`}
            >
              {i18n.t("authentication.register")}
            </button>
          </div>

          <div className="text-center mt-4">
            <button
              type="button"
              onClick={() => navigate("/login")}
              className={`px-4 font-bold rounded w-full transition duration-300 hover:underline ${classes.textClass}`}
            >
              {i18n.t("authentication.alreadyRegistered")}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default Register;
