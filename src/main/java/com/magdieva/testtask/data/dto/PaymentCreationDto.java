package com.magdieva.testtask.data.dto;

import com.magdieva.testtask.data.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentCreationDto {
    private PaymentType paymentType;
    private BigDecimal amount;
    private String currency;
    private CustomerDto customer;
}