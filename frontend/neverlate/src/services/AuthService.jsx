import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const AuthService = {

  login(username, password) {
    return axios
      .post('http://localhost:8080/api/auth/login', { username, password })
      .then((response) => {
        if (response.data.jwt) {
          sessionStorage.setItem('jwt', JSON.stringify(response.data));
          return jwtDecode(response.data.jwt); // Retourne les infos de l'utilisateur
        }
      });
  },

  logout() {
    sessionStorage.removeItem('jwt');
  },

  getCurrentUser() {
    const token = sessionStorage.getItem('jwt');
    if (token) {
      return jwtDecode(JSON.parse(token).jwt);  // Décoder le token pour obtenir l'utilisateur
    }
    return null;
  },

  getToken() {
    return sessionStorage.getItem('jwt');
  },

  register(username, email, password) {
    return axios
      .post('http://localhost:8080/api/auth/register', { username, email, password })
      .then((response) => {
        if (response.data.jwt) {
          sessionStorage.setItem('jwt', JSON.stringify(response.data));
          return jwtDecode(response.data.jwt); // Retourne les infos de l'utilisateur
        }
      });
  },
};

export default AuthService;
