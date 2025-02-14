package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.Error400;
import dataaccess.Error401;
import dataaccess.MemoryGameDAO;
import model.GameData;
import resultrequest.*;
import java.util.ArrayList;
import static dataaccess.MemoryAuthDAO.getAuth;
import static dataaccess.MemoryGameDAO.createGameId;
import static dataaccess.MemoryGameDAO.listGames;

public class GameService {

    public static GameListResult getGames(GameListRequest gameListRequest) throws DataAccessException {
        String auth = gameListRequest.authToken();
        String authDb = getAuth(auth);
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        ArrayList<GameData> listOfGames = listGames();
        return new GameListResult(listOfGames);
    }

    public static CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String auth = createGameRequest.authToken();
        String authDb = getAuth(auth);
        if (createGameRequest.gameName().isEmpty()) {
            throw new Error400("Error: bad request");
        }
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        String gameName = createGameRequest.gameName();
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(createGameId(),null,null,gameName,newGame);
        MemoryGameDAO.createGame(gameData);
        return new CreateGameResult(gameData.gameID());
    }

    public static void joinGame(JoinGameRequest joinGameRequest) {

    }
}
