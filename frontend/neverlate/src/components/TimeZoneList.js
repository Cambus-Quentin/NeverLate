import React, { useEffect, useState } from 'react';
import api from '../services/api';
import { useNavigate } from 'react-router-dom';
import TimeZoneCard from './TimeZoneCard';
import { convertToLocalTime, getCurrentGMTTime, getSystemGMTOffset } from '../utils/dateUtils';
import TimeZoneService from '../services/TimeZoneService';

const TimeZoneList = () => {
  const [timezones, setTimezones] = useState([]);
  const [systemTime, setSystemTime] = useState(new Date());
  const [currentTimes, setCurrentTimes] = useState({});
  const [favoriteId, setFavoriteId] = useState(null);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  // Fonction pour récupérer la liste des fuseaux horaires depuis l'API
  const fetchTimeZones = async () => {
    try {
      const response = await TimeZoneService.getAllTimeZones();
      setTimezones(response.data);
    } catch (err) {
      setError('Erreur lors de la récupération des fuseaux horaires.');
      console.error(err);
    }
  };

  useEffect(() => {
    fetchTimeZones();
  }, []);

  useEffect(() => {
    const updateTimeForAllTimeZones = () => {
      setSystemTime(new Date());

      const updatedTimes = {};
      const gmtBase = getCurrentGMTTime(); // Utiliser l'heure GMT actuelle

      timezones.forEach((timezone) => {
        const localTime = convertToLocalTime(gmtBase, timezone.offset); // Utiliser la fonction de conversion
        updatedTimes[timezone.id] = localTime.toLocaleTimeString();
      });
      setCurrentTimes(updatedTimes);
    };

    if (timezones && timezones.length > 0) {
      updateTimeForAllTimeZones();
    }

    const intervalId = setInterval(updateTimeForAllTimeZones, 1000);

    return () => clearInterval(intervalId);
  }, [timezones]);

  // Fonction pour supprimer un fuseau horaire
  const handleDelete = async (id) => {
    try {
      await TimeZoneService.deleteTimeZone(id);
      setTimezones(timezones.filter((tz) => tz.id !== id));
    } catch (err) {
      setError('Erreur lors de la suppression du fuseau horaire.');
      console.error(err);
    }
  };

  // Fonction pour marquer un fuseau horaire comme favori
  const handleFavorite = (id) => {
    setFavoriteId(id);
  };

  const sortedTimezones = [...timezones].sort((a, b) => {
    if (a.id === favoriteId) return -1;
    if (b.id === favoriteId) return 1;
    return 0;
  });

  return (
    <div>
      <h2>Heure actuelle du système : {systemTime.toLocaleTimeString()} ({getSystemGMTOffset()})</h2>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <button onClick={() => navigate('/create')}>Ajouter un nouveau fuseau horaire</button>

      <ul>
        {sortedTimezones.length > 0 ? (
          sortedTimezones.map((timezone) => (
            <TimeZoneCard
              key={timezone.id}
              timezone={timezone}
              currentTimes={currentTimes}
              handleDelete={handleDelete}
              handleFavorite={handleFavorite}
            />
          ))
        ) : (
          <li>Aucun fuseau horaire disponible</li>
        )}
      </ul>
    </div>
  );
};

export default TimeZoneList;
