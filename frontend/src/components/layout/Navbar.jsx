import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { Menu, X, LogOut, User } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';
import Button from '../ui/Button';

export const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const getRolePath = () => {
    if (user?.role === 'ADMIN') return '/admin';
    if (user?.role === 'DOCTOR') return '/doctor';
    if (user?.role === 'PATIENT') return '/patient';
    return '/';
  };

  return (
    <nav className="bg-white border-b border-neutral-200 shadow-soft sticky top-0 z-40">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          {/* Logo */}
          <Link to={getRolePath()} className="flex items-center gap-2">
            <div className="w-8 h-8 bg-primary rounded-lg flex items-center justify-center">
              <span className="text-white font-bold">M</span>
            </div>
            <span className="font-bold text-lg text-neutral-900 hidden sm:inline">
              MediConnect
            </span>
          </Link>

          {/* Desktop Menu */}
          <div className="hidden md:flex items-center gap-4">
            {user && (
              <>
                <Link 
                  to={getRolePath()}
                  className="text-neutral-600 hover:text-primary transition-colors"
                >
                  Dashboard
                </Link>
                <div className="flex items-center gap-3 ml-4 pl-4 border-l border-neutral-200">
                  <div className="text-right">
                    <p className="text-sm font-medium text-neutral-900">
                      {user?.firstName} {user?.lastName}
                    </p>
                    <p className="text-xs text-neutral-500">{user?.role}</p>
                  </div>
                  <div className="w-8 h-8 bg-primary-light rounded-full flex items-center justify-center text-white font-semibold">
                    {user?.firstName?.charAt(0)}
                  </div>
                </div>
                <Button 
                  variant="outline" 
                  size="sm"
                  onClick={handleLogout}
                  className="flex items-center gap-2"
                >
                  <LogOut className="w-4 h-4" />
                  Logout
                </Button>
              </>
            )}
          </div>

          {/* Mobile Menu Button */}
          <button
            onClick={() => setIsOpen(!isOpen)}
            className="md:hidden text-neutral-600 hover:text-primary"
          >
            {isOpen ? <X className="w-6 h-6" /> : <Menu className="w-6 h-6" />}
          </button>
        </div>

        {/* Mobile Menu */}
        {isOpen && user && (
          <div className="md:hidden pb-4 space-y-3 border-t border-neutral-200 pt-4">
            <Link 
              to={getRolePath()}
              className="block text-neutral-600 hover:text-primary transition-colors"
            >
              Dashboard
            </Link>
            <div className="flex items-center gap-2 p-2 bg-neutral-50 rounded-lg">
              <div className="w-8 h-8 bg-primary-light rounded-full flex items-center justify-center text-white font-semibold">
                {user?.firstName?.charAt(0)}
              </div>
              <div className="text-right">
                <p className="text-sm font-medium text-neutral-900">
                  {user?.firstName} {user?.lastName}
                </p>
                <p className="text-xs text-neutral-500">{user?.role}</p>
              </div>
            </div>
            <Button 
              variant="outline" 
              size="sm"
              onClick={handleLogout}
              className="w-full flex items-center justify-center gap-2"
            >
              <LogOut className="w-4 h-4" />
              Logout
            </Button>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
