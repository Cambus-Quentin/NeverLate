import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../services/api';
import TimeZoneService from '../services/TimeZoneService';

const CreateTimeZone = () => {
  const [name, setName] = useState('');
  const [city, setCity] = useState('');
  const [offset, setOffset] = useState('');
  const [error, setError] = useState(null);
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
        await TimeZoneService.createTimeZone({
            name,
            city,
            offset,
        });
      navigate('/home');
    } catch (err) {
      setError('Erreur lors de la création du fuseau horaire.');
      console.error(err);
    }
  };

  return (
    <div>
      <h2>Créer un nouveau fuseau horaire</h2>

      {error && <p style={{ color: 'red' }}>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label>Nom :</label>
          <input
            type="text"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>
        <div>
          <label>Ville :</label>
          <input
            type="text"
            value={city}
            onChange={(e) => setCity(e.target.value)}
          />
        </div>
        <div>
          <label>Décalage horaire :</label>
          <input
            type="text"
            value={offset}
            onChange={(e) => setOffset(e.target.value)}
            required
          />
        </div>
        <button type="submit">Créer</button>
      </form>
    </div>
  );
};

export default CreateTimeZone;
