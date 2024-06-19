package com.petscreening.petfriendly.boatrentalservice;

import org.springframework.boot.SpringApplication;

public class TestBoatRentalServiceApplication {

    public static void main(String[] args) {
        SpringApplication.from(BoatRentalServiceApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
