
import { Navigate } from 'react-router-dom';

function PrivateRoute({ children }) {
  const token = sessionStorage.getItem('jwt'); // Vérifie si le token JWT est présent
  return token ? children : <Navigate to="/login" />;
}

export default PrivateRoute;