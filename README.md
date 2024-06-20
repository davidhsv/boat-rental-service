- using docker compose support (spring boot 3.1) to spin up the psotgresql for
- local development and disabled for production. In testing we are using
- testcontainer to test always in a new database.
- Using @RequestScope to enable the dev to see the sata after the test

http://localhost:8080/graphiql?path=/graphql


TODO/IDEAS
hexagonal packages
--test
usar pageable no retorno

Implement basic error handling for the API and database interactions.

```json
query {
  eligiblePets (
    criteria: {
    breedCriteria: {
      breedConditions: [
        { breed: "Poodle", include: true }
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
  }){
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