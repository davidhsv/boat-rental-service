package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.config.TestcontainersConfiguration;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.*;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureHttpGraphQlTester
@DisplayName("PetController Integration Tests")
public class PetControllerIntegrationTest {

    @Autowired
    private HttpGraphQlTester httpGraphQlTester;

    @Nested
    @Sql(scripts = "classpath:add-pets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    @Sql(scripts = "classpath:remove-pets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_CLASS)
    @DisplayName("Eligible Pets Query Tests")
    class EligiblePetsQueryTests {
        String query = """
                query EligiblePets($criteria: EligibilityCriteriaInput!) {
                    eligiblePets(criteria: $criteria) {
                        id
                        name
                        weight
                        breed
                        vaccinated
                        trainingLevel
                    }
                }
                """;

        @Test
        @DisplayName("Should find eligible pets with specific breed, training level, vaccination status, and weight range")
        void testEligiblePetsWithSpecificCriteria() {
            // Given
            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(1, 8),
                    new VaccinationCriteriaInput(true),
                    new WeightCriteriaInput(2.0f, 100.0f)
            );

            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(Pet.class)
                    .satisfies(petList -> {
                        assertThat(petList).allMatch(pet -> pet.getTrainingLevel() >= 1 && pet.getTrainingLevel() <= 8);
                        assertThat(petList).allMatch(Pet::getVaccinated);
                        assertThat(petList).allMatch(pet -> pet.getWeight() >= 2.0f && pet.getWeight() <= 100.0f);
                        assertThat(petList).extracting(Pet::getBreed)
                                .containsOnly("Poodle", "Labrador");
                    });
        }

        @Test
        @DisplayName("Should find pets only with specific breed")
        void testEligiblePetsWithSpecificBreed() {
            // Given
            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true))),
                    null,
                    null,
                    null
            );

            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(Pet.class)
                    .satisfies(petList -> {
                        assertThat(petList).allMatch(pet -> pet.getBreed().equals("Poodle"));
                    });
        }

        @Test
        @DisplayName("Should find no pets when no criteria matches")
        void testEligiblePetsWithNoMatchingCriteria() {
            // Given
            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Non-existent breed", true))),
                    new TrainingLevelCriteriaInput(8, 10),
                    new VaccinationCriteriaInput(false),
                    new WeightCriteriaInput(2.0f, 5.0f)
            );

            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(Pet.class)
                    .hasSize(0);
        }

        @Test
        @DisplayName("Should return error when no criteria is provided")
        void testEligiblePetsWithNoCriteria() {
            // Given
            var criteria = new EligibilityCriteriaInput(null, null, null, null);
            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteria)
                    .execute()
                    .errors()
                    .expect(error -> !error.getMessage().isEmpty())
                    .verify();
        }

        @Test
        @DisplayName("Should include and exclude breeds properly")
        void testEligiblePetsWithIncludeAndExcludeBreeds() {
            // Given
            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", false),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(1, 10),
                    new VaccinationCriteriaInput(true),
                    new WeightCriteriaInput(2.0f, 18f)
            );

            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(Pet.class)
                    .hasSize(1)
                    .satisfies(petList -> {
                        assertThat(petList).allMatch(pet -> pet.getBreed().equals("Labrador"));
                        assertThat(petList).noneMatch(pet -> pet.getBreed().equals("Poodle"));
                        assertThat(petList).allMatch(pet -> pet.getTrainingLevel() >= 1 && pet.getTrainingLevel() <= 10);
                        assertThat(petList).allMatch(Pet::getVaccinated);
                        assertThat(petList).allMatch(pet -> pet.getWeight() >= 2.0f && pet.getWeight() <= 18f);
                    });
        }
    }
}
