/**
 * Convertit une heure donnée (en GMT) en fonction de l'offset GMT d'un fuseau horaire.
 * @param {Date} baseTime - L'heure de base (GMT).
 * @param {string} offset - Le décalage horaire (ex: "+02:00", "-06:00").
 * @returns {Date} - L'heure locale calculée avec le décalage horaire.
 */
export const convertToLocalTime = (baseTime, offset) => {
    const parsedOffset = parseFloat(offset);
    return new Date(baseTime.getTime() + parsedOffset * 3600000);
  };
  
  /**
   * Récupère l'heure actuelle du système dans le fuseau horaire GMT.
   * @returns {Date} - L'heure actuelle en GMT.
   */
  export const getCurrentGMTTime = () => {
    const now = new Date();
    return new Date(now.getTime() + now.getTimezoneOffset() * 60000);
  };

  /**
   * Récupère l'offset de l'heure courante du systeme .
   * @returns {String} - L'offset (ex: GMT+03:00).
   */
  export const getSystemGMTOffset = () => {
    const offset = -new Date().getTimezoneOffset() / 60;
    const sign = offset >= 0 ? '+' : '-';
    const hours = Math.abs(offset).toString().padStart(2, '0');
    return `GMT${sign}${hours}:00`;
  };
  