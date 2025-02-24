package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import resultrequest.*;

public class GameServiceTests {

    private final GameDAO gameDAO = new MemoryGameDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final UserDAO userDAO = new MemoryUserDAO();
    private final GameService gameService = new GameService(authDAO, gameDAO);
    private final ClearAppService clearApp = new ClearAppService(gameDAO,authDAO,userDAO);


    @Test
    @DisplayName("getGames Authorized")
    public void getGamesTestAuthorized() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, null, null,
                "test",game);
        gameDAO.createGame(gameData);
        ChessGame game2 = new ChessGame();
        GameData gameData2 = new GameData(123, null, null,
                "test",game2);
        gameDAO.createGame(gameData2);
        boolean thrown = false;
        GameListRequest request = new GameListRequest("123");
        GameListResult result = null;
        try {
            result = gameService.getGames(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert result.gameList().equals(gameDAO.listGames());
    }

    @Test
    @DisplayName("getGames Unauthorized")
    public void getGamesTestUnauthorized() throws DataAccessException {
        clearApp.clearApp();
        boolean thrown = false;
        GameListRequest request = new GameListRequest("123");
        try {
            gameService.getGames(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("createGame Authorized")
    public void createGameTest() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        boolean thrown = false;
        CreateGameResult result = null;
        try {
            result = gameService.createGame(new CreateGameRequest(auth.authToken(), "123"));
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert result != null;
    }

    @Test
    @DisplayName("createGame Unauthorized")
    public void createGameTestUnauthorized() throws DataAccessException {
        clearApp.clearApp();
        boolean thrown = false;
        try {
            gameService.createGame(new CreateGameRequest("testGame", "123"));
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("createGame Bad Request")
    public void createGameTestBadRequest() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        boolean thrown = false;
        try {
            gameService.createGame(new CreateGameRequest("", "123"));
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Happy Path for Black")
    public void joinGameTestHappyPathBlack() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("black",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert !gameDAO.getGameMap().get(gameData.gameID()).blackUsername().isEmpty();
        assert gameDAO.getGameMap().get(gameData.gameID()).blackUsername().equals("sam");
    }

    @Test
    @DisplayName("joinGame Happy Path for White")
    public void joinGameTestHappyPathWhite() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("White",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            thrown = true;
        }
        assert !thrown;
        assert !gameDAO.getGameMap().get(gameData.gameID()).whiteUsername().isEmpty();
        assert gameDAO.getGameMap().get(gameData.gameID()).whiteUsername().equals("sam");
    }


    @Test
    @DisplayName("joinGame Unauthorized")
    public void joinGameTestUnauthorized() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("White",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: unauthorized")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Bad Request Empty Player Color")
    public void joinGameTestBadRequestEmptyPlayer() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Bad Request Empty gameId")
    public void joinGameTestBadRequestEmptyGameId() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("black",null,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Bad Request Wrong Color")
    public void joinGameTestBadRequestWrongColor() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("purple",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: bad request")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Already Taken White")
    public void joinGameTestAlreadyTakenWhite() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "henry", "",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("white",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                thrown = true;
            }
        }
        assert thrown;
    }

    @Test
    @DisplayName("joinGame Already Taken Black")
    public void joinGameTestAlreadyTakenBlack() throws DataAccessException {
        clearApp.clearApp();
        AuthData auth = new AuthData("123", "sam");
        authDAO.createAuth(auth);
        ChessGame game = new ChessGame();
        GameData gameData = new GameData(123, "", "Henry",
                "test",game);
        gameDAO.createGame(gameData);
        boolean thrown = false;
        JoinGameRequest request = new JoinGameRequest("black",123,auth.authToken());
        try {
            gameService.joinGame(request);
        } catch (DataAccessException e) {
            if (e.getMessage().equals("Error: already taken")) {
                thrown = true;
            }
        }
        assert thrown;
    }
}
