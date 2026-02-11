# MediConnect Frontend

A production-ready React SPA for the MediConnect Microservices platform. Built with React, Vite, Tailwind CSS, and Material-UI.

## Features

- **Secure Authentication**: Token-based auth with refresh mechanism
- **Role-Based Access**: Separate dashboards for Admin, Doctor, and Patient
- **Multi-Step Registration**: Beautiful stepper form with data caching and recovery
- **Responsive Design**: Mobile-first, works seamlessly on all devices
- **Modern UI**: Clean, minimal aesthetic with healthcare-friendly colors
- **Form Persistence**: IndexedDB-based caching prevents data loss
- **Global State Management**: Context API for auth and notifications
- **API Integration**: Centralized Axios instance with interceptors

## Tech Stack

- **React 18** - UI framework
- **React Router v6** - Client-side routing
- **Vite** - Build tool and dev server
- **Tailwind CSS** - Utility-first styling
- **Material-UI** - Interactive components
- **Axios** - HTTP client
- **IndexedDB** - Local storage for forms

## Project Structure

```
src/
├── api/                    # API client functions
│   ├── axios.js           # Axios instance with interceptors
│   ├── auth.js            # Authentication endpoints
│   ├── doctors.js         # Doctor service endpoints
│   ├── patients.js        # Patient service endpoints
│   └── appointments.js    # Appointment endpoints
├── components/
│   ├── ui/                # Reusable UI components
│   │   ├── Button.jsx
│   │   ├── InputField.jsx
│   │   ├── SelectDropdown.jsx
│   │   ├── Card.jsx
│   │   ├── FileUploader.jsx
│   │   └── Toast.jsx
│   ├── layout/            # Layout components
│   │   ├── Navbar.jsx
│   │   ├── Sidebar.jsx
│   │   └── index.jsx
│   └── forms/             # Form components
│       ├── LoginForm.jsx
│       └── RegistrationStepper.jsx
├── context/               # Global state management
│   ├── AuthContext.jsx
│   └── NotificationContext.jsx
├── pages/                 # Page components
│   ├── LoginPage.jsx
│   ├── RegisterPage.jsx
│   ├── AdminDashboard.jsx
│   ├── DoctorDashboard.jsx
│   ├── PatientDashboard.jsx
│   └── NotFoundPage.jsx
├── routes/                # Route protection
│   └── ProtectedRoute.jsx
├── utils/                 # Utility functions
│   └── storageUtils.js
├── App.jsx               # Main app component
├── main.jsx              # Entry point
└── index.css             # Global styles
```

## Getting Started

### Prerequisites
- Node.js 16+
- npm or yarn

### Installation

1. Clone the repository:
```bash
cd frontend
npm install
```

2. Create `.env` file:
```bash
cp .env.example .env
```

3. Update `.env` with your API endpoints:
```env
VITE_API_BASE_URL=http://localhost:8080
```

### Development

Start the development server:
```bash
npm run dev
```

The app will be available at `http://localhost:3000`

### Building

Build for production:
```bash
npm run build
```

Preview the build:
```bash
npm run preview
```

## Key Features Explained

### 1. Secure Authentication

- Access token stored in memory (React Context)
- Refresh token stored in HttpOnly cookie (backend-managed)
- Automatic token refresh on 401 response
- Silent refresh without user interruption

### 2. Multi-Step Registration

The registration form uses Material-UI Stepper and includes:

**Step 1 - Basic Details:**
- First/Last Name
- Email
- Password confirmation
- Role selection (Doctor/Patient)

**Step 2 - Role-Specific:**

*Doctor:*
- Qualification
- Specialization
- Years of experience
- Hospital/Clinic
- Profile bio
- Document upload

*Patient:*
- Gender
- Blood group
- Date of birth
- Address
- Emergency contact

### 3. Form Data Caching

- Uses IndexedDB for form data persistence
- Auto-saves on every field change
- Automatic recovery on page reload
- Clear cache on successful submission

### 4. Role-Based Dashboards

**Admin Dashboard:**
- View and approve doctors
- Manage users
- Appointment analytics
- System health indicators

**Doctor Dashboard:**
- Profile and availability management
- Schedule management
- Appointment queue
- Patient consultation history

**Patient Dashboard:**
- Browse and book appointments
- View medical history
- Manage prescriptions
- Track consultations

## API Integration

All API calls go through the centralized Axios instance with automatic token management:

```javascript
// Example: Login
const response = await authAPI.login(email, password);
const { accessToken, refreshToken, user } = response.data;
login(accessToken, refreshToken, user, expiresIn);

// Example: Create doctor profile
await doctorAPI.createProfile(doctorData);

// Example: Book appointment
await appointmentAPI.createAppointment(appointmentData);
```

## Styling

The app uses Tailwind CSS with a custom color system:

- **Primary**: Teal (#0F766E) - Main brand color
- **Secondary**: Cyan (#06B6D4) - Accent color
- **Accent**: Amber (#F59E0B) - Highlight color
- **Neutrals**: Full grayscale palette

All styles are utility-first with Tailwind, and component-specific styles use CSS-in-JS where needed.

## Error Handling

- Global error handling with toast notifications
- Form validation with inline error messages
- API error interceptors with automatic retry
- Network error recovery

## Browser Support

- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

MIT
