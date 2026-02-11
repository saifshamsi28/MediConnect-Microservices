import React from 'react';
import { Link, useLocation } from 'react-router-dom';
import { 
  LayoutDashboard, 
  Users, 
  Calendar, 
  Settings,
  FileText,
  LogOut
} from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

export const Sidebar = ({ isOpen = true }) => {
  const { user } = useAuth();
  const location = useLocation();

  const getMenuItems = () => {
    const base = {
      ADMIN: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/admin' },
        { icon: Users, label: 'Doctors', path: '/admin/doctors' },
        { icon: Users, label: 'Patients', path: '/admin/patients' },
        { icon: Calendar, label: 'Appointments', path: '/admin/appointments' },
        { icon: FileText, label: 'Reports', path: '/admin/reports' },
      ],
      DOCTOR: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/doctor' },
        { icon: Calendar, label: 'Schedule', path: '/doctor/schedule' },
        { icon: Calendar, label: 'Appointments', path: '/doctor/appointments' },
        { icon: Settings, label: 'Profile', path: '/doctor/profile' },
      ],
      PATIENT: [
        { icon: LayoutDashboard, label: 'Dashboard', path: '/patient' },
        { icon: Calendar, label: 'Appointments', path: '/patient/appointments' },
        { icon: Users, label: 'Doctors', path: '/patient/doctors' },
        { icon: Settings, label: 'Profile', path: '/patient/profile' },
      ],
    };

    return base[user?.role] || [];
  };

  const menuItems = getMenuItems();
  const isActive = (path) => location.pathname === path;

  if (!isOpen) return null;

  return (
    <aside className="w-64 bg-white border-r border-neutral-200 min-h-screen">
      <nav className="p-4 space-y-2">
        {menuItems.map((item) => {
          const Icon = item.icon;
          return (
            <Link
              key={item.path}
              to={item.path}
              className={`flex items-center gap-3 px-4 py-3 rounded-lg transition-all ${
                isActive(item.path)
                  ? 'bg-primary text-white shadow-medium'
                  : 'text-neutral-600 hover:bg-neutral-50'
              }`}
            >
              <Icon className="w-5 h-5" />
              <span className="font-medium">{item.label}</span>
            </Link>
          );
        })}
      </nav>
    </aside>
  );
};

export default Sidebar;
