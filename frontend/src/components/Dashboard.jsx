import React from 'react';

const Dashboard = ({ teachers, subjects }) => {
  return (
    <div>
      <h3>Admin Dashboard</h3>
      <p>Total Teachers: {teachers}</p>
      <p>Total Subjects: {subjects}</p>
    </div>
  );
};

export default Dashboard;
