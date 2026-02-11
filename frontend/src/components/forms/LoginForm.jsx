import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import Button from '../ui/Button';
import InputField from '../ui/InputField';
import Card from '../ui/Card';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import authAPI from '../../api/auth';

export const LoginForm = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { addNotification } = useNotification();
  const [loading, setLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.email.trim()) newErrors.email = 'Email is required';
    if (!formData.password) newErrors.password = 'Password is required';
    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);
    try {
      const response = await authAPI.login(formData.email, formData.password);
      const { accessToken, refreshToken, expiresIn, user } = response.data;
      
      login(accessToken, refreshToken, user, expiresIn);
      addNotification(`Welcome back, ${user.firstName}!`, 'success');
      
      const redirectPath = user.role === 'ADMIN' ? '/admin' 
                         : user.role === 'DOCTOR' ? '/doctor' 
                         : '/patient';
      navigate(redirectPath);
    } catch (error) {
      console.error('Login error:', error);
      const message = error.response?.data?.message || 'Login failed. Please check your credentials.';
      addNotification(message, 'error');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="w-full">
      <div className="text-center mb-8">
        <h1 className="text-3xl font-bold text-neutral-900 mb-2">Welcome Back</h1>
        <p className="text-neutral-600">Sign in to your MediConnect account</p>
      </div>

      <form onSubmit={handleSubmit} className="space-y-4">
        <InputField
          label="Email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleInputChange}
          error={errors.email}
          placeholder="you@example.com"
          required
        />

        <InputField
          label="Password"
          name="password"
          type="password"
          value={formData.password}
          onChange={handleInputChange}
          error={errors.password}
          placeholder="••••••••"
          required
        />

        <Button 
          type="submit"
          variant="primary"
          className="w-full"
          disabled={loading}
        >
          {loading ? 'Signing in...' : 'Sign In'}
        </Button>
      </form>

      <p className="text-center text-neutral-600 mt-6">
        Don't have an account?{' '}
        <Link to="/register" className="text-primary font-semibold hover:text-primary-dark">
          Create one
        </Link>
      </p>
    </Card>
  );
};

export default LoginForm;
