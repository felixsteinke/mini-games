package com.spring.server.game.card;

import com.spring.server.game.Dice;
import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.cardOwner.Player;
import com.spring.server.game.exception.SpecialCardException;
import com.spring.server.game.exception.TurnNotPossibleException;

import java.util.ArrayList;
import java.util.List;

import static com.spring.server.game.card.entity.BusinessCardEntity.*;
import static com.spring.server.game.card.entity.ProjectCardEntity.EINKAUFSZENTRUM;

public class CardLogic {

    private static final CardData cardData = new CardData();

    public static void updatePlayers(Player[] players, int activePlayer, Dice dice) throws SpecialCardException, TurnNotPossibleException {
        for (BusinessCardEntity businessCard : findRelevantCards(dice.getValue())) {
            switch (businessCard) {
                case WEIZENFELD:
                case BAUERNHOF:
                case WALD:
                    processBlueCard(players, businessCard, 1);
                    break;
                case BAECKEREI:
                    processBasicGreenCard(players, activePlayer, businessCard, 1);
                    break;
                case CAFE:
                    processRedCard(players, activePlayer, businessCard, 1);
                    break;
                case MINI_MARKT:
                    processBasicGreenCard(players, activePlayer, businessCard, 4);
                    break;
                case STADION:
                    for (Player player : players) {
                        int stollen = player.steelMoney(2);
                        players[activePlayer].addMoney(stollen);
                    }
                    break;
                case FERNSEHSENDER:
                    throw new SpecialCardException(FERNSEHSENDER);
                case BUEROHAUS:
                    throw new SpecialCardException(BUEROHAUS);
                case MOLKEREI:
                    BusinessCardEntity[] requiredCards1 = {BAUERNHOF};
                    processAdvancedGreenCard(players, activePlayer, businessCard, requiredCards1, 3);
                    break;
                case MOEBELFABRIK:
                    BusinessCardEntity[] requiredCards2 = {WALD, BERGWERK};
                    processAdvancedGreenCard(players, activePlayer, businessCard, requiredCards2, 3);
                    break;
                case BERGWERK:
                    processBlueCard(players, businessCard, 5);
                    break;
                case FAMILIENRESTAURANT:
                    processRedCard(players, activePlayer, businessCard, 2);
                    break;
                case APFELPLANTAGE:
                    processBlueCard(players, businessCard, 3);
                    break;
                case MARKTHALLE:
                    BusinessCardEntity[] requiredCards3 = {WEIZENFELD, APFELPLANTAGE};
                    processAdvancedGreenCard(players, activePlayer, businessCard, requiredCards3, 3);
                    break;
                default:
                    throw new TurnNotPossibleException("BusinessCard not fround: " + businessCard);
            }
        }
    }

    private static void processBlueCard(Player[] players, BusinessCardEntity card, int moneyValue) {
        for (Player player : players) {
            if (player.hasBusiness(card) > 0) {
                player.addMoney(moneyValue * player.hasBusiness(card));
            }
        }
    }

    private static void processAdvancedGreenCard(Player[] players, int activePlayer, BusinessCardEntity card, BusinessCardEntity[] requiredCards, int moneyValue) {
        if (players[activePlayer].hasBusiness(card) > 0) {
            //count the multiplier
            int countRequiredCards = 0;
            for (BusinessCardEntity requiredCard : requiredCards) {
                if (players[activePlayer].hasBusiness(requiredCard) > 0) {
                    countRequiredCards += players[activePlayer].hasBusiness(requiredCard);
                }
            }
            players[activePlayer].addMoney(moneyValue * countRequiredCards * players[activePlayer].hasBusiness(card));
        }
    }

    private static void processBasicGreenCard(Player[] players, int activePlayer, BusinessCardEntity card, int moneyValue) {
        if (players[activePlayer].hasBusiness(card) > 0) {
            if (players[activePlayer].hasProject(EINKAUFSZENTRUM)) {
                moneyValue += 1;
            }
            players[activePlayer].addMoney(moneyValue * players[activePlayer].hasBusiness(card));
        }
    }

    private static void processRedCard(Player[] players, int activePlayer, BusinessCardEntity card, int moneyValue) {
        // pointer takes all players and skips the active one
        int pointer = activePlayer - 1;
        while (activePlayer != pointer) {
            if (players[pointer].hasBusiness(card) > 0) {
                int steelValue = moneyValue;
                if (players[activePlayer].hasProject(EINKAUFSZENTRUM)) {
                    steelValue += 1;
                }
                steelValue *= players[pointer].hasBusiness(card);
                //if active player has no more money, you cant steel more
                int stollen = players[activePlayer].steelMoney(steelValue);
                players[pointer].addMoney(stollen);
                if (steelValue > stollen) {
                    break;
                }
            }
            //priority order is against the cock
            pointer--;
            if (pointer < 0) {
                pointer = players.length - 1;
            }
        }
    }

    private static List<BusinessCardEntity> findRelevantCards(int diceValue) {
        List<BusinessCardEntity> relevantCards = new ArrayList<>();
        for (BusinessCard card : cardData.getAllBusinessCards().values()) {
            for (int rollEntity : card.getRollEntity()) {
                if (rollEntity == diceValue) {
                    relevantCards.add(card.getEntity());
                    break;
                }
            }
        }
        return relevantCards;
    }
}
