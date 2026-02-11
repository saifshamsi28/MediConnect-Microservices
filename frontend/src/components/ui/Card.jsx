import React from 'react';

export const Card = ({ 
  className = '', 
  children,
  hoverable = false,
  ...props 
}) => {
  return (
    <div
      className={`bg-white rounded-lg border border-neutral-200 p-6 shadow-soft ${
        hoverable ? 'hover:shadow-medium transition-shadow cursor-pointer' : ''
      } ${className}`}
      {...props}
    >
      {children}
    </div>
  );
};

export default Card;
