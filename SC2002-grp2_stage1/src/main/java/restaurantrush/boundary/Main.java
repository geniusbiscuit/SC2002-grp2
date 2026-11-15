package restaurantrush.boundary;

import restaurantrush.control.GameConfig;
import restaurantrush.control.GameController;

/** Entry point: starts the base-setting game on the console. */
public class Main {

    public static void main(String[] args) {
        GameController game = new GameController(GameConfig.baseSetting());
        // When input is piped (a scripted run) echo it so the transcript shows every choice.
        boolean scripted = System.console() == null;
        try {
            new ConsoleUI(game, System.in, System.out, scripted).run();
        } catch (InputEndedException e) {
            System.out.println();
            System.out.println(e.getMessage() + " (game aborted at turn " + game.getTurn() + ")");
        }
    }
}
