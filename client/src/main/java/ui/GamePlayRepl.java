package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GamePlayRepl {

    private String playerColor;
    private GamePlayClient client;
    private Integer gameID;

    public GamePlayRepl(String playerColor, Integer gameID) {
        this.playerColor = playerColor;
        client = new GamePlayClient();
        this.gameID = gameID;
    }

    public void run() {
        System.out.print("Welcome to game " + gameID + "\n");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = client.eval(line);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
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
