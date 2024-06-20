package com.petscreening.petfriendly.boatrentalservice.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ErrorControllerAdvice {

    @GraphQlExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleException(Exception e) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }
}
