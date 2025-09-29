package com.magdieva.testtask.data.form;

import com.magdieva.testtask.data.PaymentType;
import com.magdieva.testtask.data.internal.PaymentCreationData;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;

@Getter
@Setter
public class PaymentForm {

    @DecimalMin(value = "0.01")
    @DecimalMax(value = "1000000000.00")
    @Digits(integer=10, fraction=2)
    private BigDecimal amount;

    public PaymentCreationData toPaymentCreationData() {
        return new PaymentCreationData(
            PaymentType.DEPOSIT,
            amount,
            Currency.getInstance("EUR"),
            UUID.randomUUID()
        );
    }
}
