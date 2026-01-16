def create_empty_timetable(sections):
    days = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"]
    periods = ["9-10", "10-11", "11-12", "2-3", "3-4", "1-2"]

    timetable = {}
    for section in sections:
        timetable[section] = {}
        for day in days:
            timetable[section][day] = {period: "" for period in periods}
    return timetable
