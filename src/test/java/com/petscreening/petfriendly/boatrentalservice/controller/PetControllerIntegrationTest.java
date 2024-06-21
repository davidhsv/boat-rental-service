package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.config.HttpGraphqlTesterConfig;
import com.petscreening.petfriendly.boatrentalservice.config.TestcontainersConfiguration;
import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.*;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.util.TestUtilService;
import graphql.relay.DefaultConnection;
import org.flywaydb.core.internal.util.FileUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ResourceLoader;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestcontainersConfiguration.class, HttpGraphqlTesterConfig.class})
@ActiveProfiles("test")
@AutoConfigureHttpGraphQlTester
@DisplayName("PetController Integration Tests")
public class PetControllerIntegrationTest {

    @Autowired
    private HttpGraphQlTester httpGraphQlTester;
    @Autowired
    private TestUtilService testUtilService;

    @Nested
    @Sql(scripts = "classpath:add-pets.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_CLASS)
    @DisplayName("Eligible Pets Query Tests")
    class EligiblePetsQueryTests {
        String queryWithoutPagination = testUtilService.getTestResource("queryWithoutPagination.graphql");
        String queryWithAfterPagination = testUtilService.getTestResource("queryWithAfterPagination.graphql");
        String queryWithBeforePagination = testUtilService.getTestResource("queryWithBeforePagination.graphql");

        @Test
        @DisplayName("Should find eligible pets with specific breed, training level, vaccination status, and weight range")
        void testEligiblePetsWithSpecificCriteria() {
            // Given
            var criteriaInput = new EligibilityCriteriaInputDto(
                    new BreedCriteriaInputDto(List.of(new BreedConditionInputDto("Poodle", true),
                            new BreedConditionInputDto("Labrador", true))),
                    new TrainingLevelCriteriaInputDto(1, 8),
                    new VaccinationCriteriaInputDto(true),
                    new WeightCriteriaInputDto(2.0f, 100.0f)
            );

            // When & Then
            httpGraphQlTester.document(queryWithoutPagination)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entity(new ParameterizedTypeReference<DefaultConnection<PetDto>>() {})
                    .satisfies(petConnection -> {
                        petConnection.getEdges().forEach(edge -> {
                            PetDto pet = edge.getNode();
                            assertThat(pet.breed()).isIn("Poodle", "Labrador");
                            assertThat(pet.trainingLevel()).isBetween(1, 8);
                            assertThat(pet.vaccinated()).isTrue();
                            assertThat(pet.weight()).isBetween(2.0d, 100.0d);
                        });
                    });
        }

        @Test
        @DisplayName("Should find pets only with specific breed")
        void testEligiblePetsWithSpecificBreed() {
            // Given
            var criteriaInput = new EligibilityCriteriaInputDto(
                    new BreedCriteriaInputDto(List.of(new BreedConditionInputDto("Poodle", true))),
                    null,
                    null,
                    null
            );

            // When & Then
            httpGraphQlTester.document(queryWithoutPagination)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entity(new ParameterizedTypeReference<DefaultConnection<PetDto>>() {})
                    .satisfies(petConnection -> {
                        petConnection.getEdges().forEach(edge -> {
                            PetDto pet = edge.getNode();
                            assertThat(pet.breed()).isEqualTo("Poodle");
                        });
                    });
        }

        @Test
        @DisplayName("Should find no pets when no criteria matches")
        void testEligiblePetsWithNoMatchingCriteria() {
            // Given
            var criteriaInput = new EligibilityCriteriaInputDto(
                    new BreedCriteriaInputDto(List.of(new BreedConditionInputDto("Poodle", true),
                            new BreedConditionInputDto("Non-existent breed", true))),
                    new TrainingLevelCriteriaInputDto(8, 10),
                    new VaccinationCriteriaInputDto(false),
                    new WeightCriteriaInputDto(2.0f, 5.0f)
            );

            // When & Then
            httpGraphQlTester.document(queryWithoutPagination)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entity(new ParameterizedTypeReference<DefaultConnection<PetDto>>() {})
                    .satisfies(petConnection -> {
                        assertThat(petConnection.getEdges()).isEmpty();
                    });
        }

        @Test
        @DisplayName("Should return error when no criteria is provided")
        void testEligiblePetsWithNoCriteria() {
            // Given
            var criteria = new EligibilityCriteriaInputDto(null, null, null, null);
            // When & Then
            httpGraphQlTester.document(queryWithoutPagination)
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
            var criteriaInput = new EligibilityCriteriaInputDto(
                    new BreedCriteriaInputDto(List.of(new BreedConditionInputDto("Poodle", false),
                            new BreedConditionInputDto("Labrador", true))),
                    new TrainingLevelCriteriaInputDto(1, 10),
                    new VaccinationCriteriaInputDto(true),
                    new WeightCriteriaInputDto(2.0f, 18f)
            );

            // When & Then
            httpGraphQlTester.document(queryWithoutPagination)
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entity(new ParameterizedTypeReference<DefaultConnection<PetDto>>() {})
                    .satisfies(petConnection -> {
                        petConnection.getEdges().stream().allMatch(edge -> {
                            PetDto pet = edge.getNode();
                            return pet.breed().equals("Labrador") &&
                                    pet.trainingLevel() >= 1 && pet.trainingLevel() <= 10 &&
                                    pet.vaccinated() &&
                                    pet.weight() >= 2.0f && pet.weight() <= 18f;
                        });
                        petConnection.getEdges().stream().noneMatch(edge -> {
                            PetDto pet = edge.getNode();
                            return pet.breed().equals("Poodle");
                        });
                    });
        }

        @Test
        @DisplayName("Should paginate using after cursor")
        void testEligiblePetsPaginationWithAfterCursor() {
            // Given
            var criteriaInput = new EligibilityCriteriaInputDto(
                    new BreedCriteriaInputDto(List.of(new BreedConditionInputDto("Poodle", true),
                            new BreedConditionInputDto("Labrador", true))),
                    new TrainingLevelCriteriaInputDto(1, 8),
                    new VaccinationCriteriaInputDto(true),
                    new WeightCriteriaInputDto(2.0f, 100.0f)
            );

            // When & Then
            httpGraphQlTester.document(queryWithAfterPagination)
                    .variable("criteria", criteriaInput)
                    .variable("after", "S19bImphdmEudXRpbC5Db2xsZWN0aW9ucyRVbm1vZGlmaWFibGVNYXAiLHsiaWQiOlsiamF2YS5sYW5nLkxvbmciLDExMTNdfV0=")
                    .execute()
                    .path("eligiblePets")
                    .entity(new ParameterizedTypeReference<DefaultConnection<PetDto>>() {})
                    .satisfies(petConnection -> {
                        assertThat(petConnection.getPageInfo().getStartCursor()).isNotNull();
                        assertThat(petConnection.getPageInfo().getEndCursor()).isNotNull();
                    });
        }

        @Test
        @DisplayName("Should paginate using before cursor")
        void testEligiblePetsPaginationWithBeforeCursor() {
            // Given
            var criteriaInput = new EligibilityCriteriaInputDto(
                    new BreedCriteriaInputDto(List.of(new BreedConditionInputDto("Poodle", true),
                            new BreedConditionInputDto("Labrador", true))),
                    new TrainingLevelCriteriaInputDto(1, 8),
                    new VaccinationCriteriaInputDto(true),
                    new WeightCriteriaInputDto(2.0f, 100.0f)
            );

            // When & Then
            httpGraphQlTester.document(queryWithBeforePagination)
                    .variable("criteria", criteriaInput)
                    .variable("before", "S19bImphdmEudXRpbC5Db2xsZWN0aW9ucyRVbm1vZGlmaWFibGVNYXAiLHsiaWQiOlsiamF2YS5sYW5nLkxvbmciLDExMTNdfV0=")
                    .execute()
                    .path("eligiblePets")
                    .entity(new ParameterizedTypeReference<DefaultConnection<PetDto>>() {})
                    .satisfies(petConnection -> {
                        assertThat(petConnection.getPageInfo().getStartCursor()).isNotNull();
                        assertThat(petConnection.getPageInfo().getEndCursor()).isNotNull();
                    });
        }
    }
}
