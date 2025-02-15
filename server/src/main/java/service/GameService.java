package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import resultrequest.*;

import java.util.ArrayList;
import java.util.Locale;

import static dataaccess.MemoryAuthDAO.getAuth;
import static dataaccess.MemoryGameDAO.*;

public class GameService {

    public static GameListResult getGames(GameListRequest gameListRequest) throws DataAccessException {
        String auth = gameListRequest.authToken();
        AuthData authDb = getAuth(auth);
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        ArrayList<GameData> listOfGames = listGames();
        return new GameListResult(listOfGames);
    }

    public static CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String auth = createGameRequest.authToken();
        AuthData authDb = getAuth(auth);
        if (createGameRequest.gameName().isEmpty()) {
            throw new Error400("Error: bad request");
        }
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        String gameName = createGameRequest.gameName();
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(createGameId(),"","",gameName,newGame);
        MemoryGameDAO.createGame(gameData);
        return new CreateGameResult(gameData.gameID());
    }

    public static void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String auth = joinGameRequest.authToken();
        AuthData authDb = getAuth(auth);
        String desiredColor = joinGameRequest.playerColor();
        if (desiredColor.isEmpty() || joinGameRequest.gameID() == null
                || !desiredColor.toLowerCase(Locale.ROOT).equals("black")
                && !desiredColor.toLowerCase(Locale.ROOT).equals("white")) {
            throw new Error400("Error: bad request");
        }
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        GameData game = getGame(joinGameRequest.gameID());
        if (desiredColor.toLowerCase(Locale.ROOT).equals("black")) {
            if (!game.blackUsername().isEmpty()) {
                throw new Error403("Error: already taken");
            }
            GameData updatedGame = new GameData(game.gameID(),game.whiteUsername(),
                    authDb.username(),game.gameName(),game.game());
            updateGame(updatedGame);
        }
        if (desiredColor.toLowerCase(Locale.ROOT).equals("white")) {
            if (!game.whiteUsername().isEmpty()) {
                throw new Error403("Error: already taken");
            }
            GameData updatedGame = new GameData(game.gameID(),authDb.username(),
                    game.blackUsername(),game.gameName(),game.game());
            updateGame(updatedGame);
        }
    }

}
