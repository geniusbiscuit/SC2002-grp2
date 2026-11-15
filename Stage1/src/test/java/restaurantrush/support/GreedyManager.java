package restaurantrush.support;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import restaurantrush.control.GameController;
import restaurantrush.entity.Customer;
import restaurantrush.entity.Order;
import restaurantrush.entity.Restaurant;

// A simple automatic "manager" used by the tests and to generate the reproducible scripts in
// scripts/. It plays through the same GameController API as the console UI and
// records the exact lines a human would type, so the script can be piped into the game.
//
// Strategy: the Waiter serves a READY dish first, then takes an order, then seats a queued
// customer (always the least satisfied one); the Chef works on the order with the fewest units left.
public class GreedyManager {

    private final List<String> keystrokes = new ArrayList<>();

    // Plays a whole game and returns the lines typed.
    public static List<String> playToEnd(GameController game) {
        GreedyManager manager = new GreedyManager();
        manager.play(game);
        return manager.keystrokes;
    }

    private void play(GameController game) {
        while (!game.isGameOver()) {
            game.beginTurn();
            waiterAction(game);
            chefAction(game);
            game.endTurn();
        }
    }

    private void waiterAction(GameController game) {
        Restaurant r = game.getRestaurant();
        List<Order> servable = r.servableOrders(game.getTurn());
        if (!servable.isEmpty()) {
            Customer c = lowestSatisfaction(servable.stream().map(Order::getCustomer).toList());
            type("3", String.valueOf(c.getId()));
            check(game.waiterServe(c.getId()).success());
            return;
        }
        if (!r.customersAwaitingOrder().isEmpty()) {
            Customer c = lowestSatisfaction(r.customersAwaitingOrder());
            type("2", String.valueOf(c.getId()));
            check(game.waiterTakeOrder(c.getId()).success());
            return;
        }
        if (!r.queuedCustomers().isEmpty() && r.hasFreeTable()) {
            Customer c = lowestSatisfaction(r.queuedCustomers());
            type("1", String.valueOf(c.getId()));
            check(game.waiterSeat(c.getId()).success());
            return;
        }
        type("0");
        check(game.waiterWait().success());
    }

    private void chefAction(GameController game) {
        List<Order> preparable = game.getRestaurant().preparableOrders();
        if (preparable.isEmpty()) {
            type("0");
            check(game.chefWait().success());
            return;
        }
        Order next = preparable.stream()
                .min(Comparator.comparingInt(Order::getRemainingUnits)
                        .thenComparingInt(o -> o.getCustomer().getSatisfaction())
                        .thenComparingInt(o -> o.getCustomer().getId()))
                .orElseThrow();
        type("1", String.valueOf(next.getCustomer().getId()));
        check(game.chefPrepare(next.getCustomer().getId()).success());
    }

    private static Customer lowestSatisfaction(List<Customer> customers) {
        return customers.stream()
                .min(Comparator.comparingInt(Customer::getSatisfaction).thenComparingInt(Customer::getId))
                .orElseThrow();
    }

    private void type(String... lines) {
        keystrokes.addAll(List.of(lines));
    }

    private static void check(boolean success) {
        if (!success) {
            throw new IllegalStateException("The greedy manager attempted an invalid action");
        }
    }
}
