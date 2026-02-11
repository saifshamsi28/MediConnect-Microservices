import apiClient from './axios.js';

export const doctorAPI = {
  createProfile: (doctorData) => 
    apiClient.post('/api/doctors', doctorData),

  getProfile: () => 
    apiClient.get('/api/doctors/me'),

  updateProfile: (doctorId, data) => 
    apiClient.put(`/api/doctors/${doctorId}`, data),

  getDoctors: (filters = {}) => 
    apiClient.get('/api/doctors', { params: filters }),

  getDoctorById: (doctorId) => 
    apiClient.get(`/api/doctors/${doctorId}`),

  createSchedule: (scheduleData) => 
    apiClient.post('/api/doctors/schedule', scheduleData),

  getSchedule: () => 
    apiClient.get('/api/doctors/schedule'),

  createAvailability: (availabilityData) => 
    apiClient.post('/api/doctors/availability', availabilityData),

  getAvailability: () => 
    apiClient.get('/api/doctors/availability'),
};

export default doctorAPI;
