package com.magdieva.testtask.controller;

import com.magdieva.testtask.data.form.PaymentForm;
import com.magdieva.testtask.service.PaymentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
public class PaymentController {

    private PaymentService paymentService;

    @GetMapping
    public String paymentForm(PaymentForm form) {
        return "payment";
    }

    @PostMapping
    public String createPayment(@Valid @ModelAttribute("paymentForm") PaymentForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "payment";
        }
        var redirectUrl = paymentService.createPayment(form.toPaymentCreationData());
        return "redirect:" + redirectUrl;
    }
}