package com.megasoft.calculator.service;

import com.megasoft.calculator.domain.Calculator;
import com.megasoft.calculator.dto.CalculatorRequest;
import com.megasoft.calculator.errors.UnknownCommandException;
import com.megasoft.calculator.repository.CalculatorRepository;
import com.megasoft.calculator.utils.CommandType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;


@ShellComponent
@Service
@RequiredArgsConstructor
@Slf4j
public class CalculatorService {

    private Calculator calculator;

    private final CalculatorRepository calculatorRepository;

    public Calculator save(double result, String operation) {

        Calculator calculator = new Calculator();
        calculator.setOperation(operation);
        calculator.setResult(result);

        return calculatorRepository.save(calculator);
    }

    public Calculator resourceHandler(CalculatorRequest request) {
        try {
            switch (CommandType.valueOf(request.command.toUpperCase())) {
                case ADD:
                    add(request.values);
                    break;
                case DIVIDE:
                    divide(request.values);
                    break;
                case MULTIPLY:
                    multiply(request.values);
                    break;

                case SUBTRACT:
                    subtract(request.values);
                    break;

                case FACTORIAL:
                    factor(request.values);
                    calculator.operation = "Factorial of " + request.values[0];
                    break;

                case RESET:

                    calculator = new Calculator();
                    calculator.result =  reset();
                    calculator.operation = "Calculator reset!";
                    break;

                default:
                    throw new UnknownCommandException(request.command);
            }
        } catch (Exception e) {
            throw new UnknownCommandException(request.command);
        }


        return calculator;
    }

    @ShellMethod(value = "Add comma separated integers together e.g \"add 1,2,3,3\"", key = {"add", "+"})
    public Double add(@Valid Double[] args) {

        Double result;
        Optional<Calculator> calcO = optionalCalculator();

        Double sum = Arrays.stream(args).reduce((a, b) -> a + b).get();


        if (calcO.isPresent()) {
            Double previousResult = calcO.get().getResult();
            result = previousResult + sum;
            List<Double> copyValues = new ArrayList<>(Arrays.asList(args));
            copyValues.add(previousResult);
            Collections.swap(copyValues, 0, copyValues.size() - 1);
            args = copyValues.toArray(Double[]::new);

        } else {
            result = sum;
        }

        calculator = save(result, getOperation(CommandType.ADD.toString(), args));

        return result;
    }


    @ShellMethod(value = "Multiply comma separated integers e.g \"multiply 1,2,3,3\"", key = {"multiply", "*"})
    public Double multiply(@Valid Double[] args) {

        Double result;
        Optional<Calculator> calcO = optionalCalculator();

        Double multi = Arrays.stream(args).reduce((a, b) -> a * b).get();

        if (calcO.isPresent()) {
            Double previousResult = calcO.get().getResult();
            result = previousResult * multi;
            List<Double> copyValues = new ArrayList<>(Arrays.asList(args));
            copyValues.add(previousResult);
            Collections.swap(copyValues, 0, copyValues.size() - 1);
            args = copyValues.toArray(Double[]::new);

        } else {
            result = multi;
        }

        calculator = save(result, getOperation(CommandType.MULTIPLY.toString(), args));

        return result;
    }

    @ShellMethod(value = "Multiply comma separated integers e.g \"subtract 1,2,3,3\"", key = {"subtract", "-"})
    public Double subtract(@Valid Double... args) {

        Double result;
        Optional<Calculator> calcO = optionalCalculator();

        Double subtractRes = Arrays.stream(args).reduce((a, b) -> a - b).get();
        if (calcO.isPresent()) {
            Double previousResult = calcO.get().getResult();
            result = Arrays.stream(args).reduce((a, b) -> previousResult - a - b).get();
            List<Double> copyValues = new ArrayList<>(Arrays.asList(args));
            copyValues.add(previousResult);
            Collections.swap(copyValues, 0, copyValues.size() - 1);
            args = copyValues.toArray(Double[]::new);

        } else {
            result = subtractRes;
        }

        calculator = save(result, getOperation(CommandType.SUBTRACT.toString(), args));

        return result;
    }

    @ShellMethod(value = "Multiply comma separated integers e.g \"divide 1,2,3,3\"", key = {"divide", "/"})
    public Double divide(@Valid Double... args) {

        Double result;
        Optional<Calculator> calcO = optionalCalculator();

        Double divisionRes = Arrays.stream(args).reduce((a, b) -> a / b).get();

        if (calcO.isPresent()) {
            Double previousResult = calcO.get().getResult();
            result = Arrays.stream(args).reduce((a, b) -> previousResult /a / b).get();
            List<Double> copyValues = new ArrayList<>(Arrays.asList(args));
            copyValues.add(previousResult);
            Collections.swap(copyValues, 0, copyValues.size() - 1);
            args = copyValues.toArray(Double[]::new);

        } else {
            result = divisionRes;
        }

        calculator = save(result, getOperation(CommandType.DIVIDE.toString(), args));

        return result;
    }


    @ShellMethod(value = "List all calculations history", key = "listHist")
    public List<Calculator> listHist() {
        List<Calculator> history = calculatorRepository.findAll();

        return history;
    }

    @ShellMethod(value = "Reset calculator to start a fresh calculation", key = "reset")
    public Double reset() {

        Double result = 0d;

        calculatorRepository.deleteAll();

        return result;
    }


    @ShellMethod(value = "Perform factorial operation to a single value", key = "factor")
    public Double factor(Double[] values) {

        Double value = values[0];
        Double result = 1d;
        do {
            result = result * value;
            value--;
        }
        while (value > 0);

        calculator = save(result, getOperation(CommandType.FACTORIAL.toString(), values));

        return result;
    }


    public Optional<Calculator> optionalCalculator() {
        return calculatorRepository.findTopByOrderByIdDesc();
    }

    public String getOperation(String command, Double[] values) {

        String calculation;

        switch (CommandType.valueOf(command.toUpperCase())) {
            case ADD:
                calculation = String.join("+",
                        Arrays.asList(ArrayUtils.toStringArray(values)));
                break;
            case DIVIDE:
                calculation = String.join("/",
                        Arrays.asList(ArrayUtils.toStringArray(values)));
                break;
            case MULTIPLY:
                calculation = String.join("*",
                        Arrays.asList(ArrayUtils.toStringArray(values)));
                break;
            case SUBTRACT:
                calculation = String.join("-",
                        Arrays.asList(ArrayUtils.toStringArray(values)));
                break;

            case FACTORIAL:
                calculation = "Factorial of " + values[0];
                break;

            case RESET:
                calculation = "Calc reset!";
                break;

            default:
                calculation = "Unknown operation!";
        }

        return calculation;
    }
}
