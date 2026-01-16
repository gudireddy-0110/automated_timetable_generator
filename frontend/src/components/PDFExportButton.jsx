import React from 'react';
import API from '../api';

const PDFExportButton = ({ data }) => {
  const exportPDF = async () => {
    const res = await API.post('/export/pdf', data, { responseType: 'blob' });
    const url = window.URL.createObjectURL(new Blob([res.data]));
    const a = document.createElement('a');
    a.href = url;
    a.download = 'timetable.pdf';
    a.click();
  };

  return <button onClick={exportPDF}>Export All as PDF</button>;
};

export default PDFExportButton;
