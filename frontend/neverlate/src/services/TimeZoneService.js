import api from './api';

const API_URL = "http://localhost:8080/api/timezones";

const TimeZoneService = {
  getAllTimeZones() {
    return api.get(API_URL);
  },

  getTimeZoneById(id) {
    return api.get(`${API_URL}/${id}`);
  },

  createTimeZone(data) {
    return api.post(API_URL, data);
  },

  updateTimeZone(id, data) {
    return api.put(`${API_URL}/${id}`, data);
  },

  deleteTimeZone(id) {
    return api.delete(`${API_URL}/${id}`);
  }
};

export default TimeZoneService;
