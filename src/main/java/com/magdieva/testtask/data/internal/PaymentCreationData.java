package com.magdieva.testtask.data.internal;

import com.magdieva.testtask.data.PaymentType;
import com.magdieva.testtask.data.dto.CustomerDto;
import com.magdieva.testtask.data.dto.PaymentCreationDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentCreationData {
    private PaymentType paymentType;
    private BigDecimal amount;
    private Currency currency;
    private UUID customerId;

    public PaymentCreationDto toPaymentCreationDto() {
        return new PaymentCreationDto(
            paymentType,
            amount,
            currency.getCurrencyCode(),
            new CustomerDto(customerId)
        );
    }
}