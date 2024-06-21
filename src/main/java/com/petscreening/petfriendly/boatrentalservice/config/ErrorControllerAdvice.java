package com.petscreening.petfriendly.boatrentalservice.config;

import graphql.GraphQLError;
import graphql.GraphqlErrorBuilder;
import jakarta.validation.ConstraintViolationException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ErrorControllerAdvice {

    @GraphQlExceptionHandler(IllegalArgumentException.class)
    public GraphQLError handleException(IllegalArgumentException e) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }
    
    @GraphQlExceptionHandler(ConstraintViolationException.class)
    public GraphQLError handleConstraintViolationException(ConstraintViolationException e) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.BAD_REQUEST)
                .message(e.getMessage())
                .build();
    }

    @GraphQlExceptionHandler(Exception.class)
    public GraphQLError handleGenericException(Exception e) {
        return GraphqlErrorBuilder.newError()
                .errorType(ErrorType.INTERNAL_ERROR)
                .message(e.getMessage())
                .build();
    }
}
