# Forever Home Adoption Center Management System

## Basic Idea

The **Forever Home Adoption Center Management System** is a comprehensive application designed to streamline the operations of an animal adoption center. By centralizing key functions, it enhances administrative efficiency and facilitates the welfare of animals. The system includes modules for managing animal profiles, adoption applications, medical records, staff information, events, and donations. It aims to simplify workflows, improve communication, and foster a positive user experience for both staff and adopters.

## Tech Used

- **Java**: Core programming language for application logic.
- **JavaFX**: Framework for building the user interface with Scene Builder for layout design.
- **JDBC**: For seamless database connectivity.
- **PostgreSQL**: Relational database management system.
- **PgAdmin4**: Tool for managing the database schema and records.
- **JasperReports**: For generating detailed reports and data visualizations.
- **Photoshop**: For creating and refining UI elements.
- **Draw.io**: Used for creating UML diagrams and database visualizations.

## Features

### Animal Management
- Detailed animal profiles, including photos, behavior descriptions, and adoption status.
- Option to view and edit animal details (staff only).

### Medical Records
- Comprehensive tracking of vaccinations, treatments, illnesses, and notes.
- Modular design allowing separate records with timestamps for each category.

### Adopter Information
- User-friendly interface to view, edit, and manage adopter profiles.
- Allows adopters to view adoption applications, feedback, and medical records.

### Staff Management
- Role-based management of staff profiles, schedules, and expertise.
- Secure operations for adding, editing, or removing staff information.

### Event Organization
- Manage fundraising and adoption events with details such as date, time, location, and funding goals.
- Track donations tied to specific events.

### Donation Tracking
- Record and view donations, including donor details, type, and purpose.

### Feedback System
- Adopters can leave feedback to improve center operations.
- Feedback displayed dynamically for better engagement.

### Reports
- Generate detailed reports for animals, events, medical records, adopters, and staff.
- JasperReports integration for charts, graphs, and data export.

### GUI Design
- Consistent, modern, and responsive interface for adopters and staff.
- Sidebar navigation for streamlined user experience.

### Security
- Role-based access control with unique user authentication.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 11+
- PostgreSQL
- Apache Maven (optional, for dependency management)

### Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/username/forever-home.git
   ```
2. Import the project into your IDE (e.g., NetBeans).
3. Configure the database connection in the JDBC setup file.
4. Run the application.

### Database Initialization
Use the provided SQL scripts in the `/database` folder to set up the necessary tables and initial data.

## Project Workflow
1. **User Authentication**: Secure login using SSN and password.
2. **Navigation**: Navigate through adopter or staff-specific modules using a persistent sidebar.
3. **Data Management**: Add, edit, and delete records across all modules based on user privileges.
4. **Reports**: Generate and export data visualizations for analysis and presentation.

## Acknowledgments
This project is developed as part of the 2024/2025 academic year database management course. Special thanks to our team for collaborative efforts in designing, developing, and testing the system.
