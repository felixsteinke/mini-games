package com.spring.server.controller;

import com.spring.server.ServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static testutils.RequestGenerator.testInvalidGetRequest;
import static testutils.RequestGenerator.testValidGetRequest;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ServerApplication.class})
@WebAppConfiguration
public class CardDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void businessCardValid() throws Exception {
        testValidGetRequest(
                mockMvc,
                "/data/business-card?name=WEIZENFELD",
                "DataController/weizenfeld.json");
    }

    @Test
    void allBusinessCardsValid() throws Exception {
        testValidGetRequest(
                mockMvc,
                "/data/all/business-card",
                "DataController/allBusinessCards.json");
    }

    @Test
    void projectCardValid() throws Exception {
        testValidGetRequest(
                mockMvc,
                "/data/project-card?name=BAHNHOF",
                "DataController/bahnhof.json");
    }

    @Test
    void allProjectCardsValid() throws Exception {
        testValidGetRequest(
                mockMvc,
                "/data/all/project-card",
                "DataController/allProjectCards.json");
    }

    @Test
    void businessCardInvalid() throws Exception {
        testInvalidGetRequest(
                mockMvc,
                "/data/business-card?name=noName",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    void projectCardInvalid() throws Exception {
        testInvalidGetRequest(
                mockMvc,
                "/data/project-card?name=noName",
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
