package com.spring.server.game.cardOwner;

import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.exception.CardNotAvailableException;

import java.util.HashMap;
import java.util.Map;

import static com.spring.server.game.card.entity.BusinessCardEntity.*;

public class Board {

    private final Map<BusinessCardEntity, Integer> board = new HashMap<>();

    public Board(int playerCount) {
        board.put(WEIZENFELD, playerCount * 2);
        board.put(BAUERNHOF, playerCount * 2);
        board.put(BAECKEREI, playerCount * 2);
        board.put(CAFE, playerCount * 2);
        board.put(MINI_MARKT, playerCount * 2);
        board.put(WALD, playerCount * 2);
        board.put(STADION, playerCount);
        board.put(FERNSEHSENDER, playerCount);
        board.put(BUEROHAUS, playerCount);
        board.put(MOLKEREI, playerCount * 2);
        board.put(MOEBELFABRIK, playerCount * 2);
        board.put(BERGWERK, playerCount * 2);
        board.put(FAMILIENRESTAURANT, playerCount * 2);
        board.put(APFELPLANTAGE, playerCount * 2);
        board.put(MARKTHALLE, playerCount * 2);
    }

    public Map<BusinessCardEntity, Integer> getBoard() {
        return this.board;
    }

    public boolean isCardAvailable(BusinessCardEntity card) {
        return board.get(card) > 0;
    }

    public void buyCard(BusinessCardEntity card) throws CardNotAvailableException {
        if (isCardAvailable(card)) {
            board.put(card, board.get(card) - 1);
        } else {
            throw new CardNotAvailableException(card, "No more cards on the board.");
        }
    }
}
