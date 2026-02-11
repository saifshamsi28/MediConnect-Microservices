import React, { useState } from 'react';
import { MainLayout } from '../components/layout';
import { useAuth } from '../context/AuthContext';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { Calendar, Users, Heart } from 'lucide-react';

export const PatientDashboard = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);

  return (
    <MainLayout showSidebar={true}>
      <div className="space-y-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900">Health Dashboard</h1>
          <p className="text-neutral-600 mt-2">Welcome, {user?.firstName}!</p>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card>
            <div className="text-center">
              <p className="text-neutral-600 text-sm">Upcoming Appointments</p>
              <p className="text-3xl font-bold text-primary mt-2">2</p>
            </div>
          </Card>
          <Card>
            <div className="text-center">
              <p className="text-neutral-600 text-sm">Total Consultations</p>
              <p className="text-3xl font-bold text-secondary mt-2">8</p>
            </div>
          </Card>
          <Card>
            <div className="text-center">
              <p className="text-neutral-600 text-sm">Favorite Doctors</p>
              <p className="text-3xl font-bold text-accent mt-2">3</p>
            </div>
          </Card>
        </div>

        {/* Upcoming Appointments */}
        <Card>
          <h2 className="text-xl font-bold text-neutral-900 mb-6">Upcoming Appointments</h2>
          {appointments.length === 0 ? (
            <div className="text-center py-8">
              <Calendar className="w-12 h-12 text-neutral-300 mx-auto mb-4" />
              <p className="text-neutral-600 mb-4">No appointments scheduled</p>
              <Button variant="primary">Book an Appointment</Button>
            </div>
          ) : (
            <div className="space-y-4">
              {/* Appointment items would go here */}
            </div>
          )}
        </Card>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Button variant="primary" className="w-full py-3">
            Find a Doctor
          </Button>
          <Button variant="secondary" className="w-full py-3">
            View Medical Records
          </Button>
        </div>
      </div>
    </MainLayout>
  );
};

export default PatientDashboard;
