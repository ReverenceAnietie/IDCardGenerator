School ID Card System
Overview
The School ID Card System is a Java-based desktop application designed to manage student profiles and generate student identification cards for the University of Uyo. The application features secure user authentication, profile management with image upload, and professional ID card generation with print and save capabilities. It uses Java Swing for the user interface and PostgreSQL for data storage.
Features

User Authentication: Secure login and signup functionality with password hashing (SHA-256).
Profile Management: Users can create and update student profiles, including personal details and profile photos.
ID Card Generation: Generate professional student ID cards with customizable layouts, supporting printing and saving as PNG images.
Database Integration: Uses PostgreSQL to store user and student data, with tables for users and student profiles.
Case-Insensitive Login: Username comparison during login is case-insensitive for user convenience.
User-Friendly Interface: Built with Java Swing, featuring a responsive and intuitive GUI.

Technologies Used

Java: Core programming language (Java SE).
Java Swing: For the graphical user interface.
PostgreSQL: Database for storing user and student information.
JDBC: For database connectivity.
ImageIO: For handling image uploads and ID card generation.

Prerequisites
To run this application, ensure you have the following installed:

Java Development Kit (JDK): Version 8 or higher.
PostgreSQL: Version 9.6 or higher.
PostgreSQL JDBC Driver: Add the driver to your project dependencies (e.g., postgresql-42.7.3.jar).
Maven (optional): For managing dependencies if you choose to use a Maven project structure.
A compatible IDE (e.g., IntelliJ IDEA, Eclipse) or command-line environment for compiling and running Java applications.

Setup Instructions

Clone the Repository:
git clone <repository-url>
cd SchoolIDSystem


Set Up PostgreSQL Database:

Install PostgreSQL and ensure it's running.

Create a database named school_id_system:
CREATE DATABASE school_id_system;


Update the database connection details in DatabaseConnection.java if necessary:
private static final String URL = "jdbc:postgresql://localhost:5432/school_id_system";
private static final String USERNAME = "postgres";
private static final String PASSWORD = "Admin";

Replace USERNAME and PASSWORD with your PostgreSQL credentials.



Add PostgreSQL JDBC Driver:

Download the PostgreSQL JDBC driver from https://jdbc.postgresql.org/.
Add the .jar file to your project's classpath or include it in your IDE's library settings.


Compile and Run:

If using an IDE, import the project and run the SchoolIDSystem.java main class.

From the command line:
javac -cp .:path/to/postgresql.jar SchoolIDSystem/*.java
java -cp .:path/to/postgresql.jar SchoolIDSystem.SchoolIDSystem

Replace path/to/postgresql.jar with the actual path to the JDBC driver.



Database Initialization:

The application automatically initializes the database tables (users and students) on startup via DatabaseConnection.java.



Usage

Launch the Application:

Run the SchoolIDSystem main class to start the application.
The login window will appear.


Sign Up:

Click "Sign Up" on the login screen to create a new account.
Enter a username (minimum 3 characters), a valid email, and a password (minimum 6 characters).
Confirm the password and submit. You'll be redirected to the login screen upon successful registration.


Log In:

Enter your username and password. The login is case-insensitive for usernames.
Upon successful login, you'll be taken to the main dashboard.


Manage Profile:

In the dashboard, fill in your student profile details (e.g., full name, registration number, department, faculty, etc.).
Upload a profile photo (JPG, PNG, or GIF formats supported).
Click "Save Profile" to store your information in the database.


Generate ID Card:

Ensure your profile is complete (at least the full name is required).
Click "Generate ID Card" to open the ID card generator.
Preview the ID card, then use the "Print ID Card" button to print or "Save as Image" to save it as a PNG file.


Logout:

Click the "Logout" button in the dashboard to return to the login screen.



Project Structure

DatabaseConnection.java: Manages database connections and initializes tables.
AuthService.java: Handles user authentication (login, signup, profile updates).
User.java: Model class for user and student data.
LoginForm.java: GUI for user login.
SignupForm.java: GUI for user registration.
MainDashboard.java: Main interface for profile management and ID card generation.
IDCardGenerator.java: Generates and displays the ID card, with options to print or save.
SchoolIDSystem.java: Main entry point for the application.

Notes

Ensure the images directory is writable, as profile photos are saved there.
The ID card design is tailored for the University of Uyo, with a specific layout and branding.
The application uses SHA-256 for password hashing to ensure security.
The database schema includes two tables: users (for authentication) and students (for profile details).

Troubleshooting

Database Connection Errors: Verify PostgreSQL is running and the credentials in DatabaseConnection.java are correct.
Image Upload Issues: Ensure the application has permission to create/read/write in the images directory.
JDBC Driver Not Found: Confirm the PostgreSQL JDBC driver is included in the classpath.
ID Card Printing Issues: Ensure a printer is connected and configured correctly.

Future Enhancements

Add support for batch ID card generation.
Implement password recovery functionality.
Enhance ID card customization options (e.g., templates, colors).
Add administrative features for managing multiple users.

Contact
For issues or contributions, please contact the project maintainer or open an issue on the repository.