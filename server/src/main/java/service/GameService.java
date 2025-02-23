package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import resultrequest.*;
import java.util.ArrayList;
import java.util.Locale;
import dataaccess.AuthDAO;



public class GameService {

    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    GameService(AuthDAO authDAO, GameDAO gameDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public GameListResult getGames(GameListRequest gameListRequest) throws DataAccessException {
        String auth = gameListRequest.authToken();
        AuthData authDb = authDAO.getAuth(auth);
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        ArrayList<GameData> listOfGames = gameDAO.listGames();
        return new GameListResult(listOfGames);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        String auth = createGameRequest.authToken();
        AuthData authDb = authDAO.getAuth(auth);
        if (createGameRequest.gameName().isEmpty()) {
            throw new Error400("Error: bad request");
        }
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        String gameName = createGameRequest.gameName();
        ChessGame newGame = new ChessGame();
        GameData gameData = new GameData(gameDAO.createGameId(),"","",gameName,newGame);
        gameDAO.createGame(gameData);
        return new CreateGameResult(gameData.gameID());
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        String auth = joinGameRequest.authToken();
        AuthData authDb = authDAO.getAuth(auth);
        String desiredColor = joinGameRequest.playerColor();
        if (desiredColor.isEmpty() || joinGameRequest.gameID() == null
                || !desiredColor.toLowerCase(Locale.ROOT).equals("black")
                && !desiredColor.toLowerCase(Locale.ROOT).equals("white")) {
            throw new Error400("Error: bad request");
        }
        if (authDb == null) {
            throw new Error401("Error: unauthorized");
        }
        GameData game = gameDAO.getGame(joinGameRequest.gameID());
        if (desiredColor.toLowerCase(Locale.ROOT).equals("black")) {
            if (!game.blackUsername().isEmpty()) {
                throw new Error403("Error: already taken");
            }
            GameData updatedGame = new GameData(game.gameID(),game.whiteUsername(),
                    authDb.username(),game.gameName(),game.game());
            gameDAO.updateGame(updatedGame);
        }
        if (desiredColor.toLowerCase(Locale.ROOT).equals("white")) {
            if (!game.whiteUsername().isEmpty()) {
                throw new Error403("Error: already taken");
            }
            GameData updatedGame = new GameData(game.gameID(),authDb.username(),
                    game.blackUsername(),game.gameName(),game.game());
            gameDAO.updateGame(updatedGame);
        }
    }

}
