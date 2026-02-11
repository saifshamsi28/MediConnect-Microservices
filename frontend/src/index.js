// Components
export { default as Button } from './components/ui/Button';
export { default as InputField } from './components/ui/InputField';
export { default as SelectDropdown } from './components/ui/SelectDropdown';
export { default as Card } from './components/ui/Card';
export { default as FileUploader } from './components/ui/FileUploader';
export { Toast, Loader } from './components/ui/Toast';

// Layout
export { default as MainLayout, AuthLayout } from './components/layout';
export { default as Navbar } from './components/layout/Navbar';
export { default as Sidebar } from './components/layout/Sidebar';

// Forms
export { default as LoginForm } from './components/forms/LoginForm';
export { default as RegistrationStepper } from './components/forms/RegistrationStepper';

// Context
export { AuthProvider, useAuth } from './context/AuthContext';
export { NotificationProvider, useNotification } from './context/NotificationContext';

// API
export { default as apiClient } from './api/axios';
export { default as authAPI } from './api/auth';
export { default as doctorAPI } from './api/doctors';
export { default as patientAPI } from './api/patients';
export { default as appointmentAPI } from './api/appointments';

// Routes
export { default as ProtectedRoute, PublicRoute } from './routes/ProtectedRoute';

// Utils
export * from './utils/storageUtils';
