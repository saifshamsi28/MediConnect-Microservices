# MediConnect Frontend - Setup & Deployment Guide

## Quick Start

### 1. Installation

```bash
# Navigate to frontend directory
cd frontend

# Install dependencies
npm install
```

### 2. Environment Configuration

Copy `.env.example` to `.env` and configure:

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_API_GATEWAY_URL=http://localhost:8080
VITE_AUTH_SERVICE_URL=http://localhost:8080/auth
```

### 3. Run Development Server

```bash
npm run dev
```

Visit `http://localhost:3000` in your browser.

## Architecture Overview

### Authentication Flow

1. **Login**
   - User enters email/password
   - Frontend calls `POST /auth/login`
   - Backend verifies with Keycloak
   - Returns: `{ accessToken, refreshToken, expiresIn, user }`

2. **Token Storage**
   - Access token → React Context (memory)
   - Refresh token → HttpOnly Cookie (backend-managed)

3. **Token Refresh**
   - 401 response triggers `POST /auth/refresh`
   - New token fetched automatically
   - Original request retried silently

4. **Logout**
   - Frontend calls `POST /auth/logout`
   - Tokens cleared from memory and cookies
   - User redirected to login

### State Management

**AuthContext**
- User data
- Access token (memory)
- Authentication state
- Login/logout methods

**NotificationContext**
- Toast notifications
- Error/success messages
- Auto-dismiss notifications

### Form Data Persistence

**Multi-step registration form uses IndexedDB:**
- Auto-saves on field change
- Survives page refresh
- Survives browser crash
- User can resume from where they left off
- Cleared on successful submission

### API Integration

All HTTP requests use centralized Axios instance:

**Request Interceptor:**
- Attaches Authorization header
- Includes access token from context

**Response Interceptor:**
- Catches 401 errors
- Attempts token refresh
- Retries failed request
- Redirects to login if refresh fails

## Component Hierarchy

```
App (Routes)
├── AuthLayout
│   ├── LoginPage
│   └── RegisterPage (RegistrationStepper)
└── MainLayout
    ├── Navbar
    ├── Sidebar
    └── Protected Pages
        ├── AdminDashboard
        ├── DoctorDashboard
        └── PatientDashboard
```

## Key Components

### Button
- Variants: primary, secondary, outline, danger
- Sizes: sm, md, lg
- States: disabled, loading

### InputField
- Label and error display
- Real-time validation
- Focus ring styling
- Support for all input types

### SelectDropdown
- Searchable options
- Error handling
- Required field indicator

### FileUploader
- Drag-and-drop support
- File validation
- Progress indication
- Multi-file support

### Toast/Snackbar
- Auto-dismiss
- Multiple notification types
- Queue management

### RegistrationStepper
- Material-UI Stepper component
- Multi-step form
- Step validation
- Form data caching
- Role-based conditional fields

## Styling System

### Color Palette

```javascript
{
  primary: '#0F766E',
  'primary-light': '#14B8A6',
  'primary-dark': '#0D5E5E',
  secondary: '#06B6D4',
  accent: '#F59E0B',
  neutral: {...}  // Grayscale palette
}
```

### Shadow System

- `shadow-soft`: Subtle (hover states)
- `shadow-medium`: Medium (elevated cards)
- `shadow-lg`: Large (modals, dialogs)

### Typography

- Font: Inter (system fallback)
- Heading: Bold, tracking-tight
- Body: Regular, leading-relaxed
- Code: Fira Code monospace

## API Endpoints Integration

### Authentication
```javascript
POST /auth/login
POST /auth/register
POST /auth/refresh
POST /auth/logout
```

### Doctors
```javascript
POST /api/doctors                    // Create profile
GET /api/doctors/me                  // Get own profile
GET /api/doctors                     // List all
GET /api/doctors/{id}                // Get by ID
PUT /api/doctors/{id}                // Update profile
POST /api/doctors/schedule           // Create schedule
GET /api/doctors/schedule            // Get schedule
POST /api/doctors/availability       // Create availability
GET /api/doctors/availability        // Get availability
```

### Patients
```javascript
POST /api/patients                   // Create profile
GET /api/patients/me                 // Get own profile
GET /api/patients                    // List all
GET /api/patients/{id}               // Get by ID
PUT /api/patients/{id}               // Update profile
```

### Appointments
```javascript
POST /api/appointments               // Create appointment
GET /api/appointments                // List user appointments
GET /api/appointments/{id}           // Get appointment details
PUT /api/appointments/{id}           // Update appointment
POST /api/appointments/{id}/cancel   // Cancel appointment
GET /api/appointments/slots          // Get available slots
```

## Build & Deployment

### Production Build

```bash
npm run build
```

Creates optimized build in `dist/` folder.

### Deployment to Vercel

1. Push code to GitHub
2. Connect repository to Vercel
3. Set environment variables in Vercel dashboard
4. Deploy automatically on push

```env
VITE_API_BASE_URL=https://api.mediconnect.com
VITE_API_GATEWAY_URL=https://gateway.mediconnect.com
VITE_AUTH_SERVICE_URL=https://gateway.mediconnect.com/auth
```

### Docker Deployment

```dockerfile
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
RUN npm run build

FROM node:18-alpine
WORKDIR /app
RUN npm install -g serve
COPY --from=build /app/dist ./dist
CMD ["serve", "-s", "dist", "-l", "3000"]
```

## Performance Optimization

1. **Code Splitting**: React lazy loading for pages
2. **Bundle Analysis**: Monitor bundle size
3. **Image Optimization**: Use next-gen formats
4. **Caching**: Service workers for offline
5. **CDN**: Serve static assets from CDN

## Security Considerations

1. **HTTPS Only**: Always use HTTPS in production
2. **CORS**: Properly configured on backend
3. **XSS Prevention**: React's built-in escaping
4. **CSRF**: Token-based requests
5. **Input Validation**: Client and server-side
6. **Error Handling**: Don't expose sensitive info

## Troubleshooting

### 401 Unauthorized
- Refresh token may be expired
- Clear browser cookies and login again
- Check backend refresh token endpoint

### CORS Errors
- Verify backend CORS configuration
- Check API_BASE_URL in .env
- Ensure credentials are sent with requests

### Form Data Not Persisting
- Check browser IndexedDB support
- Verify localStorage is not disabled
- Check browser console for errors

### Styling Issues
- Ensure Tailwind CSS is properly built
- Check for CSS class conflicts
- Verify postcss configuration

## Testing

### Unit Tests (Future)
```bash
npm run test
```

### E2E Tests (Future)
```bash
npm run test:e2e
```

## Contributing

1. Create feature branch from `main`
2. Make changes and test thoroughly
3. Run linter: `npm run lint`
4. Submit pull request with description

## License

MIT - See LICENSE file
