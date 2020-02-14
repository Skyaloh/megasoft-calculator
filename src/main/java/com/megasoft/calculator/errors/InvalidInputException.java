package com.megasoft.calculator.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class InvalidInputException extends AbstractThrowableProblem {
    public InvalidInputException(String input){
        super(ErrorConstants.ENTITY_NOT_FOUND_TYPE, String.format("Invalid input %s!", input), Status.FORBIDDEN);
    }
}
