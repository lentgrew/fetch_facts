# Fetch Facts Service

### Deutsche Bank Coding Challenge
### Overview
Your task is to create a backend service in Kotlin or Java that fetches random facts from the Useless Facts API, provides a shortened URL for each fact, caches them, and offers a private area to consult access statistics.
New update: please not use spring tho as framework (either quarkus or ktor).
The service should be built with a focus on clean and maintainable architecture.

#### Requirements
1. Fetch Facts: Implement a service that fetches random facts from the Useless Facts API (https://uselessfacts.jsph.pl/random.json?language=en).
2. Shortened URL Service: For each fact fetched, provide a shortened URL.
3. Caching: Implement in-memory caching to store the facts and their shortened URLs.
4. Analytics: Provide a private endpoint to consult access statistics for each shortened URL (e.g., number of times accessed).
5. No Database: Use simplified in-memory storage instead of a database.

#### Endpoints
1. **Fetch Fact and Shorten URL**
   * *Endpoint:* `POST /facts`  
   * *Description:* Fetches a random fact from the Useless Facts API, stores it, and returns a shortened URL.  
   * *Response:*
```json
{
  "original_fact": "string",
  "shortened_url": "string"
}
```

2. **Return single cached fact**
    * *Endpoint:* `GET /facts/{shortenedUrl}`
    * *Descript ion:* Returns the cached fact and increments the access count.
    * *Response:*
```json
{
  "fact": "string",
  "original_permalink": "string"
}
```

3. **Return all cached facts**
    * *Endpoint:* `GET /facts`
    * *Description:* Returns all cached facts and does not increment the access count.
    * *Response:*
```json
[
  {
    "fact": "string",
    "original_permalink": "string"
  }
]
```

4. **Redirect to original fact**
    * *Endpoint:* `GET /facts/{shortenedUrl}/redirect`
    * *Description:* Redirects to the original fact and increments the access count.

5. **Access statistics**
   * *Endpoint:* `GET /admin/statistics`  
   * *Description:* Provides access statistics for all shortened URLs.
   * *Response:*
```json
[
  {
    "shortened_url": "string",
    "access_count": "integer"
  }
]
```

#### Instructions
1. Project Setup
    * Use Kotlin or Java.
2. Implementation
    * Create a service to fetch random facts from the Useless Facts API.
    * Implement URL shortening logic.
    * Implement in-memory storage to cache facts and their shortened URLs.
    * Track access statistics for each shortened URL.
    * Provide a private endpoint to view access statistics.
3. Running the Application
    * Provide a README.md file with instructions on how to run the application.
    * Include necessary dependencies and setup instructions.
4. Submission
    * Ensure your code is well-documented.
    * Include unit tests to demonstrate code quality and reliability.

#### Evaluation Criteria
    * Adherence to clean and maintainable architecture.
    * Code quality and readability.
    * Completeness and correctness of the implementation.
    * Documentation and ease of setup.
    * Use of in-memory storage for caching and analytics.

#### Additional Notes
    Ensure to handle error responses from the Useless Facts API appropriately.
    Focus on building a robust and maintainable codebase that can be easily extended and maintained.

### Prerequisites

This repository was builded using the following technologies:

* Java SDK 21/23
* Gradle 8.10.2

Having the JDK 21/23, Docker, Docker Compose are mandatory to be able to build and run the App.

### Build

* Clone the project
* Navigate to the root directory of the project
* Execute the command `./gradlew clean build`


### Running

First:
* `./gradlew quarkusDev`

Second:
* `./gradlew build`
* The JAR file will be located in the `./build` folder.
* To run the JAR, use the following command in a console `java -jar build/quarkus-app/quarkus-run.jar`.

Alternative:
* Navigate to the root directory of the project
* Execute the command `docker compose build`
* Execute the command `docker compose up -d`

### Test

* Navigate to the root directory of the project
* Execute the command `./gradlew clean test`

### Usage

* The app will deploy by default in the `port:8080` so please make sure to have it free or change it
  before compilation inside the `application.yml` config file.
* OpenAPI Specification: http://localhost:8080/q/openapi?format=json
    * be aware of the port in case you change it in the steps above.
* Secured private endpoints by enabling role-based access control. Implemented embedded user accounts with Basic Authentication for access.
  *  For testing purposes only, use the following credentials: admin:admin