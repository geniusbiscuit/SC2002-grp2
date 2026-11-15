package restaurantrush.control;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import restaurantrush.control.GameResult.Outcome;
import restaurantrush.entity.Customer;
import restaurantrush.entity.InvalidActionException;
import restaurantrush.entity.Order;
import restaurantrush.entity.Payment;
import restaurantrush.entity.Restaurant;
import restaurantrush.entity.Table;
import restaurantrush.util.Money;

/**
 * Runs the turn sequence and holds no display code. The boundary (console UI) calls these
 * methods in order: {@link #beginTurn()}, one Waiter action, one Chef action, {@link #endTurn()}.
 *
 * <p>Steps of a turn: 1 arrivals ({@code beginTurn}), 2 manager choices and 3 kitchen and serving
 * (Waiter first, then Chef, so a dish finished this turn cannot be served until next turn),
 * 4 eating and payment, 5 waiting and abandonment ({@code endTurn}), 6 result ({@link #getResult()}).
 */
public class GameController {

    /** Where we are inside a turn. Calls made in the wrong phase are rejected, not guessed. */
    public enum Phase {
        AWAITING_TURN_START, WAITER, CHEF, READY_TO_END, FINISHED
    }

    private final GameConfig config;
    private final Restaurant restaurant;
    private int turn = 0;
    private int nextCustomerId = 1;
    private Phase phase = Phase.AWAITING_TURN_START;

    public GameController(GameConfig config) {
        this.config = config;
        this.restaurant = new Restaurant(config.getMenu(), config.getTableCount());
    }

    // ---- read-only access for the boundary ---------------------------------------------

    public GameConfig getConfig() {
        return config;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public int getTurn() {
        return turn;
    }

    public Phase getPhase() {
        return phase;
    }

    public boolean isGameOver() {
        return phase == Phase.FINISHED;
    }

    // ---- step 1: arrivals ------------------------------------------------------------

    /** Starts the next turn and returns the arrival messages. */
    public List<String> beginTurn() {
        requirePhase(Phase.AWAITING_TURN_START);
        turn++;
        restaurant.startStaffTurn();
        List<String> arrivals = new ArrayList<>();
        for (GameConfig.ArrivalSlot slot : config.getArrivals()) {
            if (slot.turn() == turn) {
                Customer customer = slot.factory().apply(nextCustomerId++);
                restaurant.addCustomer(customer);
                arrivals.add("Customer " + customer + " arrives and joins the waiting queue (satisfaction "
                        + customer.getSatisfaction() + ").");
            }
        }
        phase = Phase.WAITER;
        return arrivals;
    }

    // ---- step 2/3: Waiter (acts first) --------------------------------------------------

    public ActionResult waiterWait() {
        return performWaiter(() -> {
            restaurant.getWaiter().rest();
            return "The Waiter waits.";
        });
    }

    public ActionResult waiterSeat(int customerId) {
        return performWaiter(() -> {
            Customer customer = requireCustomer(customerId);
            Table table = restaurant.getWaiter().seat(customer, restaurant.getTables());
            return "The Waiter seats customer " + customer + " at table " + table.getNumber() + ".";
        });
    }

    public ActionResult waiterTakeOrder(int customerId) {
        return performWaiter(() -> {
            Customer customer = requireCustomer(customerId);
            Order order = restaurant.getWaiter().takeOrder(customer, restaurant.getMenu());
            return "The Waiter takes the order of customer " + customer + ": " + order.getItem().getName()
                    + " (" + Money.format(order.getBillCents()) + ", " + order.getItem().getPrepUnits()
                    + " prep unit(s)).";
        });
    }

    public ActionResult waiterServe(int customerId) {
        return performWaiter(() -> {
            Customer customer = requireCustomer(customerId);
            Order order = restaurant.getWaiter().serve(customer, turn);
            return "The Waiter serves " + order.getItem().getName() + " to customer " + customer + ".";
        });
    }

    // ---- step 2/3: Chef (acts after the Waiter) -------------------------------------------

    public ActionResult chefWait() {
        return performChef(() -> {
            restaurant.getChef().rest();
            return "The Chef waits.";
        });
    }

    public ActionResult chefPrepare(int customerId) {
        return performChef(() -> {
            Customer customer = requireCustomer(customerId);
            Order order = restaurant.getChef().prepare(customer, turn);
            String progress = order.getPreparedUnits() + "/" + order.getItem().getPrepUnits();
            String suffix = order.getRemainingUnits() == 0
                    ? " The dish is READY (it can be served next turn at the earliest)." : "";
            return "The Chef prepares " + order.getItem().getName() + " for customer " + customer
                    + " (unit " + progress + ")." + suffix;
        });
    }

    // ---- steps 4 and 5: automatic ----------------------------------------------------------

    /** Eating and payment, then waiting and abandonment. */
    public TurnSummary endTurn() {
        requirePhase(Phase.READY_TO_END);
        List<String> events = new ArrayList<>();

        // Step 4: pay first (only customers ready BEFORE this turn), then let diners finish eating.
        restaurant.collectPayment(turn).ifPresent(p -> events.add(describePayment(p)));
        for (Customer customer : restaurant.finishEating(turn)) {
            events.add("Customer " + customer + " finishes eating and is ready to pay.");
        }

        // Step 5: unserved customers lose satisfaction; anyone at 0 leaves.
        for (Customer customer : restaurant.applyWaitingLoss()) {
            events.add("Customer " + customer + " loses patience and leaves without paying"
                    + " (order cancelled, table freed).");
        }

        TurnSummary summary = new TurnSummary(turn, events);
        phase = turn >= config.getMaxTurns() ? Phase.FINISHED : Phase.AWAITING_TURN_START;
        return summary;
    }

    private String describePayment(Payment p) {
        return "The Cashier collects " + Money.format(p.amountCents()) + " from customer " + p.customer()
                + " (satisfaction " + p.satisfaction() + " recorded). Table " + p.tableNumber() + " is free.";
    }

    // ---- step 6: result --------------------------------------------------------------------

    public GameResult getResult() {
        requirePhase(Phase.FINISHED);
        int paid = restaurant.getPaidCount();
        int revenue = restaurant.getRevenueCents();
        int satisfactionSum = restaurant.getPaidSatisfactionSum();
        boolean paidMet = paid >= config.getRequiredPaidCustomers();
        boolean revenueMet = revenue >= config.getRequiredRevenueCents();
        // Integer comparison of the average: sum / paid >= required  <=>  sum >= required * paid.
        boolean satisfactionMet = paid > 0
                && satisfactionSum >= config.getRequiredAverageSatisfaction() * paid;
        Outcome outcome = paidMet && revenueMet && satisfactionMet ? Outcome.VICTORY : Outcome.DEFEAT;
        return new GameResult(outcome, paid, revenue, satisfactionSum, restaurant.getServedCount(),
                restaurant.getUnhappyDepartures(), turn, paidMet, revenueMet, satisfactionMet);
    }

    // ---- helpers -----------------------------------------------------------------------------

    private ActionResult performWaiter(Supplier<String> action) {
        if (phase != Phase.WAITER) {
            return ActionResult.fail("It is not the Waiter's turn to act (phase " + phase + ").");
        }
        try {
            String message = action.get();
            phase = Phase.CHEF;
            return ActionResult.ok(message);
        } catch (InvalidActionException e) {
            return ActionResult.fail(e.getMessage());
        }
    }

    private ActionResult performChef(Supplier<String> action) {
        if (phase != Phase.CHEF) {
            return ActionResult.fail(phase == Phase.WAITER
                    ? "The Waiter acts before the Chef - choose the Waiter task first."
                    : "It is not the Chef's turn to act (phase " + phase + ").");
        }
        try {
            String message = action.get();
            phase = Phase.READY_TO_END;
            return ActionResult.ok(message);
        } catch (InvalidActionException e) {
            return ActionResult.fail(e.getMessage());
        }
    }

    private Customer requireCustomer(int customerId) {
        return restaurant.findCustomer(customerId)
                .orElseThrow(() -> new InvalidActionException("There is no customer with id " + customerId + "."));
    }

    private void requirePhase(Phase expected) {
        if (phase != expected) {
            throw new IllegalStateException("Expected phase " + expected + " but was " + phase);
        }
    }
}
