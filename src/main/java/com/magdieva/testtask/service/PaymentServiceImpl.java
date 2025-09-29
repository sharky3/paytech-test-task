package com.magdieva.testtask.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.magdieva.testtask.config.PaymentProperties;
import com.magdieva.testtask.data.dto.ErrorDto;
import com.magdieva.testtask.data.dto.Result;
import com.magdieva.testtask.data.internal.PaymentCreationData;
import com.magdieva.testtask.data.dto.PaymentCreationResponse;
import com.magdieva.testtask.exception.IntegrationException;
import com.magdieva.testtask.exception.PaymentProcessingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private RestClient restClient;
    private ObjectMapper objectMapper;
    private PaymentProperties paymentProperties;

    public URI createPayment(PaymentCreationData paymentData) {
        log.debug("Creating payment: {}", paymentData);
        var paymentCreationResponse = restClient
            .post()
            .uri(paymentProperties.getBaseUrl() + "/payments")
            .contentType(MediaType.APPLICATION_JSON)
            .body(paymentData.toPaymentCreationDto())
            .retrieve()
            .onStatus(HttpStatusCode::isError, (request, response) -> {
                var body = new String(response.getBody().readAllBytes());
                var errorData = toErrorDto(body);
                throw new IntegrationException(errorData.getError(), errorData.getMessage());
            })
            .toEntity(PaymentCreationResponse.class);

        var result = Optional.ofNullable(paymentCreationResponse.getBody()).map(PaymentCreationResponse::getResult);
        result.map(Result::getErrorCode).ifPresent(errorCode -> {
            throw new PaymentProcessingException(
                result.map(Result::getErrorCode).get(),
                result.map(Result::getErrorMessage).orElse("payment failed")
            );
        });
        return result.map(Result::getRedirectUrl).orElseThrow(
            () -> new IllegalStateException("redirect url is missing")
        );
    }

    private ErrorDto toErrorDto(String body) {
        try {
            return objectMapper.readValue(body, ErrorDto.class);
        } catch (JsonProcessingException ex) {
            log.error("Wrong data format {}", body, ex);
            throw new IllegalStateException("Internal Error", ex);
        }
    }
}
