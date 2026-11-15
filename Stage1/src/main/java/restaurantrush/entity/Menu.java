package restaurantrush.entity;

import java.util.List;

// The restaurant menu. Besides holding the dishes it owns the documented rule that decides
// what a customer orders (customers choose their own food; the manager never does).
public class Menu {

    private final List<MenuItem> items;

    public Menu(List<MenuItem> items) {
        this.items = List.copyOf(items);
    }

    // The three dishes of the required base setting.
    public static Menu classic() {
        return new Menu(List.of(
                new MenuItem("Salad", 900, 1),
                new MenuItem("Burger", 1500, 1),
                new MenuItem("Pasta", 1800, 2)));
    }

    public List<MenuItem> getItems() {
        return items;
    }

    // Default ordering rule (deterministic, so every run is reproducible):
    // the n-th customer to arrive orders dish ((n - 1) mod number-of-dishes), i.e. customer 1
    // Salad, 2 Burger, 3 Pasta, 4 Salad, 5 Burger, 6 Pasta.
    public MenuItem defaultChoiceFor(int customerId) {
        return items.get((customerId - 1) % items.size());
    }
}
