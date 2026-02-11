import React, { createContext, useState, useCallback, useEffect } from 'react';

export const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [accessToken, setAccessToken] = useState(() => 
    localStorage.getItem('accessToken')
  );
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Restore session on mount
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    const userData = localStorage.getItem('user');
    if (token && userData) {
      setAccessToken(token);
      setUser(JSON.parse(userData));
    }
  }, []);

  const login = useCallback((accessToken, refreshToken, userData, expiresIn) => {
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', refreshToken);
    localStorage.setItem('user', JSON.stringify(userData));
    localStorage.setItem('tokenExpiry', Date.now() + expiresIn * 1000);
    
    setAccessToken(accessToken);
    setUser(userData);
    setError(null);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    localStorage.removeItem('user');
    localStorage.removeItem('tokenExpiry');
    
    setAccessToken(null);
    setUser(null);
  }, []);

  const updateUser = useCallback((userData) => {
    localStorage.setItem('user', JSON.stringify(userData));
    setUser(userData);
  }, []);

  const value = {
    user,
    accessToken,
    loading,
    error,
    login,
    logout,
    updateUser,
    isAuthenticated: !!accessToken,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = React.useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within AuthProvider');
  }
  return context;
};
