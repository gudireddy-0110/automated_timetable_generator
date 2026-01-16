import React from 'react';
import './TimetableGrid.css';

const TimetableGrid = ({ section, timetable }) => {
  if (!section || !timetable[section]) return null;

  const days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
  const slots = [
    { label: "9:20–10:30", key: "P1" },
    { label: "10:30–11:40", key: "P2" },
    { label: "11:40–12:50", key: "P3" },
    { label: "Break", key: "BREAK" },
    { label: "1:30–2:55", key: "P4" },
    { label: "2:55–4:20", key: "P5" }
  ];

  const backendKeys = ["P1", "P2", "P3", "P4", "P5"];

  return (
    <div style={{ marginTop: "20px" }}>
      <h2>{section} Timetable</h2>
      <table border="1" cellPadding="6" style={{ borderCollapse: "collapse", width: "100%" }}>
        <thead>
          <tr>
            <th>Day</th>
            {slots.map((slot, i) => (
              <th key={i}>{slot.label}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {days.map((day) => (
            <tr key={day}>
              <td><strong>{day}</strong></td>
              {slots.map((slot, index) => {
                if (slot.key === "BREAK") {
                  return <td key={index} style={{ textAlign: 'center' }}>🍴</td>;
                } else {
                  return <td key={index}>{timetable[section][day][slot.key]}</td>;
                }
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default TimetableGrid;
