package com.example.practice.Handler;

import com.example.practice.PracticeApplication;
import com.example.practice.controller.HolidayController;
import com.example.practice.repository.HolidayRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration (classes = { PracticeApplication.class })
@WebAppConfiguration
class NotFoundAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HolidayController holidayController = new HolidayController();

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).setControllerAdvice(new NotFoundAdvice()).build();
    }

    @Test
    public void testNullPointerExceptionHandling() throws Exception {
        Mockito.when(holidayController.findHistoryById(1)).thenThrow(new NullPointerException("No such history"));
        mockMvc.perform(MockMvcRequestBuilders.get("/holidayhistory/1")).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}