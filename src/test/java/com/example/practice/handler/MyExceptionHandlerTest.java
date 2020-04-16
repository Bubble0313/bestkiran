package com.example.practice.handler;


import com.example.practice.PracticeApplication;
import com.example.practice.controller.HolidayController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {PracticeApplication.class})
@WebAppConfiguration
public class MyExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private HolidayController holidayController = new HolidayController();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).setControllerAdvice(new MyExceptionHandler()).build();
    }

    @Test
    public void testArgumentExceptionHandling() throws Exception {
        mockMvc.perform(get("/holidayhistory/er")).andExpect(MockMvcResultMatchers.status().is4xxClientError())
        .andExpect(MockMvcResultMatchers.content().string("Illegal argument! An integer is expected."));
    }
}