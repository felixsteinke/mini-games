package com.spring.server.controller;

import com.spring.server.game.Game;
import com.spring.server.game.GameManagement;
import com.spring.server.game.exception.*;
import com.spring.server.model.CreateGameRequest;
import com.spring.server.model.GameExecuteRequest;
import com.spring.server.model.GameStateResponse;
import com.spring.server.model.JoinGameRequest;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class GameController {

    //@Autowired
    private final GameManagement gameManager = new GameManagement();

    @ApiOperation("Creates a new Game.")
    @RequestMapping(value = "/game/create",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> createGame(@RequestBody CreateGameRequest request) {
        try {
            gameManager.createGame(request.getGameName(), request.getPassword(), request.getPlayerCount(), request.getPlayerName());
        } catch (FullGameException | IncorrectPasswordException e) {
            e.printStackTrace();
        }
        return getGameState(request.getGameName(), request.getPassword());
    }

    @ApiOperation("Joins the selected game.")
    @RequestMapping(value = "/game/join", method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> joinGame(@RequestBody JoinGameRequest request) {
        try {
            gameManager.getGame(request.getGameName(), request.getPassword()).join(request.getPlayerName());
        } catch (IncorrectPasswordException | FullGameException e) {
            e.printStackTrace();
        }
        return getGameState(request.getGameName(), request.getPassword());
    }

    @ApiOperation("Returns the information about a Project Card.")
    @RequestMapping(value = "/game/state", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GameStateResponse> getGameState(
            @RequestParam(value = "gameName", defaultValue = "game123") String gameName,
            @RequestParam(value = "password", defaultValue = "password123") String password) {
        Game game = null;
        try {
            game = gameManager.getGame(gameName, password);
        } catch (IncorrectPasswordException e) {
            e.printStackTrace();
        }
        assert game != null;
        return ResponseEntity.ok(new GameStateResponse(
                game.getName(),
                game.getGamePhase(),
                game.getActivePlayer(),
                game.getDice(),
                game.getPlayers(),
                game.getBoard()));
    }

    @ApiOperation("Rolls the dice. Influenced by gameProperty: 'rollWithTwoDices'")
    @RequestMapping(value = "/game/execute/roll",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> executeRoll(@RequestBody GameExecuteRequest request) {
        try {
            accessGame(request).executeRoll(
                    request.getPlayerName(),
                    request.getExecuteProperties().isRollWithTwoDices());
        } catch (SpecialCardException | TurnNotPossibleException | IncorrectPasswordException e) {
            //TODO
            e.printStackTrace();
        }
        return accessGameState(request);
    }

    @ApiOperation("Submits the roll if the players can possibly reset the roll.")
    @RequestMapping(value = "/game/execute/submit-roll",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> submitRoll(@RequestBody GameExecuteRequest request) {
        try {
            accessGame(request).submitRoll(request.getPlayerName());
        } catch (SpecialCardException | TurnNotPossibleException | IncorrectPasswordException e) {
            //TODO
            e.printStackTrace();
        }
        return accessGameState(request);
    }

    @ApiOperation("Skip the special effect of card trading.")
    @RequestMapping(value = "/game/execute/special-card/trade-card/skip",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> skipTradingCard(@RequestBody GameExecuteRequest request) {
        try {
            accessGame(request).skipSpecialCardTrading(request.getPlayerName());
        } catch (TurnNotPossibleException | IncorrectPasswordException e) {
            e.printStackTrace();
        }
        return accessGameState(request);
    }

    @ApiOperation("If a special card get rolled the target player needs to be chosen. Influenced by gameProperty: 'targetPlayerName', 'cardToTradeIn', 'cardToGetFromTrade'")
    @RequestMapping(value = "/game/execute/special-card/trade-card",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> tradeCard(@RequestBody GameExecuteRequest request) {
        try {
            accessGame(request).tradeCard(
                    request.getPlayerName(),
                    request.getExecuteProperties().getTargetPlayerName(),
                    request.getExecuteProperties().getCardToTradeIn(),
                    request.getExecuteProperties().getCardToGetFromTrade());
        } catch (TurnNotPossibleException | IncorrectPasswordException e) {
            e.printStackTrace();
            //TODO
        }
        return accessGameState(request);
    }

    @ApiOperation("If a special card get rolled the target player needs to be chosen. Influenced by gameProperty: 'targetPlayerName'")
    @RequestMapping(value = "/game/execute/special-card/steel-money",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> steelMoney(@RequestBody GameExecuteRequest request) throws TurnNotPossibleException {
        try {
            accessGame(request).steelMoney(
                    request.getPlayerName(),
                    request.getExecuteProperties().getTargetPlayerName());
        } catch (IncorrectPasswordException e) {
            //TODO
            e.printStackTrace();
        }
        return accessGameState(request);
    }

    @ApiOperation("Buy a business card. Influenced by gameProperty: 'buyBusinessCard'")
    @RequestMapping(value = "/game/execute/buy-business-card",
            method = RequestMethod.POST)
    public ResponseEntity<GameStateResponse> buyCard(@RequestBody GameExecuteRequest request) {
        try {
            accessGame(request).executeBuyBusinessCard(request.getPlayerName(), request.getExecuteProperties().getBuyBusinessCard());
        } catch (GameOverException | TurnNotPossibleException | IncorrectPasswordException | CardNotAvailableException e) {
            e.printStackTrace();
            //TODO
        }
        return accessGameState(request);
    }

    private Game accessGame(GameExecuteRequest request) throws IncorrectPasswordException {
        return gameManager.getGame(request.getGameName(), request.getPassword());
    }

    private ResponseEntity<GameStateResponse> accessGameState(GameExecuteRequest request) {
        return getGameState(request.getGameName(), request.getPassword());
    }
}
