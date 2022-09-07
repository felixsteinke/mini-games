package com.spring.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
@ApiModel(description = "Request to create a new game.")
public class CreateGameRequest {

    @ApiModelProperty(value = "Game identity.", example = "game123")
    private final String gameName;
    @ApiModelProperty(value = "Game password.", example = "password123")
    private final String password;
    @ApiModelProperty(value = "Player name in this game.", example = "2", allowableValues = "1, 2, 3, 4")
    private final int playerCount;
    @ApiModelProperty(value = "Player name in this game.", example = "player123")
    private final String playerName;
}
