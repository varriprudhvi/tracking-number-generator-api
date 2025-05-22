package com.redboxlogistics.tracking.service;

import com.redboxlogistics.tracking.exception.ApiException;
import com.redboxlogistics.tracking.model.TrackingResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class TrackingServiceTest {

    private TrackingService trackingService;

    @BeforeEach
    void setUp() {
        trackingService = new TrackingService();
    }

    @Test
    void generateTrackingNumber_shouldReturnNonNullAndValidTrackingNumber() {
        String origin = "MY";
        String destination = "ID";
        BigDecimal weight = new BigDecimal("1.234");
        ZonedDateTime createdAt = ZonedDateTime.now();
        UUID customerId = UUID.randomUUID();
        String customerName = "RedBox Logistics";
        String customerSlug = "redbox-logistics";

        TrackingResponse trackingResponse = trackingService.generateTrackingResponse(
                origin, destination, weight, createdAt, customerId, customerName, customerSlug
        );

        assertThat(trackingResponse).isNotNull();
        assertThat(trackingResponse.getTrackingNumber()).matches("^[A-Z0-9]{16}$");
    }

    @Test
    void generateTrackingNumber_shouldReturnUniqueTrackingNumbersForMultipleCalls() {
        String origin = "MY";
        String destination = "ID";
        BigDecimal weight = new BigDecimal("1.234");
        ZonedDateTime createdAt = ZonedDateTime.now();
        UUID customerId = UUID.randomUUID();
        String customerName = "RedBox Logistics";
        String customerSlug = "redbox-logistics";

        TrackingResponse first = trackingService.generateTrackingResponse(origin, destination, weight, createdAt, customerId, customerName, customerSlug);
        TrackingResponse second = trackingService.generateTrackingResponse(origin, destination, weight, createdAt, customerId, customerName, customerSlug);

        assertThat(first).isNotEqualTo(second);
        assertThat(first.getTrackingNumber()).matches("^[A-Z0-9]{16}$");
        assertThat(second.getTrackingNumber()).matches("^[A-Z0-9]{16}$");
    }

    @Test
    void generateTrackingNumber_withBlankOrigin_throwsAPIException() {
        Assertions.assertThrows(ApiException.class, () -> {
            trackingService.generateTrackingResponse(
                    "", "IN", new BigDecimal("2.123"),
                    ZonedDateTime.now(), UUID.randomUUID(), "Test", "slug"
            );
        });
    }
}
