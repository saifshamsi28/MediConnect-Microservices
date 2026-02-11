import React, { useState } from 'react';
import { MainLayout } from '../components/layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import InputField from '../components/ui/InputField';
import { Settings, Edit2, Save, X } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const DoctorProfilePage = () => {
  const { user, updateUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    specialization: 'Cardiology',
    experience: '5',
    hospital: 'City Hospital',
    bio: 'Experienced cardiologist with 5 years of practice.',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSave = () => {
    // Call API to update profile
    updateUser({
      ...user,
      ...formData,
    });
    setIsEditing(false);
  };

  return (
    <MainLayout showSidebar={true}>
      <div className="space-y-8">
        <div>
          <h1 className="text-3xl font-bold text-neutral-900">Doctor Profile</h1>
          <p className="text-neutral-600 mt-2">Manage your professional information</p>
        </div>

        {/* Profile Card */}
        <Card>
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-bold text-neutral-900">Professional Information</h2>
            {!isEditing ? (
              <Button 
                variant="outline"
                size="sm"
                onClick={() => setIsEditing(true)}
                className="flex items-center gap-2"
              >
                <Edit2 className="w-4 h-4" />
                Edit
              </Button>
            ) : (
              <div className="flex gap-2">
                <Button 
                  variant="primary"
                  size="sm"
                  onClick={handleSave}
                  className="flex items-center gap-2"
                >
                  <Save className="w-4 h-4" />
                  Save
                </Button>
                <Button 
                  variant="secondary"
                  size="sm"
                  onClick={() => setIsEditing(false)}
                  className="flex items-center gap-2"
                >
                  <X className="w-4 h-4" />
                  Cancel
                </Button>
              </div>
            )}
          </div>

          {isEditing ? (
            <div className="space-y-4">
              <div className="grid grid-cols-2 gap-4">
                <InputField
                  label="First Name"
                  name="firstName"
                  value={formData.firstName}
                  onChange={handleChange}
                />
                <InputField
                  label="Last Name"
                  name="lastName"
                  value={formData.lastName}
                  onChange={handleChange}
                />
              </div>
              <InputField
                label="Specialization"
                name="specialization"
                value={formData.specialization}
                onChange={handleChange}
              />
              <InputField
                label="Years of Experience"
                name="experience"
                type="number"
                value={formData.experience}
                onChange={handleChange}
              />
              <InputField
                label="Hospital/Clinic"
                name="hospital"
                value={formData.hospital}
                onChange={handleChange}
              />
              <div>
                <label className="block text-sm font-medium text-neutral-700 mb-2">Bio</label>
                <textarea
                  name="bio"
                  value={formData.bio}
                  onChange={handleChange}
                  rows="4"
                  className="w-full px-4 py-2 border border-neutral-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-primary"
                />
              </div>
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-6">
              <div>
                <p className="text-sm text-neutral-600">Name</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">
                  Dr. {formData.firstName} {formData.lastName}
                </p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Specialization</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">{formData.specialization}</p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Experience</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">{formData.experience} years</p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Hospital</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">{formData.hospital}</p>
              </div>
              <div className="col-span-2">
                <p className="text-sm text-neutral-600">Bio</p>
                <p className="text-neutral-900 mt-1">{formData.bio}</p>
              </div>
            </div>
          )}
        </Card>

        {/* Availability Settings */}
        <Card>
          <h2 className="text-xl font-bold text-neutral-900 mb-4 flex items-center gap-2">
            <Settings className="w-5 h-5" />
            Availability Settings
          </h2>
          <p className="text-neutral-600 mb-4">Configure your working hours and availability</p>
          <Button variant="primary">Manage Schedule</Button>
        </Card>
      </div>
    </MainLayout>
  );
};

export default DoctorProfilePage;
