package com.petscreening.petfriendly.boatrentalservice;

import com.cosium.spring.data.jpa.entity.graph.repository.support.EntityGraphJpaRepositoryFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryFactoryBeanClass = EntityGraphJpaRepositoryFactoryBean.class)
public class BoatRentalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoatRentalServiceApplication.class, args);
    }

}
