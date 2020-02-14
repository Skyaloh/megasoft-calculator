package com.megasoft.calculator;

import com.megasoft.calculator.repository.CalculatorRepository;
import com.megasoft.calculator.service.CalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.Before;
import org.springframework.shell.Shell;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.shell.result.DefaultResultHandler;


import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
})
//@ComponentScan(basePackages = {"com.megasoft.calculator", "javax.servlet"})
@DisplayName("Calculator Unit Tests")
@Slf4j
public class CalculatorServiceUnitTests {


    @InjectMocks
    private CalculatorService calculatorService;

    @Mock
    private CalculatorRepository calculatorRepository;

    @Autowired
    private Shell shell;

    @Autowired
    private DefaultResultHandler resultHandler;


    @DisplayName("Testing 'help' command works")
    @Test
    void contextLoads() {
        Object help = shell.evaluate(() -> "help");
        MatcherAssert.assertThat(help, is(not(nullValue())));

        resultHandler.handleResult(help);
        System.out.println(help);

    }

    @Before
    public void setUp() throws Exception{
        MockitoAnnotations.initMocks(this);

    }

    @DisplayName("Testing Sum -> +")
    @Test
    void testSum(){
        assertEquals((Double) 50d,calculatorService.add(new Double[]{10d,20d,20d}),
                ()-> "10+20+20 = " + (10d+20d+20d));
    }

    @DisplayName("Testing Subtraction -> -")
    @Test
    public void testSubtraction(){
        assertEquals((Double) 8d,calculatorService.subtract(new Double[]{10d,2d}),
                ()-> "10-2 = " + (10d-2d)
        );
    }

    @DisplayName("Testing Division -> ÷")
    @Test
    public void testDivide(){
        assertEquals((Double) 5d,calculatorService.divide(new Double[]{100d,10d,2d}),
                ()-> "100÷10÷2 = " + (100d/10d/2d)
                );
    }


    @DisplayName("Testing Multiply -> *")
    @Test
    public void testMultiply(){
        assertEquals((Double) 20d,calculatorService.multiply(new Double[]{10d,2d}),
                ()-> "10x2 = " + (10d*2d)
                );
    }


    @DisplayName("Testing Factorial -> *")
    @Test
    public void testFactorial(){
        assertEquals((Double) 720d,calculatorService.factor(new Double[]{6d}),
                ()-> "6x5x4x3x2x1 = " + (6d*5d*4d*3d*2d*1d)
        );
    }


    @DisplayName("Testing Reset Calculator history")
    @Test
    public void testReset(){
        assertEquals((Double) 50d,calculatorService.add(new Double[]{10d,20d,20d}),
                ()-> "10+20+20 = " + (10d+20d+20d));

        log.info("History: {}",calculatorService.listHist() );
        assertThat(calculatorService.listHist()).isNotNull();
        //assertThat(calculatorService.reset()).isNull();
    }



}
