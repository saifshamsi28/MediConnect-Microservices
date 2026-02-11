# MediConnect Frontend - Project Overview

## Quick Links

- **Documentation**: See [SETUP.md](./SETUP.md) for detailed setup guide
- **README**: See [README.md](./README.md) for feature overview

## What's Included

This is a production-ready React SPA frontend for the MediConnect microservices platform.

### Features
- Secure JWT authentication with token refresh
- Multi-step registration with form data persistence
- Role-based dashboards (Admin, Doctor, Patient)
- Responsive mobile-first design
- Real-time error handling and notifications
- IndexedDB form data caching
- Global state management with Context API

### Technology Stack
- React 18 with Hooks
- React Router v6
- Vite build tool
- Tailwind CSS
- Material-UI components
- Axios HTTP client
- IndexedDB for storage

### Project Structure
```
frontend/
├── src/
│   ├── api/                    # API integration layer
│   ├── components/             # Reusable React components
│   │   ├── ui/                # Basic UI components
│   │   ├── layout/            # Layout components
│   │   └── forms/             # Form components
│   ├── context/               # Global state (Auth, Notifications)
│   ├── pages/                 # Page components
│   ├── routes/                # Route protection logic
│   ├── utils/                 # Helper utilities
│   ├── App.jsx                # Main app with routing
│   ├── main.jsx               # React entry point
│   └── index.css              # Global styles
├── public/                    # Static assets
├── package.json               # Dependencies
├── vite.config.js            # Vite configuration
├── tailwind.config.js        # Tailwind configuration
├── tsconfig.json             # TypeScript config
├── .env.example              # Environment template
├── README.md                 # Feature documentation
└── SETUP.md                  # Setup guide

```

## Getting Started

### 1. Install dependencies
```bash
npm install
```

### 2. Configure environment
```bash
cp .env.example .env
# Edit .env with your API endpoints
```

### 3. Start dev server
```bash
npm run dev
```

### 4. Build for production
```bash
npm run build
```

## Key Features Explained

### Authentication Flow
1. User logs in → `POST /auth/login`
2. Receives access token (memory) + refresh token (HttpOnly cookie)
3. Token attached to all requests
4. On 401 → Automatic refresh → Retry request
5. User never sees the token management

### Form Persistence
- All form data auto-saved to IndexedDB
- Survives page refresh and browser crash
- User can resume from where they left off
- Automatically cleared on successful submission

### Role-Based Access
- Automatic route protection
- Different dashboards for each role
- Menu items filtered by permissions
- Conditional component rendering

## API Integration

All API requests go through centralized Axios instance:

```javascript
// Example: Login
const response = await authAPI.login(email, password);

// Example: Create doctor profile
await doctorAPI.createProfile(data);

// Example: Book appointment
await appointmentAPI.createAppointment(data);
```

## Styling

- Tailwind CSS for utilities
- Material-UI for complex components
- Custom color system (teal primary, cyan secondary)
- Responsive mobile-first design
- Dark mode ready (extend if needed)

## Troubleshooting

**Issue**: 401 Unauthorized errors
- Solution: Refresh token may be expired, login again

**Issue**: CORS errors
- Solution: Check backend CORS configuration and API_BASE_URL in .env

**Issue**: Form data not persisting
- Solution: Check browser IndexedDB support, verify localStorage not disabled

**Issue**: Styles not applying
- Solution: Ensure `npm install` completed, check tailwind.config.js

## Contributing

1. Create feature branch
2. Make changes following component structure
3. Test thoroughly
4. Submit PR with description

## Deployment

### Vercel (Recommended)
1. Push code to GitHub
2. Connect repo to Vercel
3. Set environment variables
4. Auto-deploy on push

### Docker
See Dockerfile in project for containerization

### Manual
```bash
npm run build
# Upload dist/ folder to hosting
```

## Support

For issues or questions:
1. Check SETUP.md documentation
2. Review component examples
3. Check browser console for errors
4. Review backend API logs

## License

MIT
