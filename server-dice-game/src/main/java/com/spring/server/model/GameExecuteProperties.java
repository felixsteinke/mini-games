package com.spring.server.model;

import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.card.entity.ProjectCardEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(description = "Properties to modify the workflow execution")
public class GameExecuteProperties {

    @ApiModelProperty(value = "Needed to roll the dice. Default: 'false'", example = "true")
    private boolean rollWithTwoDices = false;
    @ApiModelProperty(value = "Set this to true if you want to skip the buy phase. Default: 'false'", example = "true")
    private boolean skipBuyPhase = false;
    @ApiModelProperty(value = "Target player to trade a card or steel money.", example = "player123")
    private String targetPlayerName;
    @ApiModelProperty(value = "Card entity you will lose after the trade.", example = "WEIZENFELD")
    private BusinessCardEntity cardToTradeIn;
    @ApiModelProperty(value = "Card entity you will get after the trade.", example = "BERGWERK")
    private BusinessCardEntity cardToGetFromTrade;
    @ApiModelProperty(value = "Card you want to buy from the board for money.", example = "BAUERNHOF")
    private BusinessCardEntity buyBusinessCard;
    @ApiModelProperty(value = "Card you want to buy from the big projects for money.", example = "BAHNHOF")
    private ProjectCardEntity buyProjectCard;
}
