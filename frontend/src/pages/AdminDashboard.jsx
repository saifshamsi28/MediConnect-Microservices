import React from 'react';
import { MainLayout } from '../components/layout';
import { useAuth } from '../context/AuthContext';
import Card from '../components/ui/Card';
import { Users, Calendar, FileText } from 'lucide-react';

const StatCard = ({ icon: Icon, label, value, color = 'bg-primary' }) => (
  <Card className="flex items-center gap-4">
    <div className={`${color} p-4 rounded-lg`}>
      <Icon className="w-6 h-6 text-white" />
    </div>
    <div>
      <p className="text-neutral-600 text-sm">{label}</p>
      <p className="text-2xl font-bold text-neutral-900">{value}</p>
    </div>
  </Card>
);

export const AdminDashboard = () => {
  const { user } = useAuth();

  return (
    <MainLayout showSidebar={true}>
      <div className="space-y-8">
        {/* Header */}
        <div>
          <h1 className="text-3xl font-bold text-neutral-900">Admin Dashboard</h1>
          <p className="text-neutral-600 mt-2">Welcome back, {user?.firstName}!</p>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
          <StatCard 
            icon={Users} 
            label="Total Doctors" 
            value="124"
            color="bg-primary"
          />
          <StatCard 
            icon={Users} 
            label="Total Patients" 
            value="856"
            color="bg-secondary"
          />
          <StatCard 
            icon={Calendar} 
            label="Appointments" 
            value="342"
            color="bg-accent"
          />
          <StatCard 
            icon={FileText} 
            label="Pending Approvals" 
            value="12"
            color="bg-red-500"
          />
        </div>

        {/* Recent Activity */}
        <Card>
          <h2 className="text-xl font-bold text-neutral-900 mb-4">Recent Activity</h2>
          <p className="text-neutral-600">No recent activity to display</p>
        </Card>
      </div>
    </MainLayout>
  );
};

export default AdminDashboard;
