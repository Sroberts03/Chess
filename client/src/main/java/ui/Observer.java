package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Observer {

    private final Integer game;
    private final ObserverClient obs;

    public Observer(Integer game) {
        this.game = game;
        obs = new ObserverClient();
    }

    public void run() {
        System.out.print("Welcome to game " + game + "\n");
        System.out.print(obs.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";

        while (!result.equals("quit")) {
            obs.printBoard();
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = obs.eval(line);
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
