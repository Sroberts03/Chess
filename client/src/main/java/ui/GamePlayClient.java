package ui;

import java.util.Arrays;

public class GamePlayClient {

    public String eval(String input) {
        try {
            var tokens = input.split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd.toUpperCase()) {
//                case "L" -> Leave();
//                case "M" -> makeMove(params);
//                case "H" -> highlight(params);
                default -> help();
            };
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public String help() {
        return """
                L -> leave game
                M <Column> <Row> -> make move to column and row
                H <Column> <Row> -> Highlight Possible Moves of Piece at Column and Row
                """;
    }
}
