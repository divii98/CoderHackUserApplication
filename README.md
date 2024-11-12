# Contest Leaderboard Service
This project is a simple RESTful API for managing users for a coding contest, allowing user registration, score updating, and badge awarding based on user scores. The service provides CRUD operations for user management and retrieves user rankings sorted by score.

## Features
1. User Registration: Register new users with an ID and username.
2. Score Management: Update user scores to determine leaderboard position.
3. Badge Awarding: Assign badges based on score:
   * Code Ninja: 1 ≤ Score < 30
   * Code Champ: 30 ≤ Score < 60
   * Code Master: 60 ≤ Score ≤ 100
4. User Retrieval: Retrieve user using their User ID. 
5. Leaderboard Retrieval: Retrieve users sorted by score.
6. Error Handling: Handles invalid inputs, not found errors, and returns appropriate HTTP codes.

## API Endpoints
| Method  | Endpoint                            | Description                                   |
|---------|-------------------------------------|-----------------------------------------------|
| GET     | /coder-hack-api/v1/users            | Retrieve a list of all registered users\.     |
| GET     | /coder-hack-api/v1/users/\{userId\} | Retrieve the details of a specific user\.     |
| POST    | /coder-hack-api/v1/users            | Register a new user to the contest\.          |
| PUT     | /coder-hack-api/v1/users/\{userId\} | Update the score of a specific user\.         |
| DELETE  | /coder-hack-api/v1/users/\{userId\} | Deregister a specific user from the contest\. |


## Pre-requisites
1. Java 21+
2. Gradle (or use the Gradle wrapper provided in the project)
3. MongoDB

## Installation
##### Clone the repository:
```bash
git clone https://github.com/XXXX
cd your-repo-name
```

## Running the Application
##### 1. Configure the Database:
Make sure to set up your database (e.g., MySQL) and update the database configurations in application.properties.
##### 2. Build and Run the Service:

Use Gradle to build and run the application
``
./gradlew bootRun
``

##### 3. Access the Application:

By default, the application will run at:
> http://localhost:8081


## Postman Collections

For testing the API endpoints, you can use the Postman collection provided in the link below:

[CoderHack API Postman Collection](https://www.postman.com/apicollections-7830/apicollections/collection/8bs20ti/codehackendpoints)
