package com.redboxlogistics.tracking.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrackingResponse {

    private String trackingNumber;
    private ZonedDateTime createdAt;
    private String originCountryId;
    private String destinationCountryId;
    private UUID customerId;
    private String customerName;
    private String customerSlug;
    private BigDecimal weight;
    private String status;
    private String message;
}
