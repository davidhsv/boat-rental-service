package com.petscreening.petfriendly.boatrentalservice.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import graphql.com.google.common.collect.ImmutableList;
import graphql.relay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@TestConfiguration(proxyBeanMethods = false)
public class HttpGraphqlTesterConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public HttpGraphQlTester webTestClientGraphQlTester(WebTestClient webTestClient, GraphQlProperties properties) {
        SimpleModule module = new SimpleModule();
        module.addDeserializer(ImmutableList.class, new ImmutableListJsonDeserializer());
        module.addAbstractTypeMapping(Edge.class, DefaultEdge.class);
        module.addAbstractTypeMapping(PageInfo.class, DefaultPageInfo.class);
        module.addAbstractTypeMapping(Connection.class, DefaultConnection.class);
        module.addAbstractTypeMapping(ConnectionCursor.class, DefaultConnectionCursor.class);
        module.addAbstractTypeMapping(ConnectionCursor.class, DefaultConnectionCursor.class);
        objectMapper.registerModule(module);

        WebTestClient.Builder builder = webTestClient.mutate().baseUrl(properties.getPath());
        return HttpGraphQlTester.builder(builder)
                .codecConfigurer(configurer -> {
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(objectMapper));
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(objectMapper));
                }).build();
    }

    private static class ImmutableListJsonDeserializer extends JsonDeserializer<ImmutableList<Object>> {
        @Override
        public ImmutableList<Object> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
            JsonNode node = p.getCodec().readTree(p);
            List<Edge<Object>> list = new ArrayList<>();
            node.forEach(item -> {
                try {
                    Edge<Object> edge = p.getCodec().treeToValue(item, Edge.class);
                    list.add(edge);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }
            });
            return ImmutableList.copyOf(list);
        }
    }
}