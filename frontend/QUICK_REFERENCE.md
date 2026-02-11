# MediConnect Frontend - Quick Reference

## Installation & Running

```bash
# Install dependencies
npm install

# Create .env file
cp .env.example .env

# Start development server
npm run dev

# Build for production
npm run build

# Preview production build
npm run preview
```

## Project Structure Quick Reference

```
src/
├── api/              # API client functions
├── components/       # Reusable React components
├── context/          # Global state (Auth, Notifications)
├── pages/            # Page components
├── routes/           # Route protection
├── utils/            # Helper utilities
├── App.jsx           # Main app with routes
└── index.css         # Global styles
```

## Key Files & Their Purpose

| File | Purpose |
|------|---------|
| `src/App.jsx` | Routes, auth provider, notification setup |
| `src/context/AuthContext.jsx` | User state, login/logout, session |
| `src/api/axios.js` | Token attachment, 401 refresh, retry logic |
| `src/components/forms/RegistrationStepper.jsx` | Multi-step registration with caching |
| `src/components/layout/Navbar.jsx` | Top navigation bar |
| `src/components/layout/Sidebar.jsx` | Role-based navigation |
| `.env` | API endpoints configuration |

## Common Tasks

### Add New Page
1. Create file in `src/pages/PageName.jsx`
2. Import in `App.jsx`
3. Add route in `App.jsx` with `<ProtectedRoute>`

### Add New API Endpoint
1. Add method in `src/api/moduleName.js`
2. Use `apiClient.get/post/put/delete()`
3. Import in component and use

### Add New Component
1. Create file in `src/components/ui/ComponentName.jsx`
2. Export component
3. Import where needed

### Modify Colors
1. Edit `tailwind.config.js` - colors section
2. Update CSS variables in `src/index.css`
3. Apply with Tailwind classes

## Testing Credentials (Development)

```
Admin:
  Email: admin@mediconnect.com
  Password: admin@123456

Doctor:
  Email: doctor@mediconnect.com
  Password: doctor@123456

Patient:
  Email: patient@mediconnect.com
  Password: patient@123456
```

## Environment Variables

```env
VITE_API_BASE_URL=http://localhost:8080
VITE_API_GATEWAY_URL=http://localhost:8080
VITE_AUTH_SERVICE_URL=http://localhost:8080/auth
```

## Component Props Examples

### Button
```jsx
<Button variant="primary" size="md" onClick={handleClick}>
  Click Me
</Button>
```

### InputField
```jsx
<InputField
  label="Email"
  type="email"
  value={email}
  onChange={(e) => setEmail(e.target.value)}
  error={errors.email}
  required
/>
```

### Card
```jsx
<Card hoverable className="custom-class">
  Content here
</Card>
```

## API Usage Examples

### Login
```javascript
import { authAPI } from './api/auth';

const response = await authAPI.login(email, password);
const { accessToken, refreshToken, user } = response.data;
```

### Create Doctor Profile
```javascript
import { doctorAPI } from './api/doctors';

const profile = await doctorAPI.createProfile(doctorData);
```

### Get Appointments
```javascript
import { appointmentAPI } from './api/appointments';

const appointments = await appointmentAPI.getAppointments({
  status: 'UPCOMING'
});
```

## State Management Examples

### Using Auth Context
```javascript
import { useAuth } from './context/AuthContext';

const MyComponent = () => {
  const { user, logout, isAuthenticated } = useAuth();
  return <div>{user?.firstName}</div>;
};
```

### Using Notifications
```javascript
import { useNotification } from './context/NotificationContext';

const MyComponent = () => {
  const { addNotification } = useNotification();
  
  const handleSave = async () => {
    try {
      // ... save logic
      addNotification('Saved successfully!', 'success');
    } catch (error) {
      addNotification(error.message, 'error');
    }
  };
};
```

## Routing Examples

### Protected Route
```jsx
<Route 
  path="/admin" 
  element={
    <ProtectedRoute requiredRole="ADMIN">
      <AdminDashboard />
    </ProtectedRoute>
  }
/>
```

### Public Route
```jsx
<Route 
  path="/login" 
  element={
    <PublicRoute>
      <LoginPage />
    </PublicRoute>
  }
/>
```

## Form Data Caching

### Save Form Data
```javascript
import { saveFormData } from './utils/storageUtils';

const formData = { name: 'John', email: 'john@example.com' };
await saveFormData('my_form_cache', formData);
```

### Load Form Data
```javascript
import { loadFormData } from './utils/storageUtils';

const cachedData = await loadFormData('my_form_cache');
if (cachedData) {
  setFormData(cachedData);
}
```

## Styling with Tailwind

### Common Classes
```jsx
// Layout
<div className="flex items-center justify-between gap-4">

// Colors
<div className="bg-primary text-white">

// Spacing
<div className="p-4 m-2">

// Responsive
<div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3">

// States
<button className="hover:bg-primary-dark focus:ring-2">
```

## Troubleshooting Quick Fixes

| Issue | Solution |
|-------|----------|
| 401 Errors | Login again, token may have expired |
| CORS Errors | Check backend CORS config and API_BASE_URL |
| Styles not loading | Run `npm install`, restart dev server |
| Form data lost | Check browser IndexedDB support |
| Routes not working | Verify route paths in App.jsx |

## Performance Tips

- Use React.memo for expensive components
- Implement code splitting with React.lazy
- Optimize images before upload
- Use production build for deployment
- Monitor bundle size with npm run build

## Security Reminders

- Never commit `.env` file
- Always use HTTPS in production
- Validate input on client AND server
- Keep dependencies updated
- Use environment variables for secrets
- Implement rate limiting on backend

## Useful Commands

```bash
npm run dev          # Start dev server
npm run build        # Create production build
npm run preview      # Preview production build
npm run lint         # Run ESLint
npm install          # Install dependencies
npm update           # Update dependencies
npm audit            # Check security vulnerabilities
```

## Resources

- [React Docs](https://react.dev)
- [React Router Docs](https://reactrouter.com)
- [Tailwind CSS Docs](https://tailwindcss.com)
- [Material-UI Docs](https://mui.com)
- [Axios Docs](https://axios-http.com)

## Support

1. Check SETUP.md for detailed setup guide
2. Review README.md for feature overview
3. Check component examples in src/
4. Review browser console for errors
5. Check backend API logs for issues

---

**Quick Start**: `npm install && npm run dev`
**Production Build**: `npm run build`
**Documentation**: See README.md, SETUP.md, IMPLEMENTATION_SUMMARY.md
