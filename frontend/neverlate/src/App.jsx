import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Login from './components/Login';
import TimeZoneList from './components/TimeZoneList';
import PrivateRoute from './components/PrivateRoute';
import Header from './components/Header';
import TimeZoneEdit from './components/TimeZoneEdit';
import CreateTimeZone from './components/CreateTimeZone';
import Register from './components/Register';
import './index.css';
import { getClassesForMode } from './utils/themeUtils';
import { I18nextProvider } from 'react-i18next';
import i18n from './i18n';

function App() {
  const [isDarkMode, setIsDarkMode] = useState(false);

  const toggleTheme = () => {
    setIsDarkMode(!isDarkMode);
    if (!isDarkMode) {
      document.documentElement.classList.add('dark');
    } else {
      document.documentElement.classList.remove('dark');
    }
  };
  const classes = getClassesForMode(isDarkMode);

  return (
    <div className={`min-h-screen ${classes.bgInvertClass} ${classes.textInvertClass} font-sans`}>
      <AuthProvider>
        <I18nextProvider i18n={i18n}>
          <Router>
            <Header isDarkMode={isDarkMode} toggleTheme={toggleTheme} classes={classes} />
            <Routes>
              <Route path="/login" element={<Login classes={classes} />} />
              <Route path="/register" element={<Register classes={classes} />} />
              <Route
                path="/"
                element={
                  <PrivateRoute>
                    <TimeZoneList classes={classes} />
                  </PrivateRoute>
                }
              />
              <Route
                path="/edit/:id"
                element={
                  <PrivateRoute>
                    <TimeZoneEdit classes={classes} />
                  </PrivateRoute>
                }
              />
              <Route
                path="/create"
                element={
                  <PrivateRoute>
                    <CreateTimeZone classes={classes} />
                  </PrivateRoute>
                }
              />
            </Routes>
          </Router>
        </I18nextProvider>
      </AuthProvider>
    </div>
  );
}

export default App;
