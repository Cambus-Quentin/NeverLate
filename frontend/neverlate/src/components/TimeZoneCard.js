import React from 'react';
import { Link } from 'react-router-dom'; // Pour la navigation

const TimeZoneCard = ({ timezone, currentTimes, handleDelete, handleFavorite }) => {
  return (
    <div className="timezone-card" style={{ border: '1px solid #ccc', padding: '10px', marginBottom: '10px' }}>
      <Link to={`/edit/${timezone.id}`} style={{ textDecoration: 'none', color: 'black' }}>
        <strong>{timezone.city ? timezone.city : 'Ville inconnue'}</strong>
        <p>Nom : {timezone.name}</p>
        <p>Décalage horaire : {timezone.offset}</p>
        <p>Heure locale : {currentTimes[timezone.id]}</p>
      </Link>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <button onClick={() => handleFavorite(timezone.id)}>Favoris</button>
        <button onClick={() => handleDelete(timezone.id)} style={{ color: 'red' }}>
          Supprimer
        </button>
      </div>
    </div>
  );
};

export default TimeZoneCard;
