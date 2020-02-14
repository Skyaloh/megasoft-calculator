package com.megasoft.calculator.resources;


import com.megasoft.calculator.domain.Calculator;
import com.megasoft.calculator.dto.CalculatorRequest;
import com.megasoft.calculator.service.CalculatorService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/operation")
@RestController
@RequiredArgsConstructor
public class CalculatorResource {

    private final CalculatorService calculatorService;

    @ApiOperation(value = "Perform command based calculations")

    @PostMapping(value = "")
    public ResponseEntity<Calculator> calculation(@ApiParam(value = "Calculation object to pass command and values", required = true) @RequestBody CalculatorRequest request){
        Calculator response = calculatorService.resourceHandler(request);

        return new ResponseEntity<Calculator>(response, HttpStatus.OK);
    }


    @ApiOperation(value = "Get all calculations history")
    @GetMapping(value = "")
    public ResponseEntity<List<Calculator>> history(){

        List<Calculator> response = calculatorService.listHist();

        return new ResponseEntity<List<Calculator>>(response, HttpStatus.OK);
    }

    @ApiOperation(value = "Reset calculator a fresh")
    @DeleteMapping(value = "")
    public ResponseEntity<Calculator> resetCalculator(){

        calculatorService.reset();
        return new ResponseEntity<Calculator>(HttpStatus.OK);
    }


}
