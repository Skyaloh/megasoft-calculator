package com.megasoft.calculator.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Calculation request details")
public class CalculatorRequest {

    @ApiModelProperty(notes = "Type of operation to be performed. Available commands: { ADD, SUBTRACT, MULTIPLY, DIVIDE, FACTORIAL, RESET }")
    public String command;
    @ApiModelProperty(notes = "Values to be worked on")
    public Double [] values;


}
