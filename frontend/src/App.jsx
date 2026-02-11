import React, { Suspense } from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { NotificationProvider } from './context/NotificationContext';
import ProtectedRoute, { PublicRoute } from './routes/ProtectedRoute';
import Toast, { Loader } from './components/ui/Toast';

// Pages
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import AdminDashboard from './pages/AdminDashboard';
import AdminDoctorsPage from './pages/AdminDoctorsPage';
import DoctorDashboard from './pages/DoctorDashboard';
import DoctorProfilePage from './pages/DoctorProfilePage';
import PatientDashboard from './pages/PatientDashboard';
import PatientProfilePage from './pages/PatientProfilePage';
import NotFoundPage from './pages/NotFoundPage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <NotificationProvider>
          <Suspense fallback={<Loader open={true} message="Loading..." />}>
            <Routes>
              {/* Public Routes */}
              <Route 
                path="/login" 
                element={
                  <PublicRoute>
                    <LoginPage />
                  </PublicRoute>
                }
              />
              <Route 
                path="/register" 
                element={
                  <PublicRoute>
                    <RegisterPage />
                  </PublicRoute>
                }
              />

              {/* Protected Routes */}
              {/* Admin Routes */}
              <Route 
                path="/admin" 
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <AdminDashboard />
                  </ProtectedRoute>
                }
              />
              <Route 
                path="/admin/doctors" 
                element={
                  <ProtectedRoute requiredRole="ADMIN">
                    <AdminDoctorsPage />
                  </ProtectedRoute>
                }
              />

              {/* Doctor Routes */}
              <Route 
                path="/doctor" 
                element={
                  <ProtectedRoute requiredRole="DOCTOR">
                    <DoctorDashboard />
                  </ProtectedRoute>
                }
              />
              <Route 
                path="/doctor/profile" 
                element={
                  <ProtectedRoute requiredRole="DOCTOR">
                    <DoctorProfilePage />
                  </ProtectedRoute>
                }
              />

              {/* Patient Routes */}
              <Route 
                path="/patient" 
                element={
                  <ProtectedRoute requiredRole="PATIENT">
                    <PatientDashboard />
                  </ProtectedRoute>
                }
              />
              <Route 
                path="/patient/profile" 
                element={
                  <ProtectedRoute requiredRole="PATIENT">
                    <PatientProfilePage />
                  </ProtectedRoute>
                }
              />

              {/* Default Route - redirect based on auth */}
              <Route 
                path="/" 
                element={
                  <ProtectedRoute>
                    <Navigate to="/patient" replace />
                  </ProtectedRoute>
                }
              />
              <Route 
                path="/dashboard" 
                element={
                  <ProtectedRoute>
                    <Navigate to="/patient" replace />
                  </ProtectedRoute>
                }
              />
              
              {/* 404 Route */}
              <Route path="*" element={<NotFoundPage />} />
            </Routes>
          </Suspense>
          <Toast />
        </NotificationProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
