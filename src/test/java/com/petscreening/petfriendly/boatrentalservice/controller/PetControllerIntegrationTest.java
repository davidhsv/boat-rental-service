package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.config.TestcontainersConfiguration;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.*;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import com.petscreening.petfriendly.boatrentalservice.repository.PetOwnerRepository;
import com.petscreening.petfriendly.boatrentalservice.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
@Transactional
@AutoConfigureHttpGraphQlTester
@Sql(scripts = "classpath:add-pets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:remove-pets.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class PetControllerIntegrationTest {

    @Autowired
    private HttpGraphQlTester httpGraphQlTester;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private PetOwnerRepository petOwnerRepository;

    private PetOwner owner;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        owner = petOwnerRepository.save(new PetOwner(null, "John Doe", "john.doe@example.com", null));
        pets = petRepository.saveAll(List.of(
                new Pet(null, "Buddy", 22.5, "Poodle", true, 5, owner),
                new Pet(null, "Max", 50.0, "Labrador", true, 7, owner),
                new Pet(null, "Charlie", 30.0, "Terrier", true, 6, owner)
        ));
    }

    @Nested
    @DisplayName("Eligible Pets Query Tests")
    class EligiblePetsQueryTests {

        @Test
        @DisplayName("Should find eligible pets with specific breed, training level, vaccination status, and weight range")
        void testEligiblePetsWithSpecificCriteria() {
            // Given
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

            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(1, 10),
                    new VaccinationCriteriaInput(true),
                    new WeightCriteriaInput(2.0f, 100.0f)
            );

            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(Pet.class)
                    .hasSize(3)
                    .satisfies(petList -> {
                        assertThat(petList).extracting(Pet::getName)
                                .containsExactlyInAnyOrder("Buddy", "Max");
                    });
        }

        @Test
        @DisplayName("Should find no pets when no criteria matches")
        void testEligiblePetsWithNoMatchingCriteria() {
            // Given
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

            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
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
        @DisplayName("Should include and exclude breeds properly")
        void testEligiblePetsWithIncludeAndExcludeBreeds() {
            // Given
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

            var criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(1, 10),
                    new VaccinationCriteriaInput(true),
                    new WeightCriteriaInput(2.0f, 100.0f)
            );

            // When & Then
            httpGraphQlTester.document(query)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(Pet.class)
                    .hasSize(1)
                    .satisfies(petList -> {
                        assertThat(petList).extracting(Pet::getName)
                                .containsExactly("Buddy");
                    });
        }
    }
}
