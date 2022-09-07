package com.spring.server.game;

import com.spring.server.game.exception.FullGameException;
import com.spring.server.game.exception.IncorrectPasswordException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class GameManagement {

    //TODO add GameManagement into a database
    @Value("${database-url}")
    private String databaseURL;

    private final Map<String, Game> games;

    public GameManagement() {
        games = new HashMap<>();
    }

    public void createGame(String name, String password, int playerCount, String playerName) throws FullGameException, IncorrectPasswordException {
        Game game = new Game(name, password, playerCount);
        game.join(playerName);
        games.put(name, game);
    }

    public Game getGame(String name, String password) throws IncorrectPasswordException {
        if (games.get(name).checkPassword(password)) {
            return games.get(name);
        } else {
            throw new IncorrectPasswordException(password);
        }
    }

    public void deleteGame(String name, String password) throws IncorrectPasswordException {
        if (games.get(name).checkPassword(password)) {
            games.remove(name);
        } else {
            throw new IncorrectPasswordException(password);
        }
    }
}
