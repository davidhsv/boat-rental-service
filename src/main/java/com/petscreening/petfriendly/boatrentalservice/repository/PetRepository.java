package com.petscreening.petfriendly.boatrentalservice.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphJpaRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

@Repository
public interface PetRepository extends EntityGraphJpaRepository<Pet, Long>, EntityGraphQuerydslPredicateExecutor<Pet> {
    int LIMIT = 20;

    default Window<Pet> findBy(Predicate criteria, KeysetScrollPosition scrollPosition, EntityGraph entityGraph) {
        // entityGraph is going to be used and intercepted by EntityGraphQueryHintCandidates
        return this.findBy(
                criteria, (FluentQuery.FetchableFluentQuery<Pet> query) -> 
                        query.limit(LIMIT)
                        .scroll(scrollPosition));
    }
}