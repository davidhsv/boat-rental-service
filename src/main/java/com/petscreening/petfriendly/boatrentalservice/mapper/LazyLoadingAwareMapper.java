package com.petscreening.petfriendly.boatrentalservice.mapper;

import org.hibernate.Hibernate;

import java.util.Collection;

public interface LazyLoadingAwareMapper {
  default boolean isNotLazyLoaded(Object sourceCollection){
      return Hibernate.isInitialized(sourceCollection);
  }
}