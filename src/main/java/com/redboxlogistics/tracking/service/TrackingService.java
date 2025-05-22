package com.redboxlogistics.tracking.service;

import com.redboxlogistics.tracking.exception.ApiException;
import com.redboxlogistics.tracking.model.TrackingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

@Service
@Slf4j
public class TrackingService {

    private static final Pattern ISO_COUNTRY_PATTERN = Pattern.compile("^[A-Z]{2}$");

    private final Set<String> generatedNumbers = ConcurrentHashMap.newKeySet();

    public TrackingResponse generateTrackingResponse(String origin, String destination, BigDecimal weight,
                                                     ZonedDateTime createdAt, UUID customerId, String customerName,
                                                     String customerSlug) {

        validateInputs(origin, destination, weight, createdAt, customerId, customerName, customerSlug);

        String base = (origin + destination + customerSlug + createdAt.toEpochSecond()).toUpperCase();
        String hash = UUID.nameUUIDFromBytes(base.getBytes()).toString().replaceAll("-", "").substring(0, 16);
        String trackingNumber = hash.toUpperCase();

        while (!generatedNumbers.add(trackingNumber)) {
            trackingNumber = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16).toUpperCase();
        }

        log.debug("Generated tracking number: {}", trackingNumber);
        return TrackingResponse.builder()
                .trackingNumber(trackingNumber)
                .createdAt(ZonedDateTime.now())
                .originCountryId(origin)
                .destinationCountryId(destination)
                .customerId(customerId)
                .customerName(customerName)
                .customerSlug(customerSlug)
                .weight(weight)
                .status("GENERATED")
                .message("Tracking number generated successfully")
                .build();
    }

    private void validateInputs(String origin, String destination, BigDecimal weight,
                                ZonedDateTime createdAt, UUID customerId,
                                String customerName, String customerSlug) {

        boolean valid = true;
        StringBuilder errorMsg = new StringBuilder("Validation failed for tracking number request: ");

        if (origin == null || !ISO_COUNTRY_PATTERN.matcher(origin).matches()) {
            errorMsg.append("Invalid origin_country_id. ");
            valid = false;
        }

        if (destination == null || !ISO_COUNTRY_PATTERN.matcher(destination).matches()) {
            errorMsg.append("Invalid destination_country_id. ");
            valid = false;
        }

        if (weight == null || weight.scale() > 3) {
            errorMsg.append("Invalid weight (max 3 decimal places). ");
            valid = false;
        }

        if (createdAt == null) {
            errorMsg.append("Missing created_at. ");
            valid = false;
        }

        if (customerId == null) {
            errorMsg.append("Missing customer_id. ");
            valid = false;
        }

        if (customerName == null || customerName.trim().isEmpty()) {
            errorMsg.append("Missing or empty customer_name. ");
            valid = false;
        }

        if (customerSlug == null || customerName.trim().isEmpty()) {
            errorMsg.append("Missing or empty customerSlug. ");
            valid = false;
        }

        if (!valid) {
            log.error(errorMsg.toString());
            throw new ApiException("Invalid or missing parameter(s) for tracking number generation");
        }
    }
}
