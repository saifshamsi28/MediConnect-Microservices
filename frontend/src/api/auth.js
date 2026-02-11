import apiClient from './axios.js';

export const authAPI = {
  login: (email, password) => 
    apiClient.post('/auth/login', { email, password }),

  register: (data) => 
    apiClient.post('/auth/register', data),

  refreshToken: (refreshToken) => 
    apiClient.post('/auth/refresh', { refreshToken }),

  logout: () => 
    apiClient.post('/auth/logout'),
};

export default authAPI;
