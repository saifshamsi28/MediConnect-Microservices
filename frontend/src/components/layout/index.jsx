import React from 'react';
import Navbar from './Navbar';
import Sidebar from './Sidebar';

export const MainLayout = ({ children, showSidebar = true }) => {
  return (
    <div className="flex h-screen bg-neutral-50">
      {showSidebar && <Sidebar isOpen={true} />}
      <div className="flex-1 flex flex-col overflow-hidden">
        <Navbar />
        <main className="flex-1 overflow-auto">
          <div className="container mx-auto px-4 py-8">
            {children}
          </div>
        </main>
      </div>
    </div>
  );
};

export const AuthLayout = ({ children }) => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-light via-primary to-primary-dark flex items-center justify-center p-4">
      <div className="w-full max-w-md">
        {children}
      </div>
    </div>
  );
};

export default MainLayout;
