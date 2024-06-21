package com.petscreening.petfriendly.boatrentalservice.util;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class TestUtilService {
    @Autowired
    ResourceLoader resourceLoader;
    
    @SneakyThrows
    public String getTestResource(String path) {
        return resourceLoader.getResource("classpath:" + path)
                .getContentAsString(StandardCharsets.UTF_8);
    }
}
