package restaurantrush.boundary;

import restaurantrush.control.GameConfig;
import restaurantrush.control.GameController;

// Entry point: starts the base-setting game on the console. Pass --regular-only to play the
// Stage 1 crowd (six Regular customers) instead of the mixed Stage 2 crowd.
public class Main {

    public static void main(String[] args) {
        boolean regularOnly = args.length > 0 && args[0].equals("--regular-only");
        GameController game = new GameController(
                regularOnly ? GameConfig.regularOnlySetting() : GameConfig.baseSetting());
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
