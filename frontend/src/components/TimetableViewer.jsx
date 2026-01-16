import React from 'react';

const TimetableViewer = ({ data }) => {
  if (!data || Object.keys(data).length === 0) return null;

  const periods = ["1-2", "10-11", "11-12", "2-3", "3-4", "9-10"];

  return Object.entries(data).map(([section, days]) => (
    <div key={section}>
      <h4>Section {section}</h4>
      <table border="1" cellPadding="5">
        <thead>
          <tr>
            <th>Day</th>
            {periods.map((p) => (
              <th key={p}>{p}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {Object.entries(days).map(([day, slots]) => (
            <tr key={day}>
              <td>{day}</td>
              {periods.map((p) => (
                <td key={p}>{slots[p]}</td>
              ))}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  ));
};

export default TimetableViewer;
