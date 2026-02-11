import React from 'react';
import { Snackbar, Alert, CircularProgress } from '@mui/material';
import { useNotification } from '../../context/NotificationContext';

export const Toast = () => {
  const { notifications, removeNotification } = useNotification();

  const getAlertSeverity = (type) => {
    const severityMap = {
      success: 'success',
      error: 'error',
      warning: 'warning',
      info: 'info',
    };
    return severityMap[type] || 'info';
  };

  return (
    <>
      {notifications.map((notification) => (
        <Snackbar
          key={notification.id}
          open={true}
          autoHideDuration={5000}
          onClose={() => removeNotification(notification.id)}
          anchorOrigin={{ vertical: 'top', horizontal: 'right' }}
        >
          <Alert 
            onClose={() => removeNotification(notification.id)}
            severity={getAlertSeverity(notification.type)}
            sx={{ width: '100%' }}
          >
            {notification.message}
          </Alert>
        </Snackbar>
      ))}
    </>
  );
};

export const Loader = ({ open = false, message = 'Loading...' }) => {
  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div className="bg-white rounded-lg p-6 flex flex-col items-center gap-4">
        <CircularProgress />
        <p className="text-neutral-700 font-medium">{message}</p>
      </div>
    </div>
  );
};

export default Toast;
