package com.petscreening.petfriendly.boatrentalservice.controller;

import com.petscreening.petfriendly.boatrentalservice.dto.PetDto;
import com.petscreening.petfriendly.boatrentalservice.dto.criteria.*;
import com.petscreening.petfriendly.boatrentalservice.service.PetService;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.petscreening.petfriendly.boatrentalservice.model.PetOwner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@GraphQlTest(PetController.class)
public class PetControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private PetService petService;

    private PetOwner owner;
    private List<Pet> pets;

    @BeforeEach
    void setUp() {
        owner = new PetOwner(1L, "John Doe", "john.doe@example.com", null);
        pets = List.of(
                new Pet(1L, "Buddy", 22.5, "Poodle", true, 5, owner),
                new Pet(2L, "Max", 50.0, "Labrador", true, 7, owner),
                new Pet(3L, "Charlie", 30.0, "Terrier", true, 6, owner)
        );
    }

    @Nested
    @DisplayName("Eligible Pets Query Tests")
    class EligiblePetsQueryTests {

        @Test
        @DisplayName("Should find eligible pets with specific breed, training level, vaccination status, and weight range")
        void testEligiblePetsWithSpecificCriteria() throws Exception {
            // Given
            EligibilityCriteriaInput criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(1, 10),
                    new VaccinationCriteriaInput(true),
                    new WeightCriteriaInput(2.0f, 100.0f)
            );
            List<PetDto> petDtos = pets.stream()
                    .filter(p -> p.getBreed().equals("Poodle") || p.getBreed().equals("Labrador"))
                    .filter(p -> p.getVaccinated())
                    .filter(p -> p.getTrainingLevel() >= 1 && p.getTrainingLevel() <= 10)
                    .filter(p -> p.getWeight() >= 2 && p.getWeight() <= 100)
                    .map(p -> new PetDto(p.getId(), p.getName(), p.getWeight(), p.getBreed(), p.getVaccinated(), p.getTrainingLevel(), null))
                    .collect(Collectors.toList());
//            when(petService.getEligiblePets(any(EligibilityCriteriaInput.class))).thenReturn(() -> petDtos);

            // When & Then
            graphQlTester.documentName("eligiblePets")
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(PetDto.class)
                    .hasSize(2)
                    .contains(petDtos.get(0), petDtos.get(1));
        }

        @Test
        @DisplayName("Should find no pets when no criteria matches")
        void testEligiblePetsWithNoMatchingCriteria() throws Exception {
            // Given
            EligibilityCriteriaInput criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(8, 10),
                    new VaccinationCriteriaInput(false),
                    new WeightCriteriaInput(2.0f, 5.0f)
            );
//            when(petService.getEligiblePets(any(EligibilityCriteriaInput.class))).thenReturn(() -> List.of());

            // When & Then
            graphQlTester.documentName("eligiblePets")
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(PetDto.class)
                    .hasSize(0);
        }

        @Test
        @DisplayName("Should include and exclude breeds properly")
        void testEligiblePetsWithIncludeAndExcludeBreeds() throws Exception {
            // Given
            EligibilityCriteriaInput criteriaInput = new EligibilityCriteriaInput(
                    new BreedCriteriaInput(List.of(new BreedConditionInput("Poodle", true),
                            new BreedConditionInput("Labrador", true))),
                    new TrainingLevelCriteriaInput(1, 10),
                    new VaccinationCriteriaInput(true),
                    new WeightCriteriaInput(2.0f, 100.0f)
            );
            List<PetDto> petDtos = pets.stream()
                    .filter(p -> p.getBreed().equals("Poodle"))
                    .filter(p -> p.getVaccinated())
                    .filter(p -> p.getTrainingLevel() >= 1 && p.getTrainingLevel() <= 10)
                    .filter(p -> p.getWeight() >= 2 && p.getWeight() <= 100)
                    .map(p -> new PetDto(p.getId(), p.getName(), p.getWeight(), p.getBreed(), p.getVaccinated(), p.getTrainingLevel(), null))
                    .collect(Collectors.toList());
//            when(petService.getEligiblePets(any(EligibilityCriteriaInput.class))).thenReturn(() -> petDtos);

            // When & Then
            graphQlTester.documentName("eligiblePets")
                    .variable("criteria", criteriaInput)
                    .execute()
                    .path("eligiblePets")
                    .entityList(PetDto.class)
                    .hasSize(1)
                    .contains(petDtos.get(0));
        }
    }
}
