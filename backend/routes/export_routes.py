from flask import Blueprint, request, send_file
from reportlab.lib.pagesizes import letter, landscape
from reportlab.pdfgen import canvas
from io import BytesIO

export_bp = Blueprint('export', __name__)

@export_bp.route('/export/pdf', methods=['POST'])
def export_pdf():
    data = request.get_json()
    output = BytesIO()
    c = canvas.Canvas(output, pagesize=landscape(letter))

    for section, days in data.items():
        c.setFont("Helvetica-Bold", 16)
        c.drawString(30, 550, f"Timetable - Section {section}")

        y = 520
        periods = ["Day", "9-10", "10-11", "11-12", "2-3", "3-4", "1-2"]
        for day, period_data in days.items():
            row = [day] + [period_data.get(p, "") for p in periods[1:]]
            x = 30
            for item in row:
                c.setFont("Helvetica", 10)
                c.drawString(x, y, item)
                x += 100
            y -= 20

        c.showPage()

    c.save()
    output.seek(0)
    return send_file(output, download_name="timetable.pdf", as_attachment=True)
