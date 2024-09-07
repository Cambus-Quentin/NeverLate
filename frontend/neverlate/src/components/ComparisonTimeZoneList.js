import React from 'react';

const ComparisonList = ({ selectedDate, timezone, allTimezones }) => {
/**
 * Calcule les heures locales correspondantes dans d'autres fuseaux horaires en fonction d'une date saisie.
 * @returns {Array} - Un tableau contenant les correspondances des fuseaux horaires, avec le nom, la ville et l'heure locale calculée.
 * 
 * La fonction utilise la date sélectionnée par l'utilisateur pour calculer l'heure locale dans les autres fuseaux horaires
 * en se basant sur l'offset GMT de chaque timezone.
 * 
 */
const calculateTimeZoneComparison = () => {
    if (!selectedDate || !timezone) return [];

    const baseDate = new Date(selectedDate);

    const currentOffset = parseFloat(timezone.offset);
  
    // Calcul de l'heure de base en GMT+00 en ajustant avec l'offset du fuseau actuel
    const gmtBaseDate = new Date(baseDate.getTime() - currentOffset * 3600000);
  
    // Calcul des heures correspondantes pour tous les autres fuseaux horaires
    const results = allTimezones
      .filter((tz) => tz.id !== timezone.id) // Exclure le fuseau horaire actuellement sélectionné
      .map((tz) => {
        const offset = parseFloat(tz.offset);
        const localDateTime = new Date(gmtBaseDate.getTime() + offset * 3600000); 
        return {
          name: tz.name,
          city: tz.city || 'Ville inconnue', 
          localDateTime: localDateTime.toLocaleString(),
        };
      });
  
    return results;
  };

  const comparisonResults = calculateTimeZoneComparison();

  return (
    <div>
      <h3>Correspondances dans les autres fuseaux horaires :</h3>
      <ul>
        {comparisonResults.length > 0 ? (
          comparisonResults.map((result, index) => (
            <li key={index}>
              {result.city} - {result.name} : {result.localDateTime}
            </li>
          ))
        ) : (
          <li>Aucune correspondance disponible</li>
        )}
      </ul>
    </div>
  );
};

export default ComparisonList;
