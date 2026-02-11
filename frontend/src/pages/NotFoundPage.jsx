import React from 'react';
import { useNavigate } from 'react-router-dom';
import { AuthLayout } from '../components/layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { AlertCircle } from 'lucide-react';

export const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <AuthLayout>
      <Card className="text-center">
        <AlertCircle className="w-16 h-16 text-red-500 mx-auto mb-4" />
        <h1 className="text-3xl font-bold text-neutral-900 mb-2">Page Not Found</h1>
        <p className="text-neutral-600 mb-6">
          Sorry, the page you're looking for doesn't exist.
        </p>
        <Button 
          variant="primary"
          onClick={() => navigate('/')}
        >
          Go Home
        </Button>
      </Card>
    </AuthLayout>
  );
};

export default NotFoundPage;
