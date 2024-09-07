import axios from 'axios';
import { jwtDecode } from 'jwt-decode';

const AuthService = {
  login(username, password) {
    return axios
      .post('http://localhost:8080/api/auth/login', { username, password })
      .then((response) => {
        if (response.data.jwt) {
          sessionStorage.setItem('jwt', JSON.stringify(response.data));
        }
        return response.data;
      });
  },

  logout() {
    sessionStorage.removeItem('jwt');
  },

  getCurrentUser() {
    const token = sessionStorage.getItem('jwt');
    if (token) {
      return jwtDecode(JSON.parse(token).jwt);
    }
    return null;
  },

  getToken() {
    return sessionStorage.getItem('jwt');
  }
};

export default AuthService;
