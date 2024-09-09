import React, { useState, useEffect, useContext } from 'react';
import { useNavigate } from 'react-router-dom';
import AuthService from '../services/AuthService';
import { AuthContext } from '../context/AuthContext';
import { useTranslation } from 'react-i18next';

const Login = ({ classes }) => {
  const { currentUser, setCurrentUser } = useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();
  const { i18n } = useTranslation();

  useEffect(() => {
    if (currentUser) {
      navigate('/');
    }
  }, [currentUser, navigate]);

  const handleSubmit = (e) => {
    e.preventDefault();

    if (!username || !password) {
      setErrorMessage(i18n.t('authentication.allFiledRequiredError'));
      return;
    }

    AuthService.login(username, password)
      .then(() => {
        setCurrentUser(AuthService.getCurrentUser());
        navigate('/');  // Redirection après connexion réussie
      })
      .catch(() => {
        setErrorMessage(i18n.t('authentication.loginError'));
      });
  };

  return (
    <div
      className={`${classes.bgClass} ${classes.textClass} shadow-md mx-auto my-10 px-16 pt-6 pb-8`}
      style={{
        borderRadius: '43% 89% 40% 81% / 73% 47% 85% 31%',
        maxWidth: '400px',
        width: '100%',
        height: 'auto',
      }}
    >
      <h1 className="text-center text-2xl font-bold mb-6">{i18n.t('authentication.login')}</h1>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label
            className={`block text-sm font-bold mb-2 ${classes.textClass}`}
            htmlFor="username"
          >
            {i18n.t('authentication.username')}
          </label>
          <input
            type="text"
            id="username"
            name="username"
            className={`w-full py-2 px-3 border text-black rounded focus:outline-none`}
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder={i18n.t('authentication.usernamePaceHolder')}
          />
          {errorMessage && !username && (
            <p className="text-red-500 text-xs italic">{i18n.t('errors.requiredField')}</p>
          )}
        </div>

        <div>
          <label
            className={`block text-sm font-bold mb-2 ${classes.textClass}`}
            htmlFor="password"
          >
            {i18n.t('authentication.password')}
          </label>
          <input
            type="password"
            id="password"
            name="password"
            className={`w-full py-2 px-3 border text-black rounded focus:outline-none`}
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="********"
          />
          {errorMessage && !password && (
            <p className="text-red-500 text-xs italic">{i18n.t('errors.requiredField')}</p>
          )}
        </div>

        {errorMessage && (
          <p className="text-red-500 text-xs italic text-center mb-4">{errorMessage}</p>
        )}

        <div className="flex justify-center">
          <button
            type="submit"
            className={`${classes.textClass} ${classes.bgClass} ${classes.bgHoverClass} ${classes.textHoverClass} font-bold py-2 px-4 rounded w-full transition duration-300`}
          >
            {i18n.t('authentication.login')}
          </button>
        </div>

        <div className="text-center mt-4">
          <button
            type="button"
            onClick={() => navigate('/register')}
            className={`
            px-4 font-bold rounded w-full transition duration-300 hover:underline`}
          >
            {i18n.t('authentication.register')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default Login;
