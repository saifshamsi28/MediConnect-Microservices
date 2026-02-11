import apiClient from './axios.js';

export const appointmentAPI = {
  createAppointment: (appointmentData) => 
    apiClient.post('/api/appointments', appointmentData),

  getAppointments: (filters = {}) => 
    apiClient.get('/api/appointments', { params: filters }),

  getAppointmentById: (appointmentId) => 
    apiClient.get(`/api/appointments/${appointmentId}`),

  updateAppointment: (appointmentId, data) => 
    apiClient.put(`/api/appointments/${appointmentId}`, data),

  cancelAppointment: (appointmentId) => 
    apiClient.post(`/api/appointments/${appointmentId}/cancel`),

  getAvailableSlots: (doctorId, date) => 
    apiClient.get(`/api/appointments/slots`, { 
      params: { doctorId, date } 
    }),
};

export default appointmentAPI;
