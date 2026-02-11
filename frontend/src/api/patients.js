import apiClient from './axios.js';

export const patientAPI = {
  createProfile: (patientData) => 
    apiClient.post('/api/patients', patientData),

  getProfile: () => 
    apiClient.get('/api/patients/me'),

  updateProfile: (patientId, data) => 
    apiClient.put(`/api/patients/${patientId}`, data),

  getPatients: (filters = {}) => 
    apiClient.get('/api/patients', { params: filters }),

  getPatientById: (patientId) => 
    apiClient.get(`/api/patients/${patientId}`),
};

export default patientAPI;
