package com.spring.server.game;

import com.spring.server.game.card.CardData;
import com.spring.server.game.card.CardLogic;
import com.spring.server.game.card.entity.BusinessCardEntity;
import com.spring.server.game.card.entity.ProjectCardEntity;
import com.spring.server.game.cardOwner.Board;
import com.spring.server.game.cardOwner.Player;
import com.spring.server.game.exception.*;

import java.util.Map;

import static com.spring.server.game.GamePhase.*;

public class Game {

    private final CardData cardData;

    private final String name;
    private final String password;

    private final Player[] players;
    private final Board board;

    private Dice dice;
    private int pointer;
    private GamePhase phase;

    public Game(String name, String password, int playerCount) {
        this.cardData = new CardData();
        this.name = name;
        this.password = password;
        this.players = new Player[playerCount];
        this.board = new Board(playerCount);
        this.pointer = 0;
        this.phase = GamePhase.VORBEREITUNG;
    }

    /**
     * Takes the next free slot in the game. If the same player name already exists, a player will take his slot.<br>
     * This makes rejoining available.<br>
     * <ul>
     *     <li>Game Phase: {@link GamePhase#VORBEREITUNG}</li>
     *     <li>When the game is full: Game Phase: {@link GamePhase#WUERFELN}</li>
     * </ul>
     *
     * @param playerName player identification
     * @throws IncorrectPasswordException Permission denied
     * @throws FullGameException          Game is full and can not be joined
     */
    public void join(String playerName) throws IncorrectPasswordException, FullGameException {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == null) {
                players[i] = new Player(playerName);
                if (i == players.length - 1) {
                    phase = GamePhase.WUERFELN;
                }
                return;
            } else if (players[i].getName().equals(playerName)) {
                return;
            }
        }
        throw new FullGameException(this.name, this.players.length);
    }

    /**
     * <ol>
     *     <li>Validates the execute request.</li>
     *     <li>Validates the availability of the card.</li>
     *     <li>Purchases the card. Books on the Player and Board.</li>
     *     <li>Checks if the game is over.</li>
     *     <li>Next player can roll the dice.</li>
     * </ol>
     *
     * @param playerName   player identification
     * @param rollTwoDices option to roll with two dices, if project is purchased.
     * @throws TurnNotPossibleException retry the turn with other parameters
     */
    public void executeRoll(String playerName, boolean rollTwoDices) throws TurnNotPossibleException, SpecialCardException {
        checkPossibleTurn(playerName, WUERFELN);
        // checks if rollTwoDices is available when it is selected
        if (rollTwoDices && !players[pointer].hasProject(ProjectCardEntity.BAHNHOF)) {
            throw new TurnNotPossibleException("Player (" + players[pointer].getName() + ") can not roll with two dices.");
        }

        dice = new Dice(rollTwoDices);
        phase = GamePhase.GEWUERFELT;
        // basic roll is finished
        if (!players[pointer].hasProject(ProjectCardEntity.FUNKTURM)) {
            submitRoll(playerName);
        }
    }

    public void submitRoll(String playerName) throws TurnNotPossibleException, SpecialCardException {
        checkPossibleTurn(playerName, GEWUERFELT);
        try {
            CardLogic.updatePlayers(players, pointer, dice);
        } catch (SpecialCardException e) {
            if (e.getCardEntity() == BusinessCardEntity.FERNSEHSENDER) {
                phase = VERARBEITUNG_FERNSEHSENDER;
            } else if (e.getCardEntity() == BusinessCardEntity.BUEROHAUS) {
                phase = VERARBEITUNG_BUEROHAUS;
            }
            throw e;
        }
        if (players[pointer].hasProject(ProjectCardEntity.FREIZEITPARK) && dice.isDouble()) {
            phase = WUERFELN;
        } else {
            phase = KAUFEN;
        }
    }

    public void skipSpecialCardTrading(String playerName) throws TurnNotPossibleException {
        checkPossibleTurn(playerName, VERARBEITUNG_BUEROHAUS);
        phase = KAUFEN;
    }

    public void tradeCard(String playerName, String targetPlayer, BusinessCardEntity giveCard, BusinessCardEntity getCard) throws TurnNotPossibleException {
        checkPossibleTurn(playerName, VERARBEITUNG_BUEROHAUS);
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(targetPlayer)) {
                if (player.hasBusiness(getCard) > 0 && players[pointer].hasBusiness(giveCard) > 0) {
                    //TODO trade
                    phase = KAUFEN;
                    return;
                } else {
                    throw new TurnNotPossibleException("Cards for trade not available.");
                }
            }
        }
        throw new TurnNotPossibleException("Target player not found.");
    }

    public void steelMoney(String playerName, String targetPlayer) throws TurnNotPossibleException {
        checkPossibleTurn(playerName, VERARBEITUNG_FERNSEHSENDER);
        for (Player player : players) {
            if (player.getName().equalsIgnoreCase(targetPlayer)) {
                int stollen = player.steelMoney(5);
                players[pointer].addMoney(stollen);
                phase = KAUFEN;
                break;
            }
        }
        //TODO check for card trading
        throw new TurnNotPossibleException("Target player not found.");
    }

    /**
     * <ol>
     *     <li>Validates the execute request.</li>
     *     <li>Validates the availability of the card.</li>
     *     <li>Purchases the card. Books on the Player and Board.</li>
     *     <li>Checks if the game is over.</li>
     *     <li>Next player can roll the dice.</li>
     * </ol>
     * <ul>
     *     <li>Game Phase: {@link GamePhase#KAUFEN}</li>
     *     <li>When the purchase is successful: Game Phase: {@link GamePhase#WUERFELN} for the next player.</li>
     * </ul>
     *
     * @param playerName player identification
     * @param card       card to be purchased
     * @throws TurnNotPossibleException the player needs the repeat the turn.
     * @throws GameOverException        the game is over and can be deleted.
     */
    public void executeBuyBusinessCard(String playerName, BusinessCardEntity card) throws TurnNotPossibleException, GameOverException, CardNotAvailableException {
        checkPossibleTurn(playerName, KAUFEN);
        int cardPrice = cardData.getBusinessCard(card).getPrice();
        if (board.isCardAvailable(card) && players[pointer].getMoney() >= cardPrice) {
            board.buyCard(card);
            players[pointer].bookPurchase(cardPrice);
            players[pointer].addBusiness(card);
        } else {
            throw new TurnNotPossibleException("Card can not be purchased. Try again.");
        }
        buyPhaseTearDown();
    }

    public void executeBuyProjectCard(String playerName, ProjectCardEntity card) throws TurnNotPossibleException, GameOverException, CardNotAvailableException {
        checkPossibleTurn(playerName, KAUFEN);
        int cardPrice = cardData.getProjectCard(card).getPrice();
        if (players[pointer].getMoney() >= cardPrice) {
            players[pointer].bookPurchase(cardPrice);
            players[pointer].addProject(card);
        } else {
            throw new TurnNotPossibleException("Card can not be purchased. Try again.");
        }
        buyPhaseTearDown();
    }

    private void buyPhaseTearDown() throws GameOverException {
        try {
            checkIfGameIsOver();
        } catch (GameOverException e) {
            phase = BEENDET;
            throw e;
        }
        dice = null;
        pointer = pointer == players.length ? 0 : pointer++;
    }

    /*
    Validation methods
     */

    private void checkIfGameIsOver() throws GameOverException {
        for (boolean ownedProject :
                players[pointer].getAllProjects().values()) {
            if (!ownedProject) {
                return;
            }
        }
        throw new GameOverException(players[pointer]);
    }

    private void checkPossibleTurn(String playerName, GamePhase phase) throws TurnNotPossibleException {
        if (!playerName.equals(this.players[pointer].getName())) {
            throw new TurnNotPossibleException("Only '" + players[pointer].getName() + "' can do a turn.");
        }
        if (!this.phase.equals(phase)) {
            throw new TurnNotPossibleException("Wrong game phase. Found: '" + phase + "'. Expected: '" + this.phase + "'.");
        }
    }

    public boolean checkPassword(String password) throws IncorrectPasswordException {
        if (this.password.equals(password)) {
            return true;
        } else {
            throw new IncorrectPasswordException(password);
        }
    }

    /*
    Getter methods to create the game state
     */

    public String getName() {
        return this.name;
    }

    public GamePhase getGamePhase() {
        return this.phase;
    }

    public String getActivePlayer() {
        return this.players[pointer].getName();
    }

    public Dice getDice() {
        return this.dice;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public Map<BusinessCardEntity, Integer> getBoard() {
        return this.board.getBoard();
    }
}
