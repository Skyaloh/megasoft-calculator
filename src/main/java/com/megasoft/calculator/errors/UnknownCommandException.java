package com.megasoft.calculator.errors;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class UnknownCommandException  extends AbstractThrowableProblem {

    public UnknownCommandException(String command){
        super(ErrorConstants.ENTITY_NOT_FOUND_TYPE, String.format("Unknown command %s!", command), Status.FORBIDDEN);
    }
}
