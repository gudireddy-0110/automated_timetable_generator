import React, { useState } from 'react';
import API from '../api';

const UploadForm = ({ onSuccess }) => {
  const [teachersFile, setTeachersFile] = useState(null);
  const [subjectsFile, setSubjectsFile] = useState(null);

  const handleUpload = async () => {
    if (!teachersFile || !subjectsFile) {
      alert("Please select both files.");
      return;
    }

    const formData = new FormData();
    formData.append('teachers', teachersFile);
    formData.append('subjects', subjectsFile);

    try {
      await API.post('/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      });
      onSuccess();
    } catch (err) {
      console.error("Upload failed:", err);
      alert(err?.response?.data?.error || "Upload failed.");
    }
  };

  return (
    <div>
      <input type="file" onChange={(e) => setTeachersFile(e.target.files[0])} />
      <label>teachers.csv</label>
      <input type="file" onChange={(e) => setSubjectsFile(e.target.files[0])} />
      <label>subjects.csv</label>
      <button onClick={handleUpload}>Upload</button>
    </div>
  );
};

export default UploadForm;
