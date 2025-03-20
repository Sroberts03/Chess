package ui;

import java.util.Arrays;

import static ui.EscapeSequences.*;

public class ObserverClient {

    public ObserverClient() {

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
        whiteBoard();
    }

    private void whiteBoard() {
        whiteStartandEndBoard();
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 8 ");
        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_RED + " R ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " N ");
        System.out.print(SET_BG_COLOR_WHITE + " B ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " Q ");
        System.out.print(SET_BG_COLOR_WHITE + " K ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " B ");
        System.out.print(SET_BG_COLOR_WHITE + " N ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " R ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 8 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 7 ");
        System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_RED + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 7 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 6 ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 6 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 5 ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 5 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 4 ");
        System.out.print(SET_BG_COLOR_WHITE +  "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 4 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 3 ");
        System.out.print(SET_BG_COLOR_DARK_GREY +  "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_DARK_GREY + "   ");
        System.out.print(SET_BG_COLOR_WHITE + "   ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 3 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 2 ");
        System.out.print(SET_BG_COLOR_WHITE + SET_TEXT_COLOR_BLUE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_WHITE + " P ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " P ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 2 ");
        System.out.print(RESET_BG_COLOR + "\n");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " 1 ");
        System.out.print(SET_BG_COLOR_DARK_GREY + SET_TEXT_COLOR_BLUE + " R ");
        System.out.print(SET_BG_COLOR_WHITE + " N ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " B ");
        System.out.print(SET_BG_COLOR_WHITE + " Q ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " K ");
        System.out.print(SET_BG_COLOR_WHITE + " B ");
        System.out.print(SET_BG_COLOR_DARK_GREY + " N ");
        System.out.print(SET_BG_COLOR_WHITE + " R ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + SET_TEXT_COLOR_BLACK + " 1 ");
        System.out.print(RESET_BG_COLOR + "\n");
        whiteStartandEndBoard();
    }

    private void whiteStartandEndBoard() {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " a ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " b ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " c ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " d ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " e ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " f ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " g ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + " h ");
        System.out.print(SET_BG_COLOR_LIGHT_GREY + "   ");
        System.out.print(RESET_BG_COLOR + "\n");
    }

    public String help() {
        return """
                Press any key for Help
                L -> leave game
                """;
    }
}
