# MediConnect Frontend - Complete Developer Guide

## Welcome to MediConnect Frontend

This is a production-ready React Single Page Application (SPA) for the MediConnect microservices platform. This guide covers everything you need to know to develop, deploy, and maintain this application.

## Table of Contents

1. [Quick Start](#quick-start)
2. [Architecture Overview](#architecture-overview)
3. [Key Features](#key-features)
4. [Project Structure](#project-structure)
5. [Component Guide](#component-guide)
6. [Development Workflow](#development-workflow)
7. [API Integration](#api-integration)
8. [Deployment](#deployment)
9. [Troubleshooting](#troubleshooting)
10. [Resources](#resources)

## Quick Start

### Prerequisites
- Node.js 16+
- npm 8+
- Git

### Installation

```bash
# Clone the repository
cd frontend

# Install dependencies
npm install

# Create environment file
cp .env.example .env

# Edit .env with your API endpoints
# VITE_API_BASE_URL=http://localhost:8080

# Start development server
npm run dev

# Open http://localhost:3000 in browser
```

### Production Build

```bash
# Create optimized build
npm run build

# Preview production build
npm run preview

# Output is in dist/ folder
```

## Architecture Overview

### Technology Stack

```
Frontend Layer (React 18)
├── UI Components (Tailwind CSS + Material-UI)
├── Forms & Validation
├── State Management (Context API)
└── Routing (React Router v6)

API Layer (Axios)
├── Centralized HTTP Client
├── Request/Response Interceptors
├── Token Management
└── Error Handling

Storage Layer
├── React Context (Session)
├── IndexedDB (Form Data)
└── LocalStorage (Settings)

Build & Deployment
├── Vite (Build Tool)
├── Tailwind CSS (Styling)
└── ESLint (Code Quality)
```

### Authentication Flow

```
1. User Login
   ↓
2. POST /auth/login
   ↓
3. Backend → Keycloak
   ↓
4. Return { accessToken, refreshToken }
   ↓
5. Store accessToken (Memory), refreshToken (HttpOnly Cookie)
   ↓
6. Attach token to requests
   ↓
7. If 401 → POST /auth/refresh → Silent Retry
```

### State Management

```
App Root
├── AuthProvider
│   ├── User Data
│   ├── Access Token
│   └── Auth Methods
└── NotificationProvider
    ├── Toast Queue
    └── Notification Methods
```

## Key Features

### 1. Secure Authentication
- JWT token-based authentication
- Automatic token refresh
- Session persistence
- Secure logout

### 2. Multi-Step Registration
- Beautiful Material-UI stepper
- Real-time validation
- Form data caching (survives crashes)
- Role-specific fields

### 3. Role-Based Access
- Admin dashboard
- Doctor dashboard
- Patient dashboard
- Protected routes

### 4. Beautiful UI
- Healthcare-themed colors
- Responsive design
- Smooth animations
- Dark mode ready

### 5. Form Persistence
- IndexedDB storage
- Auto-save
- Recovery on reload
- Data cleanup on submit

## Project Structure

### Detailed Breakdown

```
frontend/
│
├── src/
│   ├── api/
│   │   ├── axios.js          # Centralized HTTP client
│   │   ├── auth.js           # Auth endpoints
│   │   ├── doctors.js        # Doctor endpoints
│   │   ├── patients.js       # Patient endpoints
│   │   └── appointments.js   # Appointment endpoints
│   │
│   ├── components/
│   │   ├── ui/               # Basic UI components
│   │   │   ├── Button.jsx
│   │   │   ├── InputField.jsx
│   │   │   ├── SelectDropdown.jsx
│   │   │   ├── Card.jsx
│   │   │   ├── FileUploader.jsx
│   │   │   └── Toast.jsx
│   │   │
│   │   ├── layout/           # Layout components
│   │   │   ├── Navbar.jsx
│   │   │   ├── Sidebar.jsx
│   │   │   └── index.jsx
│   │   │
│   │   └── forms/            # Form components
│   │       ├── LoginForm.jsx
│   │       └── RegistrationStepper.jsx
│   │
│   ├── context/
│   │   ├── AuthContext.jsx   # Auth state
│   │   └── NotificationContext.jsx  # Toast state
│   │
│   ├── pages/
│   │   ├── LoginPage.jsx
│   │   ├── RegisterPage.jsx
│   │   ├── AdminDashboard.jsx
│   │   ├── AdminDoctorsPage.jsx
│   │   ├── DoctorDashboard.jsx
│   │   ├── DoctorProfilePage.jsx
│   │   ├── PatientDashboard.jsx
│   │   ├── PatientProfilePage.jsx
│   │   └── NotFoundPage.jsx
│   │
│   ├── routes/
│   │   └── ProtectedRoute.jsx  # Route protection
│   │
│   ├── utils/
│   │   ├── storageUtils.js   # IndexedDB utilities
│   │   └── helpers.js        # Helper functions
│   │
│   ├── App.jsx               # Main app with routes
│   ├── main.jsx              # React entry point
│   ├── index.css             # Global styles
│   ├── index.js              # Barrel exports
│   └── constants.js          # App constants
│
├── public/                   # Static assets
│
├── .env.example              # Environment template
├── .gitignore
├── .eslintrc.cjs
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
├── postcss.config.js
├── tsconfig.json
│
└── Docs/
    ├── README.md
    ├── SETUP.md
    ├── QUICK_REFERENCE.md
    ├── IMPLEMENTATION_SUMMARY.md
    ├── DELIVERY_CHECKLIST.md
    ├── PROJECT_OVERVIEW.md
    └── DEVELOPER_GUIDE.md
```

## Component Guide

### UI Components

#### Button
```jsx
<Button 
  variant="primary"      // primary | secondary | outline | danger
  size="md"             // sm | md | lg
  onClick={handleClick}
  disabled={false}
>
  Click Me
</Button>
```

#### InputField
```jsx
<InputField
  label="Email"
  type="email"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
  error={errors.email}
  required
  placeholder="user@example.com"
/>
```

#### SelectDropdown
```jsx
<SelectDropdown
  label="Role"
  value={role}
  onChange={(e) => setRole(e.target.value)}
  options={[
    { value: 'DOCTOR', label: 'Doctor' },
    { value: 'PATIENT', label: 'Patient' }
  ]}
  required
/>
```

#### Card
```jsx
<Card hoverable className="shadow-lg">
  <h2>Title</h2>
  <p>Content here</p>
</Card>
```

#### FileUploader
```jsx
<FileUploader
  onFilesSelected={(files) => setFiles(files)}
  acceptedTypes="image/*,application/pdf"
  maxSize={5 * 1024 * 1024}  // 5MB
  multiple={true}
/>
```

### Layout Components

#### MainLayout
```jsx
<MainLayout showSidebar={true}>
  <h1>Dashboard</h1>
  {/* Content goes here */}
</MainLayout>
```

#### AuthLayout
```jsx
<AuthLayout>
  <LoginForm />
</AuthLayout>
```

### Forms

#### RegistrationStepper
```jsx
import RegistrationStepper from './components/forms/RegistrationStepper';

<RegistrationStepper />
```

Features:
- Two-step form (Basic + Role-specific)
- Real-time validation
- Form data caching
- Automatic recovery

## Development Workflow

### 1. Starting Development

```bash
npm run dev
```

- Opens at http://localhost:3000
- Hot Module Replacement enabled
- Auto-refresh on file changes

### 2. Creating a New Component

```jsx
// File: src/components/ui/MyComponent.jsx
import React from 'react';

export const MyComponent = ({ prop1, prop2 }) => {
  return (
    <div>
      {/* Component JSX */}
    </div>
  );
};

export default MyComponent;
```

### 3. Adding a New Page

```jsx
// File: src/pages/MyPage.jsx
import React from 'react';
import { MainLayout } from '../components/layout';

export const MyPage = () => {
  return (
    <MainLayout showSidebar={true}>
      {/* Page content */}
    </MainLayout>
  );
};

export default MyPage;
```

Then add route in `src/App.jsx`:

```jsx
<Route 
  path="/mypage" 
  element={
    <ProtectedRoute requiredRole="PATIENT">
      <MyPage />
    </ProtectedRoute>
  }
/>
```

### 4. Adding API Integration

```javascript
// In src/api/mymodule.js
import apiClient from './axios.js';

export const myAPI = {
  fetchData: () => 
    apiClient.get('/api/myendpoint'),

  createData: (data) => 
    apiClient.post('/api/myendpoint', data),

  updateData: (id, data) => 
    apiClient.put(`/api/myendpoint/${id}`, data),
};

export default myAPI;
```

### 5. Using API in Component

```jsx
import { useState, useEffect } from 'react';
import myAPI from '../api/mymodule';
import { useNotification } from '../context/NotificationContext';

const MyComponent = () => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const { addNotification } = useNotification();

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => {
    setLoading(true);
    try {
      const response = await myAPI.fetchData();
      setData(response.data);
    } catch (error) {
      addNotification(error.message, 'error');
    } finally {
      setLoading(false);
    }
  };

  return <div>{/* Render data */}</div>;
};
```

### 6. Styling Components

```jsx
// Use Tailwind classes
<div className="flex items-center justify-between gap-4 p-4 bg-primary text-white rounded-lg">
  <h1 className="text-xl font-bold">Title</h1>
  <Button>Action</Button>
</div>

// Or use custom classes
<div className="glass gradient-primary">
  Custom styling
</div>
```

### 7. Form Handling

```jsx
import { useState, useEffect } from 'react';
import { saveFormData, loadFormData } from '../utils/storageUtils';

const CACHE_KEY = 'my_form_cache';

const MyForm = () => {
  const [formData, setFormData] = useState({
    field1: '',
    field2: '',
  });

  // Load cached data on mount
  useEffect(() => {
    loadFormData(CACHE_KEY).then(data => {
      if (data) setFormData(data);
    });
  }, []);

  // Save on every change
  useEffect(() => {
    saveFormData(CACHE_KEY, formData);
  }, [formData]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  return (
    <form>
      {/* Form fields */}
    </form>
  );
};
```

## API Integration

### Expected Backend Endpoints

```
Authentication:
POST   /auth/login          # Login
POST   /auth/register       # Register
POST   /auth/refresh        # Refresh token
POST   /auth/logout         # Logout

Doctors:
POST   /api/doctors         # Create profile
GET    /api/doctors         # List all
GET    /api/doctors/me      # Get own profile
GET    /api/doctors/{id}    # Get by ID
PUT    /api/doctors/{id}    # Update profile
POST   /api/doctors/schedule    # Create schedule
GET    /api/doctors/schedule    # Get schedule

Patients:
POST   /api/patients        # Create profile
GET    /api/patients        # List all
GET    /api/patients/me     # Get own profile
GET    /api/patients/{id}   # Get by ID
PUT    /api/patients/{id}   # Update profile

Appointments:
POST   /api/appointments    # Create
GET    /api/appointments    # List
GET    /api/appointments/{id}   # Get one
PUT    /api/appointments/{id}   # Update
POST   /api/appointments/{id}/cancel  # Cancel
```

### Error Handling

```javascript
import { getErrorMessage } from '../utils/helpers';

try {
  const response = await apiCall();
} catch (error) {
  const message = getErrorMessage(error);
  addNotification(message, 'error');
}
```

## Deployment

### Building

```bash
npm run build
```

Creates optimized production build in `dist/` folder.

### Vercel (Recommended)

1. Push code to GitHub
2. Connect repo to Vercel
3. Set environment variables
4. Deploy automatically

### AWS S3 + CloudFront

```bash
npm run build
aws s3 sync dist/ s3://your-bucket/
cloudfront invalidate --distribution-id YOUR_ID --paths "/*"
```

### Docker

```dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM node:18-alpine
WORKDIR /app
RUN npm install -g serve
COPY --from=build /app/dist ./dist
CMD ["serve", "-s", "dist"]
```

## Troubleshooting

### Issue: 401 Unauthorized Errors
**Cause**: Access token expired or invalid
**Solution**: 
- Clear browser cache
- Delete cookies
- Login again

### Issue: CORS Errors
**Cause**: Backend CORS not configured
**Solution**:
- Check backend CORS settings
- Verify API_BASE_URL in .env
- Ensure credentials sent with requests

### Issue: Form Data Lost
**Cause**: IndexedDB not supported or disabled
**Solution**:
- Check browser console
- Enable IndexedDB
- Check privacy settings

### Issue: Styles Not Applying
**Cause**: Tailwind CSS not compiled
**Solution**:
- Run `npm install`
- Restart dev server
- Clear browser cache

### Issue: Routes Not Found
**Cause**: Routes not defined in App.jsx
**Solution**:
- Check route definitions
- Verify component imports
- Check spelling

## Resources

### Documentation
- [README.md](./README.md) - Feature overview
- [SETUP.md](./SETUP.md) - Setup & deployment
- [QUICK_REFERENCE.md](./QUICK_REFERENCE.md) - Command reference
- [IMPLEMENTATION_SUMMARY.md](./IMPLEMENTATION_SUMMARY.md) - What was built

### External
- [React Documentation](https://react.dev)
- [React Router](https://reactrouter.com)
- [Tailwind CSS](https://tailwindcss.com)
- [Material-UI](https://mui.com)
- [Axios](https://axios-http.com)

## Support & Contact

For questions or issues:
1. Check documentation files
2. Review component examples
3. Check browser console
4. Check backend logs

---

**Version**: 1.0.0  
**Last Updated**: February 2026  
**Status**: Production Ready
