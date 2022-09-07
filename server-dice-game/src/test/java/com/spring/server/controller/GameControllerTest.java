package com.spring.server.controller;

import com.spring.server.ServerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static testutils.RequestGenerator.testValidPostRequest;
import static testutils.RequestGenerator.testValidPostRequestOk;

@AutoConfigureMockMvc
@SpringBootTest(classes = {ServerApplication.class})
@WebAppConfiguration
public class GameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createGame() throws Exception {
        testValidPostRequest(
                mockMvc,
                "/game/create",
                "GameController/createGame");
    }

    @Test
    void startGame() throws Exception {
        testValidPostRequestOk(
                mockMvc,
                "/game/create",
                "GameController/startGame/create.json");
        testValidPostRequest(
                mockMvc,
                "/game/join",
                "GameController/startGame");
    }
}
