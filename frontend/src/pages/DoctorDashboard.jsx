import React, { useState, useEffect } from 'react';
import { MainLayout } from '../components/layout';
import { useAuth } from '../context/AuthContext';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import { Calendar, Clock, MapPin } from 'lucide-react';

export const DoctorDashboard = () => {
  const { user } = useAuth();
  const [appointments, setAppointments] = useState([]);

  return (
    <MainLayout showSidebar={true}>
      <div className="space-y-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900">Doctor Dashboard</h1>
          <p className="text-neutral-600 mt-2">Welcome, Dr. {user?.lastName}!</p>
        </div>

        {/* Quick Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
          <Card>
            <div className="text-center">
              <p className="text-neutral-600 text-sm">Today's Appointments</p>
              <p className="text-3xl font-bold text-primary mt-2">5</p>
            </div>
          </Card>
          <Card>
            <div className="text-center">
              <p className="text-neutral-600 text-sm">Available Slots</p>
              <p className="text-3xl font-bold text-secondary mt-2">12</p>
            </div>
          </Card>
          <Card>
            <div className="text-center">
              <p className="text-neutral-600 text-sm">Average Rating</p>
              <p className="text-3xl font-bold text-accent mt-2">4.8/5</p>
            </div>
          </Card>
        </div>

        {/* Upcoming Appointments */}
        <Card>
          <h2 className="text-xl font-bold text-neutral-900 mb-6">Today's Appointments</h2>
          {appointments.length === 0 ? (
            <p className="text-neutral-600 text-center py-8">No appointments scheduled today</p>
          ) : (
            <div className="space-y-4">
              {/* Appointment items would go here */}
            </div>
          )}
        </Card>

        {/* Quick Actions */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
          <Button variant="primary" className="w-full py-3">
            Manage Schedule
          </Button>
          <Button variant="secondary" className="w-full py-3">
            View All Appointments
          </Button>
        </div>
      </div>
    </MainLayout>
  );
};

export default DoctorDashboard;
