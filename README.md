# University Portal System (Java OOP)

A command-line interface (CLI) application simulating a university portal. This project demonstrates core Object-Oriented Programming principles and design patterns. It allows students, teachers, and administrators to manage courses, enrollments, marks, and research papers.

## Features
*   **Role-Based Access:** Distinct menus for Students, Teachers, Admins, Deans, HR, and PR Managers.
*   **Authentication:** Secure login using PBKDF2 password hashing.
*   **Data Persistence:** In-memory repositories serialized to a local file (`data/university.ser`).
*   **Design Patterns:** Uses the Strategy Pattern for role-based menu routing.

## Prerequisites
*   **Java JDK:** Version 17 or higher.
*   **Maven:** Version 3.8 or higher (for dependency management and building).

## Setup and Build
Clone the repository and navigate to the project root (where `pom.xml` is located). 

To compile the project and package it into a JAR file:
```bash
mvn clean package