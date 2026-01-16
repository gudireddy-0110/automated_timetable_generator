import React, { useState } from 'react';
import API from './api';
import UploadForm from './components/UploadForm';
import TimetableGrid from './components/TimetableGrid';
import './App.css';
import html2pdf from 'html2pdf.js';

function App() {
  const [timetable, setTimetable] = useState({});
  const [facultyTimetables, setFacultyTimetables] = useState({});
  const [section, setSection] = useState('');
  const [selectedFaculty, setSelectedFaculty] = useState('');
  const [numSections, setNumSections] = useState(2);

  const handleUploadSuccess = () => {
    alert("Files uploaded successfully!");
  };

  const handleGenerate = async () => {
    try {
      const res = await API.get(`/generate?count=${numSections}`);
      const sectionTimetable = res.data.timetable;
      setTimetable(sectionTimetable);
      const firstSection = Object.keys(sectionTimetable)[0];
      setSection(firstSection || '');

      // Generate faculty-wise timetable
      const facultyMap = {}; // { facultyName: { day: { time: "Section - Subject" } } }

      Object.entries(sectionTimetable).forEach(([sec, days]) => {
        Object.entries(days).forEach(([day, slots]) => {
          Object.entries(slots).forEach(([time, value]) => {
            if (!value || value === "") return;

            const [subject, faculty] = value.split(" - ");
            if (!faculty) return;

            if (!facultyMap[faculty]) facultyMap[faculty] = {};
            if (!facultyMap[faculty][day]) facultyMap[faculty][day] = {};

            facultyMap[faculty][day][time] = `${sec} - ${subject}`;
          });
        });
      });

      setFacultyTimetables(facultyMap);
      alert("Timetable generated!");

    } catch (err) {
      console.error("Generation error:", err);
      alert(err?.response?.data?.error || "Generation failed.");
    }
  };

  const handleExportPDF = () => {
    const element = document.getElementById('timetable-pdf');
    const opt = {
      margin: 0.3,
      filename: `${section}_timetable.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'in', format: 'a4', orientation: 'landscape' }
    };
    html2pdf().set(opt).from(element).save();
  };

  const handleExportFacultyPDF = () => {
    const element = document.getElementById('faculty-pdf');
    const opt = {
      margin: 0.3,
      filename: `${selectedFaculty}_timetable.pdf`,
      image: { type: 'jpeg', quality: 0.98 },
      html2canvas: { scale: 2 },
      jsPDF: { unit: 'in', format: 'a4', orientation: 'landscape' }
    };
    html2pdf().set(opt).from(element).save();
  };

  return (
    <div className="App">
      <h1>AI Timetable Generator</h1>

      <h2>Upload CSV Files</h2>
      <UploadForm onSuccess={handleUploadSuccess} />

      <div style={{ marginTop: '20px' }}>
        <label>Number of Sections: </label>
        <input
          type="number"
          value={numSections}
          onChange={(e) => setNumSections(parseInt(e.target.value))}
          min={1}
          max={26}
        />
        <button onClick={handleGenerate}>Generate Timetable</button>
      </div>

      {/* Section-wise Timetable */}
      {Object.keys(timetable).length > 0 && (
        <>
          <div style={{ marginTop: '20px' }}>
            <label>Select Section: </label>
            <select
              value={section}
              onChange={(e) => setSection(e.target.value)}
            >
              {Object.keys(timetable).map((sec) => (
                <option key={sec} value={sec}>{sec}</option>
              ))}
            </select>
          </div>

          <div id="timetable-pdf" style={{ marginTop: '20px' }}>
            <TimetableGrid section={section} timetable={timetable} />
          </div>

          <button onClick={handleExportPDF} style={{ marginTop: '10px' }}>
            Export Section PDF
          </button>
        </>
      )}

      {/* Faculty-wise Timetable */}
      {Object.keys(facultyTimetables).length > 0 && (
        <>
          <div style={{ marginTop: '30px' }}>
            <label>Select Faculty: </label>
            <select
              value={selectedFaculty}
              onChange={(e) => setSelectedFaculty(e.target.value)}
            >
              <option value="">-- Select --</option>
              {Object.keys(facultyTimetables).map((name) => (
                <option key={name} value={name}>{name}</option>
              ))}
            </select>
          </div>

          {selectedFaculty && (
            <>
              <div id="faculty-pdf" style={{ marginTop: '20px' }}>
                <TimetableGrid
                  section={selectedFaculty}
                  timetable={{ [selectedFaculty]: facultyTimetables[selectedFaculty] }}
                />
              </div>
              <button onClick={handleExportFacultyPDF} style={{ marginTop: '10px' }}>
                Export Faculty PDF
              </button>
            </>
          )}
        </>
      )}
    </div>
  );
}

export default App;
