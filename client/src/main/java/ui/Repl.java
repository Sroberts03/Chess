package ui;

import java.util.Scanner;
import static ui.EscapeSequences.*;

public class Repl {
    private final ui.ChessClient client;

    public Repl(String serverUrl) {
        client = new ui.ChessClient(serverUrl);
    }

    public void run() {
        System.out.println(" Welcome to Chess. Sign in to start.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
                if (client.getGameJoined() != 0) {
                    GamePlayRepl gamePlay = new GamePlayRepl(client.getPlayerColor(), client.getGameJoined(),
                            client.getAuthToken(), client.getServerUrl(), false);
                    gamePlay.run();
                    client.setGameJoined();
                }
                if (client.getObservingGame() != 0) {
                    GamePlayRepl obs = new GamePlayRepl("white", client.getObservingGame(),
                            client.getAuthToken(), client.getServerUrl(), true);
                    obs.run();
                    client.resetObservingGame();
                }
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
