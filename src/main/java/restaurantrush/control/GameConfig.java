package restaurantrush.control;

import java.util.List;
import java.util.function.IntFunction;

import restaurantrush.entity.CriticCustomer;
import restaurantrush.entity.Customer;
import restaurantrush.entity.Menu;
import restaurantrush.entity.RegularCustomer;
import restaurantrush.entity.VIPCustomer;

// All the numbers of a game in one place. baseSetting() is the required setting a TA
// can reproduce: 18 turns, 2 tables, 1 Waiter / Chef / Cashier, six arrivals on turns
// 1, 4, 7, 10, 13 and 16, and the three targets.
public final class GameConfig {

    // A scheduled arrival: on turn, a customer is built from the given id.
    public record ArrivalSlot(int turn, IntFunction<Customer> factory) {
    }

    private final int maxTurns;
    private final int tableCount;
    private final int requiredPaidCustomers;
    private final int requiredRevenueCents;
    private final int requiredAverageSatisfaction;
    private final Menu menu;
    private final List<ArrivalSlot> arrivals;

    public GameConfig(int maxTurns, int tableCount, int requiredPaidCustomers, int requiredRevenueCents,
                      int requiredAverageSatisfaction, Menu menu, List<ArrivalSlot> arrivals) {
        this.maxTurns = maxTurns;
        this.tableCount = tableCount;
        this.requiredPaidCustomers = requiredPaidCustomers;
        this.requiredRevenueCents = requiredRevenueCents;
        this.requiredAverageSatisfaction = requiredAverageSatisfaction;
        this.menu = menu;
        this.arrivals = List.copyOf(arrivals);
    }

    // The required base setting with a mixed crowd: the second arrival is a VIP and the fourth
    // is a Critic (two of the six regular arrivals replaced). Still six arrivals on the same turns.
    public static GameConfig baseSetting() {
        return new GameConfig(18, 2, 4, 4500, 60, Menu.classic(), List.of(
                new ArrivalSlot(1, RegularCustomer::new),
                new ArrivalSlot(4, VIPCustomer::new),
                new ArrivalSlot(7, RegularCustomer::new),
                new ArrivalSlot(10, CriticCustomer::new),
                new ArrivalSlot(13, RegularCustomer::new),
                new ArrivalSlot(16, RegularCustomer::new)));
    }

    // The Stage 1 crowd (six Regular customers), kept so earlier behaviour can still be replayed.
    public static GameConfig regularOnlySetting() {
        return new GameConfig(18, 2, 4, 4500, 60, Menu.classic(), List.of(
                new ArrivalSlot(1, RegularCustomer::new),
                new ArrivalSlot(4, RegularCustomer::new),
                new ArrivalSlot(7, RegularCustomer::new),
                new ArrivalSlot(10, RegularCustomer::new),
                new ArrivalSlot(13, RegularCustomer::new),
                new ArrivalSlot(16, RegularCustomer::new)));
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public int getTableCount() {
        return tableCount;
    }

    public int getRequiredPaidCustomers() {
        return requiredPaidCustomers;
    }

    public int getRequiredRevenueCents() {
        return requiredRevenueCents;
    }

    public int getRequiredAverageSatisfaction() {
        return requiredAverageSatisfaction;
    }

    public Menu getMenu() {
        return menu;
    }

    public List<ArrivalSlot> getArrivals() {
        return arrivals;
    }
}
