import React, { useState, useEffect } from 'react';
import { Stepper, Step, StepLabel, StepContent } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import Button from '../ui/Button';
import InputField from '../ui/InputField';
import SelectDropdown from '../ui/SelectDropdown';
import Card from '../ui/Card';
import FileUploader from '../ui/FileUploader';
import { saveFormData, loadFormData, clearFormData } from '../../utils/storageUtils';
import { useAuth } from '../../context/AuthContext';
import { useNotification } from '../../context/NotificationContext';
import authAPI from '../../api/auth';

const FORM_CACHE_KEY = 'registration_form_cache';

export const RegistrationStepper = () => {
  const navigate = useNavigate();
  const { login } = useAuth();
  const { addNotification } = useNotification();
  const [activeStep, setActiveStep] = useState(0);
  const [loading, setLoading] = useState(false);
  const [formData, setFormData] = useState({
    // Step 1 - Basic Details
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
    role: 'PATIENT',
    
    // Step 2 - Role Specific
    // Doctor
    qualification: '',
    specialization: '',
    experience: '',
    hospital: '',
    bio: '',
    documents: null,
    
    // Patient
    gender: '',
    bloodGroup: '',
    dateOfBirth: '',
    address: '',
    emergencyContact: '',
  });

  const [errors, setErrors] = useState({});

  // Load cached data on mount
  useEffect(() => {
    const loadCachedData = async () => {
      const cached = await loadFormData(FORM_CACHE_KEY);
      if (cached) {
        setFormData(cached);
        addNotification('Resuming your registration...', 'info', 3000);
      }
    };
    loadCachedData();
  }, []);

  // Save form data to cache whenever it changes
  useEffect(() => {
    saveFormData(FORM_CACHE_KEY, formData);
  }, [formData]);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
    // Clear error for this field
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }));
    }
  };

  const validateStep1 = () => {
    const newErrors = {};
    if (!formData.firstName.trim()) newErrors.firstName = 'First name is required';
    if (!formData.lastName.trim()) newErrors.lastName = 'Last name is required';
    if (!formData.email.trim()) newErrors.email = 'Email is required';
    else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = 'Invalid email format';
    }
    if (!formData.password) newErrors.password = 'Password is required';
    else if (formData.password.length < 8) {
      newErrors.password = 'Password must be at least 8 characters';
    }
    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = 'Passwords do not match';
    }
    if (!formData.role) newErrors.role = 'Please select a role';

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const validateStep2 = () => {
    const newErrors = {};

    if (formData.role === 'DOCTOR') {
      if (!formData.qualification.trim()) newErrors.qualification = 'Qualification is required';
      if (!formData.specialization.trim()) newErrors.specialization = 'Specialization is required';
      if (!formData.experience) newErrors.experience = 'Years of experience is required';
      if (!formData.hospital.trim()) newErrors.hospital = 'Hospital/Clinic name is required';
      if (!formData.bio.trim()) newErrors.bio = 'Profile bio is required';
    } else if (formData.role === 'PATIENT') {
      if (!formData.gender) newErrors.gender = 'Gender is required';
      if (!formData.bloodGroup) newErrors.bloodGroup = 'Blood group is required';
      if (!formData.dateOfBirth) newErrors.dateOfBirth = 'Date of birth is required';
      if (!formData.address.trim()) newErrors.address = 'Address is required';
      if (!formData.emergencyContact.trim()) newErrors.emergencyContact = 'Emergency contact is required';
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleNext = async () => {
    if (activeStep === 0 && validateStep1()) {
      setActiveStep(1);
    } else if (activeStep === 1 && validateStep2()) {
      await handleSubmit();
    }
  };

  const handleBack = () => {
    if (activeStep > 0) {
      setActiveStep(activeStep - 1);
    }
  };

  const handleSubmit = async () => {
    setLoading(true);
    try {
      // Step 1: Register user via auth API
      const registrationData = {
        firstName: formData.firstName,
        lastName: formData.lastName,
        email: formData.email,
        password: formData.password,
        role: formData.role,
      };

      await authAPI.register(registrationData);
      addNotification('User account created successfully!', 'success');

      // Step 2: Login user
      const loginResponse = await authAPI.login(formData.email, formData.password);
      const { accessToken, refreshToken, expiresIn, user } = loginResponse.data;
      
      login(accessToken, refreshToken, user, expiresIn);
      addNotification('Logged in successfully!', 'success');

      // Step 3: Create role-specific profile
      if (formData.role === 'DOCTOR') {
        // Call doctor profile creation here
        // await doctorAPI.createProfile(doctorProfileData);
      } else if (formData.role === 'PATIENT') {
        // Call patient profile creation here
        // await patientAPI.createProfile(patientProfileData);
      }

      // Clear cache and redirect
      await clearFormData(FORM_CACHE_KEY);
      addNotification('Profile created successfully!', 'success');
      
      const redirectPath = formData.role === 'DOCTOR' ? '/doctor' : '/patient';
      navigate(redirectPath);
    } catch (error) {
      console.error('Registration error:', error);
      addNotification(
        error.response?.data?.message || 'Registration failed. Please try again.',
        'error'
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <Card className="w-full">
      <h1 className="text-3xl font-bold text-neutral-900 mb-2">Create Account</h1>
      <p className="text-neutral-600 mb-8">Join MediConnect to get started</p>

      <Stepper activeStep={activeStep} orientation="vertical">
        <Step active={activeStep === 0} completed={activeStep > 0}>
          <StepLabel>Basic Information</StepLabel>
          <StepContent>
            <div className="space-y-4 mt-4">
              <div className="grid grid-cols-2 gap-4">
                <InputField
                  label="First Name"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleInputChange}
                  error={errors.firstName}
                  required
                />
                <InputField
                  label="Last Name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleInputChange}
                  error={errors.lastName}
                  required
                />
              </div>

              <InputField
                label="Email"
                name="email"
                type="email"
                value={formData.email}
                onChange={handleInputChange}
                error={errors.email}
                required
              />

              <div className="grid grid-cols-2 gap-4">
                <InputField
                  label="Password"
                  name="password"
                  type="password"
                  value={formData.password}
                  onChange={handleInputChange}
                  error={errors.password}
                  required
                  placeholder="Min 8 characters"
                />
                <InputField
                  label="Confirm Password"
                  name="confirmPassword"
                  type="password"
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  error={errors.confirmPassword}
                  required
                />
              </div>

              <SelectDropdown
                label="I am a"
                name="role"
                value={formData.role}
                onChange={handleInputChange}
                error={errors.role}
                options={[
                  { value: 'PATIENT', label: 'Patient' },
                  { value: 'DOCTOR', label: 'Doctor' },
                ]}
                required
              />
            </div>

            <div className="flex gap-4 mt-6">
              <Button 
                onClick={handleNext}
                variant="primary"
              >
                Next
              </Button>
            </div>
          </StepContent>
        </Step>

        <Step active={activeStep === 1} completed={activeStep > 1}>
          <StepLabel>
            {formData.role === 'DOCTOR' ? 'Doctor Details' : 'Patient Details'}
          </StepLabel>
          <StepContent>
            <div className="space-y-4 mt-4">
              {formData.role === 'DOCTOR' ? (
                <>
                  <InputField
                    label="Qualification"
                    name="qualification"
                    value={formData.qualification}
                    onChange={handleInputChange}
                    error={errors.qualification}
                    placeholder="e.g., MBBS, MD"
                    required
                  />

                  <SelectDropdown
                    label="Primary Specialization"
                    name="specialization"
                    value={formData.specialization}
                    onChange={handleInputChange}
                    error={errors.specialization}
                    options={[
                      { value: 'Cardiology', label: 'Cardiology' },
                      { value: 'Neurology', label: 'Neurology' },
                      { value: 'Orthopedics', label: 'Orthopedics' },
                      { value: 'Pediatrics', label: 'Pediatrics' },
                      { value: 'General', label: 'General Practice' },
                    ]}
                    required
                  />

                  <InputField
                    label="Years of Experience"
                    name="experience"
                    type="number"
                    value={formData.experience}
                    onChange={handleInputChange}
                    error={errors.experience}
                    required
                  />

                  <InputField
                    label="Hospital/Clinic Name"
                    name="hospital"
                    value={formData.hospital}
                    onChange={handleInputChange}
                    error={errors.hospital}
                    required
                  />

                  <div>
                    <label className="block text-sm font-medium text-neutral-700 mb-2">
                      Profile Bio
                    </label>
                    <textarea
                      name="bio"
                      value={formData.bio}
                      onChange={handleInputChange}
                      rows="4"
                      className="w-full px-4 py-2 border border-neutral-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                      placeholder="Write a brief bio about yourself..."
                    />
                    {errors.bio && <p className="mt-1 text-sm text-red-600">{errors.bio}</p>}
                  </div>

                  <FileUploader
                    onFilesSelected={(files) => setFormData(prev => ({ ...prev, documents: files }))}
                    acceptedTypes="application/pdf,image/*"
                    multiple={true}
                    error={errors.documents}
                  />
                </>
              ) : (
                <>
                  <SelectDropdown
                    label="Gender"
                    name="gender"
                    value={formData.gender}
                    onChange={handleInputChange}
                    error={errors.gender}
                    options={[
                      { value: 'MALE', label: 'Male' },
                      { value: 'FEMALE', label: 'Female' },
                      { value: 'OTHER', label: 'Other' },
                    ]}
                    required
                  />

                  <SelectDropdown
                    label="Blood Group"
                    name="bloodGroup"
                    value={formData.bloodGroup}
                    onChange={handleInputChange}
                    error={errors.bloodGroup}
                    options={[
                      { value: 'O+', label: 'O+' },
                      { value: 'O-', label: 'O-' },
                      { value: 'A+', label: 'A+' },
                      { value: 'A-', label: 'A-' },
                      { value: 'B+', label: 'B+' },
                      { value: 'B-', label: 'B-' },
                      { value: 'AB+', label: 'AB+' },
                      { value: 'AB-', label: 'AB-' },
                    ]}
                    required
                  />

                  <InputField
                    label="Date of Birth"
                    name="dateOfBirth"
                    type="date"
                    value={formData.dateOfBirth}
                    onChange={handleInputChange}
                    error={errors.dateOfBirth}
                    required
                  />

                  <InputField
                    label="Address"
                    name="address"
                    value={formData.address}
                    onChange={handleInputChange}
                    error={errors.address}
                    required
                  />

                  <InputField
                    label="Emergency Contact Number"
                    name="emergencyContact"
                    type="tel"
                    value={formData.emergencyContact}
                    onChange={handleInputChange}
                    error={errors.emergencyContact}
                    required
                  />
                </>
              )}
            </div>

            <div className="flex gap-4 mt-6">
              <Button 
                onClick={handleBack}
                variant="secondary"
              >
                Back
              </Button>
              <Button 
                onClick={handleNext}
                variant="primary"
                disabled={loading}
              >
                {loading ? 'Creating Account...' : 'Complete Registration'}
              </Button>
            </div>
          </StepContent>
        </Step>
      </Stepper>
    </Card>
  );
};

export default RegistrationStepper;
