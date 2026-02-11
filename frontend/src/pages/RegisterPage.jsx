import React from 'react';
import { AuthLayout } from '../components/layout';
import RegistrationStepper from '../components/forms/RegistrationStepper';

export const RegisterPage = () => {
  return (
    <AuthLayout>
      <RegistrationStepper />
    </AuthLayout>
  );
};

export default RegisterPage;
