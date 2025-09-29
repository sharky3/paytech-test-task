package com.magdieva.testtask.service;

import com.magdieva.testtask.data.internal.PaymentCreationData;

import java.net.URI;

public interface PaymentService {

    /**
     * @throws com.magdieva.testtask.exception.IntegrationException in case of 4xx or 5xx error from paytech
     * @throws com.magdieva.testtask.exception.PaymentProcessingException if paytech returned internal error code
     * @throws IllegalStateException if error response does not conform to the expected format
     */
    URI createPayment(PaymentCreationData paymentData);

}
