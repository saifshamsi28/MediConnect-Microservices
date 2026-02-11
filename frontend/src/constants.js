// Test credentials for development
export const TEST_ACCOUNTS = {
  ADMIN: {
    email: 'admin@mediconnect.com',
    password: 'admin@123456',
    role: 'ADMIN'
  },
  DOCTOR: {
    email: 'doctor@mediconnect.com',
    password: 'doctor@123456',
    role: 'DOCTOR'
  },
  PATIENT: {
    email: 'patient@mediconnect.com',
    password: 'patient@123456',
    role: 'PATIENT'
  }
};

// API Error Messages
export const ERROR_MESSAGES = {
  INVALID_CREDENTIALS: 'Invalid email or password',
  USER_EXISTS: 'User with this email already exists',
  INVALID_EMAIL: 'Please enter a valid email address',
  PASSWORD_MISMATCH: 'Passwords do not match',
  NETWORK_ERROR: 'Network error. Please check your connection',
  SERVER_ERROR: 'Server error. Please try again later',
  UNAUTHORIZED: 'Your session has expired. Please login again',
  FORBIDDEN: 'You do not have permission to access this resource',
};

// Form Validation Rules
export const VALIDATION_RULES = {
  PASSWORD_MIN_LENGTH: 8,
  PASSWORD_PATTERN: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]/,
  EMAIL_PATTERN: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  PHONE_PATTERN: /^[+]?[(]?[0-9]{3}[)]?[-\s.]?[0-9]{3}[-\s.]?[0-9]{4,6}$/,
};

// UI Constants
export const UI = {
  NOTIFICATION_DURATION: 5000,
  LOADER_DELAY: 300,
  ANIMATION_DURATION: 300,
};

// Role Permissions
export const ROLES = {
  ADMIN: 'ADMIN',
  DOCTOR: 'DOCTOR',
  PATIENT: 'PATIENT',
};

export const ROLE_PERMISSIONS = {
  ADMIN: ['manage_doctors', 'manage_patients', 'view_analytics', 'system_settings'],
  DOCTOR: ['view_appointments', 'manage_schedule', 'view_patients', 'edit_profile'],
  PATIENT: ['book_appointments', 'view_doctors', 'edit_profile', 'view_history'],
};
