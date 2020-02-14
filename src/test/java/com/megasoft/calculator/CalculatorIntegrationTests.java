package com.megasoft.calculator;


import com.megasoft.calculator.domain.Calculator;
import com.megasoft.calculator.dto.CalculatorRequest;
import com.megasoft.calculator.repository.CalculatorRepository;
import com.megasoft.calculator.resources.CalculatorResource;
import com.megasoft.calculator.service.CalculatorService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.shell.jline.InteractiveShellApplicationRunner;
import org.springframework.shell.jline.ScriptShellApplicationRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        InteractiveShellApplicationRunner.SPRING_SHELL_INTERACTIVE_ENABLED + "=false",
        ScriptShellApplicationRunner.SPRING_SHELL_SCRIPT + ".enabled=false"
})
@ExtendWith(MockitoExtension.class)
@DisplayName("Calculator Integration Tests")
@Slf4j
public class CalculatorIntegrationTests {

    @Autowired
    private CalculatorRepository calculatorRepository;

    @Mock
    CalculatorService calculatorService;

    @Autowired
    private CalculatorResource calculatorResource;

    private Calculator calculator;

    MockMvc mockMvc;

    @BeforeEach
    void setupMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorResource).build();
    }

    @Before
    void setUp() throws Exception {
        //MockitoAnnotations.initMocks(this);
        Mockito.mockitoSession()
                .initMocks(this)
                .strictness(Strictness.STRICT_STUBS)
                .startMocking();

    }

    @DisplayName("Testing should return calculation results object after calculation")
    @Test
    public void testShouldReturnWhenACalculationIsSaved() throws Exception {
        CalculatorRequest calculatorRequest = new CalculatorRequest();
        calculatorRequest.setCommand("add");
        calculatorRequest.setValues(new Double[]{1d, 2d, 3d});

        String expectedJson = new StringBuilder()//
                .append("{id: 1, operation: \"1.0+2.0+3.0\", result: 6.0}")
                .toString();

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonCalc;
        try {
            jsonCalc = objectMapper.writeValueAsString(calculatorRequest);

        mockMvc.perform(post("/api/operation").contentType(MediaType.APPLICATION_JSON).content(jsonCalc))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

        }catch (IOException e){
            e.printStackTrace();
        }

    }

    @DisplayName("Testing get calculator history")
    @Test
    public void testGetCalculatorHistory() {

        ResponseEntity<List<Calculator>> response = calculatorResource.history();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @DisplayName("Testing returns all available histories")
    @Test
    void returnsAllAvailableCalculatorHistories() throws Exception {

        when(calculatorService.listHist()).thenReturn(Arrays.asList(
                new Calculator(1L,"4.0+4.0",8d),
                new Calculator(2L,"10.0+5.0",15d)));

        List allCalcHistory = calculatorService.listHist();

        assertNotNull(allCalcHistory);
        assertEquals(2, allCalcHistory.size());

    }


    @DisplayName("Testing Reset Calculator history")
    @Test
    public void testReset() {
        CalculatorRequest calculatorRequest = new CalculatorRequest();
        calculatorRequest.setCommand("reset");

        ResponseEntity<Calculator> response = calculatorResource.calculation(calculatorRequest);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(null);
    }



}
