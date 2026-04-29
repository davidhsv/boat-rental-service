package com.petscreening.petfriendly.boatrentalservice.repository;

import com.cosium.spring.data.jpa.entity.graph.domain2.EntityGraph;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphCrudRepository;
import com.cosium.spring.data.jpa.entity.graph.repository.EntityGraphQuerydslPredicateExecutor;
import com.petscreening.petfriendly.boatrentalservice.model.Pet;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.QueryHint;
import org.springframework.data.domain.KeysetScrollPosition;
import org.springframework.data.domain.Window;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.function.Function;

@Repository
public interface PetRepository extends EntityGraphCrudRepository<Pet, Long>, EntityGraphQuerydslPredicateExecutor<Pet> {

    int LIMIT = 20;

    default Window<Pet> findBy(Predicate criteria, KeysetScrollPosition scrollPosition, EntityGraph entityGraph) {
        
        // entityGraph is going to be used and intercepted by EntityGraphQueryHintCandidates
        return this.findBy(
                criteria, (FluentQuery.FetchableFluentQuery<Pet> query) -> 
                        query.limit(LIMIT)
                        .scroll(scrollPosition));
    }

    @Override
    @QueryHints(value = {@QueryHint(name = "jakarta.persistence.query.timeout", value = "1000")})
    <S extends Pet, R> R findBy(Predicate predicate, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction);
}