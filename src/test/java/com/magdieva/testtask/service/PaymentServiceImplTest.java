package com.magdieva.testtask.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.magdieva.testtask.config.PaymentProperties;
import com.magdieva.testtask.data.PaymentType;
import com.magdieva.testtask.data.dto.PaymentCreationResponse;
import com.magdieva.testtask.data.dto.Result;
import com.magdieva.testtask.data.internal.PaymentCreationData;
import com.magdieva.testtask.exception.IntegrationException;
import com.magdieva.testtask.exception.PaymentProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestClient;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Currency;
import java.util.Map;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.jsonResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;


@EnableWireMock({@ConfigureWireMock(port = 8888, baseUrlProperties = {"payment.baseUrl"})})
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@EnableConfigurationProperties(PaymentProperties.class)
@ContextConfiguration(classes = {PaymentServiceImplTest.TestConfig.class, PaymentServiceImpl.class})
class PaymentServiceImplTest {

    @Autowired
    private PaymentProperties paymentProperties;

    @Autowired
    private PaymentService service;

    private final URI expectedUri = URI.create("http://localhost:8080");

    @Test
    void verifyPaymentCreated() {
        stubFor(post("/payments").willReturn(jsonResponse(successfulResponse(), 200)));
        var result = service.createPayment(getPaymentData());
        assertEquals(expectedUri, result);
    }

    @Test
    void verifyExceptionIsThrownOnPaymentFailure() {
        var response = responseWithErrors();
        stubFor(post("/payments").willReturn(jsonResponse(response, 200)));
        var ex = assertThrows(
            PaymentProcessingException.class,
            () -> service.createPayment(getPaymentData())
        );
        assertAll(
            () -> assertTrue(ex.getMessage().contains(response.getResult().getErrorCode())),
            () -> assertTrue(ex.getMessage().contains(response.getResult().getErrorMessage()))
        );
    }

    @Test
    void verifyExceptionIsThrownWhenRedirectUriIsMissing() {
        var response = new PaymentCreationResponse(200, new Result(null, null, null));
        stubFor(post("/payments").willReturn(jsonResponse(response, 200)));
        var ex = assertThrows(
            IllegalStateException.class,
            () -> service.createPayment(getPaymentData())
        );
        assertTrue(ex.getMessage().contains("redirect url is missing"));
    }

    @ParameterizedTest
    @ValueSource(ints = {400, 401, 403, 404, 409, 500, 502, 504})
    void verifyExceptionIsThrownOnError(int statusCode) {
        var response = errorResponse(statusCode);
        stubFor(post("/payments").willReturn(jsonResponse(response, statusCode)));
        var ex = assertThrows(
            IntegrationException.class,
            () -> service.createPayment(getPaymentData())
        );
        assertAll(
            () -> assertTrue(ex.getMessage().contains(response.get("error").toString())),
            () -> assertTrue(ex.getMessage().contains(response.get("message").toString()))
        );
    }

    private PaymentCreationResponse successfulResponse() {
        return new PaymentCreationResponse(200, new Result(expectedUri, null, null));
    }

    private PaymentCreationResponse responseWithErrors() {
        return new PaymentCreationResponse(200, new Result(expectedUri, "3.03", "Acquirer Malfunction"));
    }

    private Map<String, Object> errorResponse(int statusCode) {
        return Map.of(
            "timestamp", "2025-09-29T13:18:41.333+00:00",
            "status", statusCode,
            "error", "some error",
            "message", "some message",
            "path", "/api/v1/payments"
        );
    }

    private PaymentCreationData getPaymentData() {
        return new PaymentCreationData(
            PaymentType.DEPOSIT,
            BigDecimal.ONE,
            Currency.getInstance("EUR"),
            UUID.randomUUID()
        );
    }

    public static class TestConfig {
        @Bean
        public RestClient testRestClient() {
            return RestClient.builder().build();
        }

        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }
}
