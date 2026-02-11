import React, { useState } from 'react';
import { MainLayout } from '../components/layout';
import Card from '../components/ui/Card';
import Button from '../components/ui/Button';
import InputField from '../components/ui/InputField';
import SelectDropdown from '../components/ui/SelectDropdown';
import { Users, Search, ChevronDown } from 'lucide-react';

export const AdminDoctorsPage = () => {
  const [doctors, setDoctors] = useState([
    { id: 1, name: 'Dr. John Smith', specialty: 'Cardiology', status: 'APPROVED', rating: 4.8 },
    { id: 2, name: 'Dr. Sarah Johnson', specialty: 'Neurology', status: 'PENDING', rating: 4.5 },
    { id: 3, name: 'Dr. Mike Davis', specialty: 'Orthopedics', status: 'APPROVED', rating: 4.9 },
  ]);

  const [filterStatus, setFilterStatus] = useState('ALL');
  const [searchTerm, setSearchTerm] = useState('');

  const handleApprove = (doctorId) => {
    setDoctors(doctors.map(d => 
      d.id === doctorId ? { ...d, status: 'APPROVED' } : d
    ));
  };

  const handleReject = (doctorId) => {
    setDoctors(doctors.map(d => 
      d.id === doctorId ? { ...d, status: 'REJECTED' } : d
    ));
  };

  const filteredDoctors = doctors.filter(d => {
    const matchesStatus = filterStatus === 'ALL' || d.status === filterStatus;
    const matchesSearch = d.name.toLowerCase().includes(searchTerm.toLowerCase());
    return matchesStatus && matchesSearch;
  });

  return (
    <MainLayout showSidebar={true}>
      <div className="space-y-8">
        <div>
          <h1 className="text-3xl font-bold text-neutral-900">Manage Doctors</h1>
          <p className="text-neutral-600 mt-2">Approve and manage doctor registrations</p>
        </div>

        {/* Filters */}
        <Card>
          <div className="flex gap-4 flex-wrap">
            <InputField
              placeholder="Search by name..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
              className="flex-1 min-w-48"
            />
            <SelectDropdown
              options={[
                { value: 'ALL', label: 'All Status' },
                { value: 'PENDING', label: 'Pending' },
                { value: 'APPROVED', label: 'Approved' },
                { value: 'REJECTED', label: 'Rejected' },
              ]}
              value={filterStatus}
              onChange={(e) => setFilterStatus(e.target.value)}
              className="w-48"
            />
          </div>
        </Card>

        {/* Doctors Table */}
        <Card className="overflow-hidden">
          <div className="overflow-x-auto">
            <table className="w-full">
              <thead className="bg-neutral-50 border-b border-neutral-200">
                <tr>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-neutral-700">Name</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-neutral-700">Specialty</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-neutral-700">Rating</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-neutral-700">Status</th>
                  <th className="px-6 py-3 text-left text-sm font-semibold text-neutral-700">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-neutral-200">
                {filteredDoctors.map((doctor) => (
                  <tr key={doctor.id} className="hover:bg-neutral-50">
                    <td className="px-6 py-4 text-sm text-neutral-900">{doctor.name}</td>
                    <td className="px-6 py-4 text-sm text-neutral-600">{doctor.specialty}</td>
                    <td className="px-6 py-4 text-sm text-neutral-600">{doctor.rating}/5</td>
                    <td className="px-6 py-4 text-sm">
                      <span className={`px-3 py-1 rounded-full text-xs font-medium ${
                        doctor.status === 'APPROVED' ? 'bg-green-100 text-green-800' :
                        doctor.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' :
                        'bg-red-100 text-red-800'
                      }`}>
                        {doctor.status}
                      </span>
                    </td>
                    <td className="px-6 py-4 text-sm space-x-2">
                      {doctor.status === 'PENDING' && (
                        <>
                          <Button 
                            size="sm"
                            variant="primary"
                            onClick={() => handleApprove(doctor.id)}
                          >
                            Approve
                          </Button>
                          <Button 
                            size="sm"
                            variant="danger"
                            onClick={() => handleReject(doctor.id)}
                          >
                            Reject
                          </Button>
                        </>
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </Card>
      </div>
    </MainLayout>
  );
};

export default AdminDoctorsPage;
