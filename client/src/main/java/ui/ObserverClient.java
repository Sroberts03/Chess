package ui;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ObserverClient {

    GamePlayClient boardPrinter;

    public ObserverClient() {
        boardPrinter = new GamePlayClient();
    }

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd.toUpperCase()) {
                case "L" -> leave();
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String leave() {
        return "quit";
    }

    public void printBoard() {
        boardPrinter.printBoard("White");
    }

    public String help() {
        return """
                Press any key for Help
                L -> leave game
                """;
    }
}
