package com.spring.server.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Properties to modify the workflow execution")
public class GameExecuteRequest {

    @ApiModelProperty(value = "Properties for each step while doing a turn.")
    private final GameExecuteProperties executeProperties = new GameExecuteProperties();
    @ApiModelProperty(value = "Player identity for validation.", example = "player123")
    private String playerName;
    @ApiModelProperty(value = "Required validation for the game.", example = "game123")
    private String gameName;
    @ApiModelProperty(value = "Required validation for the game.", example = "password123")
    private String password;
}
