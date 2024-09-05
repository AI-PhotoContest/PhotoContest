
# Photo Contest Application

## Table of Contents
1. [Project Description](#project-description)
2. [Key Features](#key-features)
3. [Technologies Used](#technologies-used)
4. [Installation and Setup](#installation-and-setup)
    - [Prerequisites](#prerequisites)
    - [Build and Run the Project](#build-and-run-the-project)
5. [Usage](#usage)
    - [Public Landing Page](#public-landing-page)
    - [Authentication](#authentication)
    - [Contest Participation](#contest-participation)
    - [Jury Evaluation](#jury-evaluation)
6. [REST API Documentation](#rest-api-documentation)
7. [Database Schema](#database-schema)
8. [Testing](#testing)
9. [Contributing](#contributing)
10. [License](#license)

## Project Description
The **Photo Contest Application** is an online platform for photographers and photo enthusiasts to organize and participate in photo contests. It supports user registration, contest creation, photo submission, and jury-based evaluation. Users can increase their ranking by participating and winning contests, and experienced users (Photo Masters) can be invited to act as jury members.

## Key Features
- **Public Access:** Non-authenticated users can access the landing page to view contest results and learn about the platform.
- **User Roles:** Supports two roles:
    - **Organizers:** Can create, manage, and view contests.
    - **Photo Junkies:** Can participate in contests, upload photos, and view their ranking.
- **Contest Lifecycle:**
    - **Phase I:** Open for photo submissions.
    - **Phase II:** Open for jury evaluation.
    - **Finished:** Contest results are available.
- **Jury System:** Invitations to participate as a jury are based on user rankings.
- **Ranking System:** Users earn points by participating in contests, with special rankings awarded based on points.
- **REST API:** Full support for CRUD operations on users, contests, and photos, with detailed Swagger documentation.

## Technologies Used
- **Java 8+**
- **Spring Boot**
- **Gradle** for project management
- **Swagger** for API documentation
- **MySQL/PostgreSQL** for database management
- **Hibernate/JPA** for database interactions

## Installation and Setup

### Prerequisites
Ensure you have the following installed:
- **Java Development Kit (JDK) 8 or higher**
- **Gradle** (to build the project)
- **MySQL/PostgreSQL** (for the relational database)

### Build and Run the Project

1. **Clone the repository**:
   ```bash
   git clone <repository-url>
   cd PhotoContest
   ```

2. **Set up the database**:
    - The database is already configured in the application.properties file.
	- AWS service is used for the database.


3. **Build the project**:
   ```bash
   ./gradlew build
   ```

4. **Run the project**:
   ```bash
   ./gradlew bootRun
   ```

5. **Access the application**:
   Open your browser and navigate to `http://localhost:8080`.

## Usage

### Public Landing Page
Non-authenticated users can access the landing page to view winning photos and register for the platform.

### Authentication
Users can log in using their username and password. New users can register by providing a username, first name, last name, and password.

### Contest Participation
- **Phase I (Submission Phase):** Participants can submit one photo, along with a title and story. Once submitted, changes cannot be made.
- **Phase II (Evaluation Phase):** The jury evaluates submitted photos by assigning scores and providing comments.
- **Finished Phase:** Participants can view their scores, comments, and the winning photos.

### Jury Evaluation
- **Jury Members:** Users ranked as "Master" or higher can be invited to evaluate contest photos.
- **Scoring:** Jury members score each photo from 1-10 and provide comments. They can disqualify photos that do not meet the contest category.

## REST API Documentation

The REST API supports the following operations:
- **Users:** Create, read, update, delete (CRUD), and search users.
- **Contests:** CRUD operations, switch contest phases, submit photos, and rate photos.
- **Photos:** CRUD operations, list, and search by title.

Swagger documentation is available at:
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

## Database Schema

The application's database is designed to support contests, photos, and user management. Relationships between entities are normalized to avoid duplication and ensure data integrity.

For a detailed diagram of the database schema, refer to the `/docs` folder.

## Testing

The project includes unit tests for key business logic, ensuring at least 80% test coverage. Continuous integration is set up to run tests on each commit.

To run the tests manually:
```bash
./gradlew test
```

## Contributing

We welcome contributions from the community! To contribute:
1. Fork the repository.
2. Create a new branch for your feature.
3. Commit your changes.
4. Submit a pull request.

Please make sure to include tests for your changes and follow the established coding conventions (KISS, DRY, SOLID).

## License

This project is licensed under the MIT License. See the LICENSE file for details.

