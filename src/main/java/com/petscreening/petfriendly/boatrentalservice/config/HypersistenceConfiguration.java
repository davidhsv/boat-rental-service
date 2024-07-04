//package com.petscreening.petfriendly.boatrentalservice.config;
//
//import io.hypersistence.optimizer.HypersistenceOptimizer;
//import io.hypersistence.optimizer.core.config.JpaConfig;
//import jakarta.persistence.EntityManagerFactory;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnClass(HypersistenceOptimizer.class)
//public class HypersistenceConfiguration {
//    @Bean
//    public HypersistenceOptimizer hypersistenceOptimizer(
//            EntityManagerFactory entityManagerFactory) {
//        return new HypersistenceOptimizer(
//                new JpaConfig(entityManagerFactory)
//        );
//    }
//}