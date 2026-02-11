import React from 'react';
import { Link } from 'react-router-dom';
import { Loader } from '../ui/Toast';

export const LoadingPage = () => {
  return (
    <div className="min-h-screen bg-gradient-to-br from-primary-light via-primary to-primary-dark flex items-center justify-center">
      <Loader open={true} message="Initializing MediConnect..." />
    </div>
  );
};

export default LoadingPage;
