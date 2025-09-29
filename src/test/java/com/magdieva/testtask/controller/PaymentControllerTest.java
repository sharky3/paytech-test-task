package com.magdieva.testtask.controller;

import com.magdieva.testtask.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.net.URI;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(PaymentController.class)
class PaymentControllerTest {

    @MockitoBean
    private PaymentService paymentService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void verifyCorrectViewIsReturned() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("payment"));
    }

    @Test
    void verifyCorrectInputHandling() throws Exception {
        when(paymentService.createPayment(any())).thenReturn(URI.create("http://localhost:8181"));

        mockMvc.perform(MockMvcRequestBuilders.post(("/")).param("amount", "100"))
            .andExpect(status().is3xxRedirection());
    }

    @ParameterizedTest
    @ValueSource(strings = {"-100", "sdf", "1.4.6", "1,000", "$35", "?", "0"})
    void verifyInvalidInputHandling(String input) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(("/")).param("amount", input))
            .andExpect(view().name("payment"))
            .andExpect(model().hasErrors());

        verify(paymentService, never()).createPayment(any());
    }
}
