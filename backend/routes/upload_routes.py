from flask import Blueprint, request, jsonify
import os
import pandas as pd

upload_bp = Blueprint('upload', __name__)
UPLOAD_DIR = "backend/uploads"
os.makedirs(UPLOAD_DIR, exist_ok=True)

@upload_bp.route('/upload', methods=['POST'])
def upload_csvs():
    try:
        teacher_file = request.files['teachers']
        subject_file = request.files['subjects']

        teacher_path = os.path.join(UPLOAD_DIR, 'teachers.csv')
        subject_path = os.path.join(UPLOAD_DIR, 'subjects.csv')

        teacher_file.save(teacher_path)
        subject_file.save(subject_path)

        teachers = pd.read_csv(teacher_path)
        subjects = pd.read_csv(subject_path)

        return jsonify({
            "message": "Files uploaded successfully",
            "teachers_count": len(teachers),
            "subjects_count": len(subjects)
        }), 200
    except Exception as e:
        return jsonify({"error": f"Upload failed: {str(e)}"}), 500
