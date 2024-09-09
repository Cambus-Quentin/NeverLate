
import { Navigate } from 'react-router-dom';

function PrivateRoute({ children }) {
  const token = sessionStorage.getItem('jwt');
  return token ? children : <Navigate to="/login" />;
}

export default PrivateRoute;