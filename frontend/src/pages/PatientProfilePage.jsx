import React, { useState } from 'react';
import { MainLayout } from '../components/layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import InputField from '../components/ui/InputField';
import { Edit2, Save, X } from 'lucide-react';
import { useAuth } from '../context/AuthContext';

export const PatientProfilePage = () => {
  const { user, updateUser } = useAuth();
  const [isEditing, setIsEditing] = useState(false);
  const [formData, setFormData] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    gender: 'MALE',
    bloodGroup: 'O+',
    dateOfBirth: '1990-01-15',
    address: '123 Main St, City',
    emergencyContact: '+1-555-0123',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSave = () => {
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
          <h1 className="text-3xl font-bold text-neutral-900">My Profile</h1>
          <p className="text-neutral-600 mt-2">Manage your personal information</p>
        </div>

        {/* Profile Card */}
        <Card>
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-xl font-bold text-neutral-900">Personal Information</h2>
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

              <div className="grid grid-cols-2 gap-4">
                <InputField
                  label="Gender"
                  name="gender"
                  value={formData.gender}
                  onChange={handleChange}
                />
                <InputField
                  label="Blood Group"
                  name="bloodGroup"
                  value={formData.bloodGroup}
                  onChange={handleChange}
                />
              </div>

              <InputField
                label="Date of Birth"
                name="dateOfBirth"
                type="date"
                value={formData.dateOfBirth}
                onChange={handleChange}
              />

              <InputField
                label="Address"
                name="address"
                value={formData.address}
                onChange={handleChange}
              />

              <InputField
                label="Emergency Contact"
                name="emergencyContact"
                type="tel"
                value={formData.emergencyContact}
                onChange={handleChange}
              />
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-6">
              <div>
                <p className="text-sm text-neutral-600">Name</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">
                  {formData.firstName} {formData.lastName}
                </p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Gender</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">{formData.gender}</p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Blood Group</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">{formData.bloodGroup}</p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Date of Birth</p>
                <p className="text-lg font-semibold text-neutral-900 mt-1">{formData.dateOfBirth}</p>
              </div>
              <div className="col-span-2">
                <p className="text-sm text-neutral-600">Address</p>
                <p className="text-neutral-900 mt-1">{formData.address}</p>
              </div>
              <div>
                <p className="text-sm text-neutral-600">Emergency Contact</p>
                <p className="text-neutral-900 mt-1">{formData.emergencyContact}</p>
              </div>
            </div>
          )}
        </Card>

        {/* Medical Info Card */}
        <Card>
          <h2 className="text-xl font-bold text-neutral-900 mb-4">Medical Information</h2>
          <div className="space-y-4">
            <div>
              <p className="text-sm text-neutral-600">Allergies</p>
              <p className="text-neutral-900 mt-1">No allergies recorded</p>
            </div>
            <div>
              <p className="text-sm text-neutral-600">Current Medications</p>
              <p className="text-neutral-900 mt-1">None</p>
            </div>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
};

export default PatientProfilePage;
