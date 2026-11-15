package restaurantrush.support;

import java.util.List;

import restaurantrush.control.GameConfig;
import restaurantrush.control.GameConfig.ArrivalSlot;
import restaurantrush.control.GameController;
import restaurantrush.entity.Menu;
import restaurantrush.entity.MenuItem;
import restaurantrush.entity.RegularCustomer;

// Small configurations that make rule tests short and exact.
public final class TestConfigs {

    private TestConfigs() {
    }

    // One Regular customer on turn 1 who always orders Pasta (the worked example in the brief).
    public static GameConfig onePastaCustomer(int paid, int revenueCents, int averageSatisfaction) {
        Menu pastaOnly = new Menu(List.of(new MenuItem("Pasta", 1800, 2)));
        return new GameConfig(18, 2, paid, revenueCents, averageSatisfaction, pastaOnly,
                List.of(new ArrivalSlot(1, RegularCustomer::new)));
    }

    // Plays the six-turn example of the brief (arrive, seat, order, cook x2, serve, eat, pay), then
    // lets the manager wait until the game ends. Customer 1 pays on turn 6 with satisfaction 76.
    public static void playPastaExample(GameController game) {
        step(game, 1, "seat", "wait");
        step(game, 2, "order", "prepare");
        step(game, 3, "wait", "prepare");
        step(game, 4, "serve", "wait");
        step(game, 5, "wait", "wait");
        step(game, 6, "wait", "wait");
        while (!game.isGameOver()) {
            step(game, game.getTurn() + 1, "wait", "wait");
        }
    }

    // One full turn with customer 1 as the target of any seat/order/serve/prepare action.
    public static void step(GameController game, int expectedTurn, String waiter, String chef) {
        game.beginTurn();
        if (game.getTurn() != expectedTurn) {
            throw new IllegalStateException("Expected turn " + expectedTurn + " but was " + game.getTurn());
        }
        require(switch (waiter) {
            case "seat" -> game.waiterSeat(1).success();
            case "order" -> game.waiterTakeOrder(1).success();
            case "serve" -> game.waiterServe(1).success();
            default -> game.waiterWait().success();
        }, "waiter " + waiter);
        require(switch (chef) {
            case "prepare" -> game.chefPrepare(1).success();
            default -> game.chefWait().success();
        }, "chef " + chef);
        game.endTurn();
    }

    private static void require(boolean ok, String what) {
        if (!ok) {
            throw new IllegalStateException("Action failed: " + what);
        }
    }
}
