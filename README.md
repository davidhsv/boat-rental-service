# Boat Rental Service

This project is a Spring Boot application that provides a GraphQL API for managing pets and their eligibility for boat rentals.

It's my solution for the Boat Rental Service API interview challenge. Enjoy :)

## Getting Started

### Prerequisites

- Java 21
- Maven
- Docker (for local development)

### Technologies

- Java 21 with virtual threads enabled for improved performance and Data Driven Design (sealed interfaces + switch expression)
- Spring Boot 3
- Spring for GraphQL
- Spring Data JPA with Scrollable results and QueryDSL support
- GraphQL Cursor Connections Specification for pagination and KeysetScrollPosition for navigating the results.
- QueryDSL for dynamic (compile time checked) query generation
- Lombok for reducing boilerplate code
- MapStruct for mapping DTOs to entities
- Docker Compose Spring Boot 3.1 integration
- PostgreSQL
- TestContainers for spinning up a PostgreSQL database for integration tests
- Flyway for automatic provisioning of the database schema and data

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/davidhsv/boat-rental-service.git
   ```
2. Navigate to the project directory:
    ```bash
    cd boat-rental-service
    ```
3. Run the application:
    ```bash
    mvn spring-boot:run
    ```
    
Everything will be automatically set up for you, including the database schema and initial data (using the PostgreSQL inside compose.yaml and Flyway for automatic database provisioning).

You can access GraphiQL at:
http://localhost:8080/graphiql?path=/graphql

### Development Environment

For local development, the application uses Docker Compose to spin up a PostgreSQL database. This is automatically configured in the `application.properties` file and the `compose.yaml` file. It's leveraging the Spring Boot 3.1 integration with Docker Compose.

Also, the application uses the `spring-boot-devtools` dependency for automatic restarts when changes are detected.

For the database creating and populating, the application uses Flyway. The SQL scripts are located in the `src/main/resources/db/migration` directory.

### Production Environment

For production, the Docker Compose support is disabled. You need to provide the database connection details in the `application-prod.properties` file or ideally via environment variables.

### Testing

The application uses Testcontainers for integration tests, ensuring that each test runs in a new, isolated database environment. It allows the test to also test the database schema and data provisioning (flyway migrations).

## API Endpoints

The application exposes a GraphQL API. You can interact with it using the GraphiQL interface at:
> http://localhost:8080/graphiql?path=/graphql

### Queries
1. `eligiblePets`: Retrieves a list of pets that meet specified eligibility criteria.
It's using GraphQL Cursor Connections Specification from relay.dev (https://relay.dev/graphql/connections.htm).

Additionally, it's using KeysetScrollPosition to paginate/navigate the results. It's a more efficient way to paginate results when compared to OFFSET/LIMIT. You can navigate forward with the after query parameter, or backward with the before query parameter.

To get the first page, just omit the after and before query parameters.

   Example query:
   ```graphql
   query {
     eligiblePets(
       criteria: {
         breedCriteria: {
           breedConditions: [
             { breed: "Poodle", include: true },
             { breed: "Labrador", include: true }
           ]
         },
         trainingLevelCriteria: {
           trainingLevelFrom: 1,
           trainingLevelTo: 10
         },
         vaccinationCriteria: {
           vaccinated: true
         },
         weightCriteria: {
           weightFrom: 2,
           weightTo: 100
         }
       }
     ) {
       pageInfo {
         hasPreviousPage
         hasNextPage
         startCursor
         endCursor
       }
       edges {
         node {
           name,
           weight,
           petOwner {
             name
           }
         }
       }
     }
   }
   ```
   
2. `checkEligibility`: Checks if a pet is eligible for a boat rental based on specified criteria.

   Example query:
   ```graphql
    {
      checkEligibility(
        petId:7,
        criteria: {breedCriteria: {breedConditions: [
          {breed: "Poodle", include: true},
          {breed: "Labrador", include: true}]},
          trainingLevelCriteria: {trainingLevelFrom: 1, trainingLevelTo: 10},
          vaccinationCriteria: {vaccinated: true},
          weightCriteria: {weightFrom: 2, weightTo: 100}}
      )
    }
   ```

### Mutations

1. `addPet`: Adds a new pet to the system.

   Example mutation:
   ```graphql
   mutation {
     addPet(pet: {
       name: "Buddy",
       weight: 12.5,
       breed: "Golden Retriever",
       vaccinated: true,
       trainingLevel: 3,
       petOwnerId: 1001
     }) {
       id
       name
       weight
       breed
       vaccinated
       trainingLevel
       petOwner {
         id
         name
         contactInfo
       }
     }
   }
   ```

### Database

#### Connecting to the development database
To connect to the development database, you can use the following connection details:
- JDBC URL: jdbc:postgresql://localhost:5439/boatrental
- Username: postgres
- Password: postgres

The server should be running to connect to the database. This is because the database is running inside a Docker container and is started up automatically when the application is started and stopped when the application is stopped.

#### Tables

##### pet_owner
- `id` (INT, Primary Key)
- `name` (VARCHAR)
- `contact_info` (VARCHAR)

##### pet
- `id` (INT, Primary Key)
- `name` (VARCHAR)
- `weight` (DECIMAL)
- `breed` (VARCHAR)
- `vaccinated` (BOOLEAN)
- `training_level` (INT)
- `pet_owner_id` (INT, Foreign Key to `pet_owner`)

#### Indices
- `pet` table: Indices on `weight`, `breed`, `vaccinated`, `training_level`

### Error Handling
The application implements basic error handling for the API and database interactions. Errors are returned in the GraphQL response under the errors field using @GraphQlExceptionHandler in the ErrorControllerAdvice class. Validation is using the Bean Validation API.

## Out of scope
Things would be missing in a real production environment:
- Security: The application does not implement any security features. It's recommended to add authentication and authorization mechanisms to secure the API.
- Monitoring and logging: The application does not have any monitoring or logging features. It's recommended to add logging and monitoring to track the application's performance and health.
- Dockerfile and Kubernetes deployment: The application does not have a Dockerfile or Kubernetes deployment configuration. It's recommended to create a Dockerfile and Kubernetes deployment configuration to deploy the application in a containerized environment.
- Gitops / CI/CD: The application does not have Gitops or CI/CD pipelines. It's recommended to set up Gitops and CI/CD pipelines to automate the deployment process.
- Performance testing: The application does not have performance testing. It's recommended to perform performance testing to ensure the application can handle the expected load.