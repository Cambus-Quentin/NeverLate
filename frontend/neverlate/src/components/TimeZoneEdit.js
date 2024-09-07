import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import TimeZoneService from '../services/TimeZoneService'; // Utiliser TimeZoneService pour gérer les appels API
import { convertToLocalTime, getSystemGMTOffset } from '../utils/dateUtils';

const TimeZoneEdit = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [timezone, setTimezone] = useState(null);
  const [selectedDate, setSelectedDate] = useState('');
  const [comparisonResults, setComparisonResults] = useState([]);
  const [isEditing, setIsEditing] = useState(false);
  const [error, setError] = useState(null);

  // Récupération du fuseau horaire par ID via TimeZoneService
  const fetchTimeZone = async () => {
    try {
      const response = await TimeZoneService.getTimeZoneById(id); // Utilise TimeZoneService pour récupérer un timezone
      setTimezone(response.data);
    } catch (err) {
      setError('Erreur lors de la récupération du fuseau horaire.');
      console.error(err);
    }
  };

  useEffect(() => {
    fetchTimeZone();
  }, [id]);

  // Fonction pour gérer la suppression du fuseau horaire via TimeZoneService
  const handleDelete = async () => {
    try {
      await TimeZoneService.deleteTimeZone(id); // Utilise TimeZoneService pour supprimer un timezone
      navigate('/home');
    } catch (err) {
      setError('Erreur lors de la suppression du fuseau horaire.');
      console.error(err);
    }
  };

  // Fonction pour gérer la soumission de la modification du fuseau horaire via TimeZoneService
  const handleSave = async () => {
    try {
      await TimeZoneService.updateTimeZone(id, timezone); // Utilise TimeZoneService pour mettre à jour un timezone
      setIsEditing(false);
      navigate('/home');
    } catch (err) {
      setError('Erreur lors de la sauvegarde du fuseau horaire.');
      console.error(err);
    }
  };

  const toggleEdit = () => {
    setIsEditing(!isEditing);
  };

  /**
   * Calcule les correspondances des heures locales dans d'autres fuseaux horaires à partir d'une date sélectionnée.
   */
  const calculateTimeZoneComparison = async () => {
    try {
      const response = await TimeZoneService.getAllTimeZones(); // Utilise TimeZoneService pour obtenir tous les fuseaux horaires
      const comparisonTimeZones = response.data.filter((tz) => tz.id !== timezone.id); // Exclure le fuseau horaire actuel

      const baseDate = new Date(selectedDate); // Date entrée par l'utilisateur
      const currentOffset = parseFloat(timezone.offset); // Décalage horaire du timezone actuel
      const gmtBaseDate = new Date(baseDate.getTime() - currentOffset * 3600000); // Convertir en GMT+00

      // Calculer l'heure locale dans les autres fuseaux horaires
      const results = comparisonTimeZones.map((tz) => {
        const localDateTime = convertToLocalTime(gmtBaseDate, tz.offset);
        return {
          name: tz.name,
          city: tz.city || 'Ville inconnue',
          localDateTime: localDateTime.toLocaleString(),
        };
      });

      setComparisonResults(results);
    } catch (err) {
      setError('Erreur lors du calcul des fuseaux horaires.');
      console.error(err);
    }
  };

  return (
    <div>
      <h2>Éditer le fuseau horaire</h2>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      {timezone ? (
        <div>
          <div>
            <label>
              Nom :
              <input
                type="text"
                value={timezone.name}
                onChange={(e) => setTimezone({ ...timezone, name: e.target.value })}
                disabled={!isEditing} // Désactive le champ si non en mode édition
              />
            </label>
          </div>
          <div>
            <label>
              Ville :
              <input
                type="text"
                value={timezone.city}
                onChange={(e) => setTimezone({ ...timezone, city: e.target.value })}
                disabled={!isEditing} // Désactive le champ si non en mode édition
              />
            </label>
          </div>
          <div>
            <label>
              Décalage horaire (par rapport à GMT) :
              <input
                type="text"
                value={timezone.offset}
                onChange={(e) => setTimezone({ ...timezone, offset: e.target.value })}
                disabled={!isEditing} // Désactive le champ si non en mode édition
              />
            </label>
          </div>

          {isEditing ? (
            <button onClick={handleSave}>Sauvegarder</button>
          ) : (
            <button onClick={toggleEdit}>Modifier</button>
          )}

          <button onClick={handleDelete} style={{ color: 'red', marginLeft: '10px' }}>
            Supprimer
          </button>

          <div>
            <h3>Saisir une date et heure :</h3>
            <input
              type="datetime-local"
              value={selectedDate}
              onChange={(e) => setSelectedDate(e.target.value)}
              disabled={isEditing}
            />
            <button onClick={calculateTimeZoneComparison} disabled={isEditing}>
              Convertir
            </button>
          </div>

          <h3>Correspondances dans les autres fuseaux horaires :</h3>
          <ul>
            {comparisonResults.map((result, index) => (
              <li key={index}>
                {result.city} - {result.name} : {result.localDateTime}
              </li>
            ))}
          </ul>
        </div>
      ) : (
        <p>Chargement du fuseau horaire...</p>
      )}
    </div>
  );
};

export default TimeZoneEdit;
