package ui;

import errors.ResponseException;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GamePlayRepl {

    private final String playerColor;
    private final GamePlayClient client;
    private final Integer gameID;
    private final String authToken;
    private final String url;

    public GamePlayRepl(String playerColor, Integer gameID, String authToken, String url) {
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.authToken = authToken;
        this.url = url;
        client = new GamePlayClient(url, authToken, playerColor, gameID);
    }

    public void run() throws ResponseException {
        client.connect();
        System.out.print("Welcome to game " + gameID + "\n");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            client.printBoard();
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    private void printPrompt() {
        System.out.print("\n" + RESET_TEXT_COLOR + ">>> " + SET_TEXT_COLOR_GREEN);
    }

}
