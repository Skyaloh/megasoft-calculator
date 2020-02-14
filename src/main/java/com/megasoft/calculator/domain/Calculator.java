package com.megasoft.calculator.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;


@Entity
@Table(name = "calculator_history")
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ApiModel(description = "All details about the Calculator Fields. ")
public class Calculator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ApiModelProperty(notes = "The database generated calculation ID")
    private Long id;

    @ApiModelProperty(notes = "Performed calculation summary")
    public String operation;
    @ApiModelProperty(notes = "Calculation result")
    public Double result;

    public Calculator(String operation, Double result) {
        this.operation = operation;
        this.result = result;
    }
}
