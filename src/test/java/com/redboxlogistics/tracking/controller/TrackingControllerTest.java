package com.redboxlogistics.tracking.controller;

import com.redboxlogistics.tracking.model.TrackingResponse;
import com.redboxlogistics.tracking.service.TrackingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackingController.class)
class TrackingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TrackingService trackingService;

    @Test
    void shouldReturnTrackingNumberSuccessfully() throws Exception {

        TrackingResponse mockTrackingResponse = new TrackingResponse();
        String expectedTrackingNumber = "40CA293CD4BE39F1";
        mockTrackingResponse.setTrackingNumber(expectedTrackingNumber);

        Mockito.when(trackingService.generateTrackingResponse(
                        anyString(), anyString(), any(), any(), any(), anyString(), anyString()))
                .thenReturn(mockTrackingResponse);

        mockMvc.perform(get("/next-tracking-number")
                        .param("origin_country_id", "MY")
                        .param("destination_country_id", "ID")
                        .param("weight", "1.234")
                        .param("created_at", "2024-05-21T19:29:32+08:00")
                        .param("customer_id", "de619854-b59b-425e-9db4-943979e1bd49")
                        .param("customer_name", "RedBox Logistics")
                        .param("customer_slug", "redbox-logistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.trackingNumber").exists())
                .andExpect(jsonPath("$.trackingNumber").value(org.hamcrest.Matchers.matchesPattern("^[A-Z0-9]{16}$")))
                .andExpect(jsonPath("$.trackingNumber").value(expectedTrackingNumber));
    }

    @Test
    void shouldReturnBadRequestIfMissingParam() throws Exception {
        mockMvc.perform(get("/next-tracking-number")
                        .param("origin_country_id", "MY"))
                .andExpect(status().isInternalServerError());
    }
}