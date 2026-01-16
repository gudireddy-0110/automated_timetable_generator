from flask import Blueprint, jsonify, request
import os
import pandas as pd
import random

timetable_bp = Blueprint('timetable', __name__)
UPLOAD_DIR = "backend/uploads"

DAYS = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
PERIOD_KEYS = ["P1", "P2", "P3", "P4", "P5"]

@timetable_bp.route('/generate', methods=['GET'])
def generate():
    try:
        section_count = int(request.args.get('count', 1))

        # Load uploaded CSVs
        teachers_path = os.path.join(UPLOAD_DIR, 'teachers.csv')
        subjects_path = os.path.join(UPLOAD_DIR, 'subjects.csv')

        if not os.path.exists(teachers_path) or not os.path.exists(subjects_path):
            return jsonify({"error": "CSV files not uploaded yet."}), 400

        teachers_df = pd.read_csv(teachers_path)
        subjects_df = pd.read_csv(subjects_path)

        # Build subject-teacher pairs
        subject_teacher_pairs = []
        for _, row in subjects_df.iterrows():
            subject = row['Subject']
            teacher_row = teachers_df[teachers_df['Subject'] == subject]
            teacher = teacher_row['Teacher'].values[0] if not teacher_row.empty else "Unknown"
            subject_teacher_pairs.append(f"{subject} ({teacher})")

        # Separate labs and regulars
        labs = [s for s in subject_teacher_pairs if "Lab" in s]
        regulars = [s for s in subject_teacher_pairs if "Lab" not in s]

        if not regulars:
            return jsonify({"error": "No regular subjects found."}), 400

        timetable = {}

        # Track teacher usage: teacher_schedule[day][slot] = set of teachers assigned
        teacher_schedule = {day: {slot: set() for slot in PERIOD_KEYS} for day in DAYS}

        for sec_index in range(section_count):
            section_name = f"Section {chr(65 + sec_index)}"
            timetable[section_name] = {}

            # Choose 1 day for lab for this section
            lab_day = random.choice(DAYS) if labs else None

            for day in DAYS:
                timetable[section_name][day] = {}
                used = set()

                # === Morning periods (P1-P3)
                for slot in ["P1", "P2", "P3"]:
                    available = [
                        subj for subj in regulars
                        if subj.split(" (")[1][:-1] not in teacher_schedule[day][slot]
                    ]
                    if not available:
                        timetable[section_name][day][slot] = "Free"
                        continue

                    choice = random.choice(available)
                    teacher = choice.split(" (")[1][:-1]
                    teacher_schedule[day][slot].add(teacher)
                    used.add(choice)
                    timetable[section_name][day][slot] = choice

                # === Afternoon
                if labs and day == lab_day:
                    # Try to assign a lab in P4 and P5 (2-hour block)
                    available = [
                        lab for lab in labs
                        if lab.split(" (")[1][:-1] not in teacher_schedule[day]["P4"]
                        and lab.split(" (")[1][:-1] not in teacher_schedule[day]["P5"]
                    ]
                    if available:
                        lab_choice = random.choice(available)
                        teacher = lab_choice.split(" (")[1][:-1]
                        timetable[section_name][day]["P4"] = lab_choice
                        timetable[section_name][day]["P5"] = lab_choice
                        teacher_schedule[day]["P4"].add(teacher)
                        teacher_schedule[day]["P5"].add(teacher)
                        used.add(lab_choice)
                    else:
                        timetable[section_name][day]["P4"] = "Free"
                        timetable[section_name][day]["P5"] = "Free"
                else:
                    # Fill P4 and P5 with regulars, no teacher conflict
                    for slot in ["P4", "P5"]:
                        available = [
                            subj for subj in regulars
                            if subj.split(" (")[1][:-1] not in teacher_schedule[day][slot]
                            and subj not in used
                        ]
                        if not available:
                            timetable[section_name][day][slot] = "Free"
                            continue

                        choice = random.choice(available)
                        teacher = choice.split(" (")[1][:-1]
                        teacher_schedule[day][slot].add(teacher)
                        used.add(choice)
                        timetable[section_name][day][slot] = choice

        return jsonify({"timetable": timetable})

    except Exception as e:
        return jsonify({"error": str(e)}), 500
