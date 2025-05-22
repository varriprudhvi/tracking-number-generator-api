package com.redboxlogistics.tracking.controller;

import com.redboxlogistics.tracking.model.TrackingResponse;
import com.redboxlogistics.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/next-tracking-number")
@RequiredArgsConstructor
@Slf4j
public class TrackingController {

    private final TrackingService trackingService;

    @GetMapping
    public ResponseEntity<TrackingResponse> getNextTrackingNumber(
            @RequestParam String origin_country_id,
            @RequestParam String destination_country_id,
            @RequestParam BigDecimal weight,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime created_at,
            @RequestParam UUID customer_id,
            @RequestParam String customer_name,
            @RequestParam String customer_slug
    ) {
        log.info("Received tracking request for customer: {}", customer_slug);
        TrackingResponse trackingResponse = trackingService.generateTrackingResponse(
                origin_country_id, destination_country_id, weight, created_at, customer_id, customer_name, customer_slug
        );
        return ResponseEntity.ok(trackingResponse);
    }
}
