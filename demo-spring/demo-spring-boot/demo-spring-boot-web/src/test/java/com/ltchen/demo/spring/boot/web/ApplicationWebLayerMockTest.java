package com.ltchen.demo.spring.boot.web;

import com.ltchen.demo.spring.boot.web.controller.GreetingController;
import com.ltchen.demo.spring.boot.web.model.Greeting;
import com.ltchen.demo.spring.boot.web.service.GreetingService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author: 01139983
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GreetingController.class)
public class ApplicationWebLayerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GreetingService service;

    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        when(service.greeting("World")).thenReturn(new Greeting(1L, "Hello, World!"));
        this.mockMvc.perform(get("/rest/greeting")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("Hello, World!")));
    }
}
