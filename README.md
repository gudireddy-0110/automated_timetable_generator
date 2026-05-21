# 🗓 AutoSched AI — Academic Scheduling System

![Java](https://img.shields.io/badge/Java-21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=flat&logo=springboot&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=flat&logo=mysql&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-BCrypt-6DB33F?style=flat&logo=springsecurity&logoColor=white)
![License](https://img.shields.io/badge/License-MIT-green?style=flat)

An AI-powered timetable management system built with **Java 21 + Spring Boot + MySQL + Spring Security**. Automatically generates conflict-free section-wise timetables for multiple branches, years, and sections — with role-based access control and PDF export.

---

## ✨ Features

- **Conflict-Free Generation** — Constraint-based algorithm ensures no faculty, room, or section clashes
- **Multi-Branch Support** — CSE, IT, ECE, MECH across all 4 years
- **Section-wise Timetables** — A, B, C sections per branch-year
- **CSV Bulk Upload** — Upload faculty-subject assignments in one go
- **Role-Based Access** — Admin, Faculty, Principal with Spring Security
- **Faculty Timetable** — Individual faculty weekly schedule view
- **Room Utilization** — Track classroom usage across all slots
- **PDF Export** — Download any timetable as PDF
- **Manual Entry** — Add individual faculty assignments anytime

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 21 + Spring Boot 4 |
| Security | Spring Security + BCrypt |
| Database | MySQL 8.0 + Spring Data JPA + Hibernate |
| Frontend | HTML + CSS + Vanilla JavaScript |
| CSV Processing | Apache Commons CSV |
| Build Tool | Maven |

---
<img width="1920" height="1020" alt="Screenshot 2026-05-21 135250" src="https://github.com/user-attachments/assets/342aa4ce-028c-4174-b7e2-e756bae8e474" />

<img width="1920" height="1020" alt="Screenshot 2026-05-21 135544" src="https://github.com/user-attachments/assets/2af59a33-72bb-404a-94fc-5c3678f993b7" />

<img width="1920" height="1020" alt="Screenshot 2026-05-21 135605" src="https://github.com/user-attachments/assets/c4a2fad3-21a9-4925-b63c-6677680ba0bd" />

<img width="1920" height="1020" alt="Screenshot 2026-05-21 135619" src="https://github.com/user-attachments/assets/cd562eb7-6d7f-4eab-82a1-2fc608ad807a" />

<img width="1920" height="1020" alt="Screenshot 2026-05-21 135626" src="https://github.com/user-attachments/assets/63e4b13d-fbb3-42d5-bed6-5dfb15d8aa81" />

<img width="1920" height="1020" alt="Screenshot 2026-05-21 135642" src="https://github.com/user-attachments/assets/0dc051f4-0872-47cf-ba7c-6e3e18381634" />


---

## 🚀 Getting Started

### Prerequisites
- Java 21+
- MySQL 8.0+
- Maven 3.9+

### Setup

```bash
git clone https://github.com/gudireddy-0110/automated_timetable_generator.git
cd automated_timetable_generator
```

Create MySQL database:
```sql
CREATE DATABASE college_scheduler;
```

Update `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
```

Run:
```bash
mvn spring-boot:run
```

Open `http://localhost:8081`

### Default Login Credentials
| Role | Username | Password |
|---|---|---|
| Admin | admin | admin123 |
| Principal | principal | principal123 |
| Faculty | faculty | faculty123 |

---

## 📁 Project Structure

src/main/java/com/indhu/college_scheduler/
├── model/          ← JPA Entities (Branch, Faculty, Subject, TimetableSlot, User)
├── repository/     ← Spring Data JPA Repositories
├── service/        ← Business Logic (SchedulerEngine, TimetableService, CsvImportService)
├── controller/     ← REST Controllers + Auth + Login
└── config/         ← Spring Security Config + Data Initializer

---

## 📊 CSV Upload Format

```csv
faculty_name,subject_name,branch,year
Dr. Sharma,Data Structures,IT,3
Dr. Reddy,DBMS,IT,3
Dr. Rao,Computer Networks,IT,3
Dr. Priya,Data Structures Lab,IT,3
```

System auto-detects subject type (LAB/THEORY) and assigns hours accordingly.

---

## 🔐 Role-Based Access

| Feature | Admin | Principal | Faculty |
|---|---|---|---|
| Add Branches | ✅ | ❌ | ❌ |
| CSV Upload | ✅ | ❌ | ❌ |
| Generate Timetable | ✅ | ❌ | ❌ |
| View All Timetables | ✅ | ✅ | ❌ |
| View Own Timetable | ✅ | ✅ | ✅ |
| Manage Users | ✅ | ❌ | ❌ |

---

## 👤 Author

**Gudi Indhu Reddy**
B.Tech Information Technology | Malla Reddy Engineering College for Women | 2026
[LinkedIn](https://www.linkedin.com/in/gudi-indhu-reddy/) · [GitHub](https://github.com/gudireddy-0110)

---

*Built as a portfolio project demonstrating Java backend development, Spring ecosystem, REST API design, and constraint-based algorithm implementation.*
