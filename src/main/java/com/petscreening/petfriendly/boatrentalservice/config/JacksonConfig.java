package com.petscreening.petfriendly.boatrentalservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.graphql.data.pagination.CursorEncoder;
import org.springframework.graphql.data.pagination.CursorStrategy;
import org.springframework.graphql.data.pagination.EncodingCursorStrategy;
import org.springframework.graphql.data.query.JsonKeysetCursorStrategy;
import org.springframework.graphql.data.query.ScrollPositionCursorStrategy;
import org.springframework.http.codec.CodecConfigurer;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Configuration
public class JacksonConfig {
//    @Bean
//    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
//        // The id of Pet is Long, and itś currently not working with Cursors on GraphQL
//        // it only supports String in JsonKeysetCursorStrategy ids
//        // because of that, we need change the PolymorphicTypeValidator to
//        // accept Object -> Long
//        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
//                .allowIfBaseType(Long.class)
//                .build();
//        return builder -> builder.postConfigurer(objectMapper -> objectMapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL));
//    }

    @Bean
    EncodingCursorStrategy<ScrollPosition> cursorStrategy() {
        JsonKeysetCursorStrategy keysetCursorStrategy = new JsonKeysetCursorStrategy(initCodecConfigurer());
        ScrollPositionCursorStrategy strategy = new ScrollPositionCursorStrategy(keysetCursorStrategy);
        return CursorStrategy.withEncoder(strategy, CursorEncoder.base64());
    }

    private static ServerCodecConfigurer initCodecConfigurer() {
        ServerCodecConfigurer configurer = ServerCodecConfigurer.create();
        customizeCodecConfigurer(configurer);
        return configurer;
    }

    private static void customizeCodecConfigurer(CodecConfigurer configurer) {

        PolymorphicTypeValidator validator = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Map.class)
                .allowIfSubType("java.time.")
                .allowIfSubType(Calendar.class)
                .allowIfSubType(Date.class)
                // added
                .allowIfSubType(Long.class)
                .build();

        ObjectMapper mapper = Jackson2ObjectMapperBuilder.json().build();
        mapper.activateDefaultTyping(validator, ObjectMapper.DefaultTyping.NON_FINAL);

        configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(mapper));
        configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(mapper));
    }
}
