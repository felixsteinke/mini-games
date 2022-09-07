package com.spring.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel(description = "Request to join an existing game.")
public class JoinGameRequest {

    @ApiModelProperty(value = "Game identity.", example = "game123")
    private final String gameName;
    @ApiModelProperty(value = "Game password.", example = "password123")
    private final String password;
    @ApiModelProperty(value = "Player name in this game.", example = "player456")
    private final String playerName;
}
