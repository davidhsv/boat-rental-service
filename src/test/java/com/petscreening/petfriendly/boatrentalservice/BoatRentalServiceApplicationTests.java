package com.petscreening.petfriendly.boatrentalservice;

import com.petscreening.petfriendly.boatrentalservice.config.TestcontainersConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class BoatRentalServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
