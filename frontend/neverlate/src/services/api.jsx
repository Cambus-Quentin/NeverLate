import axios from 'axios';
import AuthService from './AuthService';

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
});

api.interceptors.request.use(
  (config) => {
    const token = AuthService.getToken();
    if (token) {
      const parsedToken = JSON.parse(token);
      config.headers['Authorization'] = 'Bearer ' + parsedToken.jwt;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default api;
