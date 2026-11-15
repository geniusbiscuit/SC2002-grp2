# Game notes: how we read the brief

Where the brief leaves a detail open, this is the rule our code follows. Every rule below is
covered by a unit test or a scripted run (see `TEST_NOTES.md`).

## Turn order

| Step | Where it lives | Notes |
|---|---|---|
| 1 Arrivals | `GameController.beginTurn()` | Customer joins the queue with satisfaction 100. Arrivals happen **before** the start-of-turn display so the manager sees the newcomer. |
| 2 Manager choices | `waiterXxx(...)`, `chefXxx(...)` | The controller enforces **Waiter first, then Chef**. A rejected choice returns an explanation and does not use up the task. |
| 3 Kitchen and serving | `Waiter`, `Chef`, `Order.isServableOn(turn)` | Each action takes effect as soon as it is chosen, so the Chef's list already includes an order the Waiter took this turn. A dish is servable only if it became READY on an earlier turn. |
| 4 Eating and payment | `GameController.endTurn()` | The Cashier pays first (only customers who were READY_TO_PAY before this turn), then diners served on an earlier turn become READY_TO_PAY. |
| 5 Waiting and abandonment | `Restaurant.applyWaitingLoss()` | Every customer who has not been served loses satisfaction. Anyone at 0 leaves: order cancelled, table freed. |
| 6 Result | `GameController.getResult()` | Only after turn 18. The game never ends early. |

## Decisions we made

1. **Customer ids** are arrival order (1 to 6).
2. **Seating:** the manager picks the customer; the customer sits at the lowest-numbered free table.
3. **What customers order (deterministic rule):** customer *n* orders dish *(n - 1) mod 3* of the menu,
   i.e. 1 Salad, 2 Burger, 3 Pasta, 4 Salad, 5 Burger, 6 Pasta. The choice is printed when the order is taken.
4. **"Oldest first" for the Cashier** means the customer who has been READY_TO_PAY longest (ties: lower id).
5. **A READY dish that has not been served yet** still counts as "not served", so that customer keeps losing satisfaction.
6. **"Wait" is a real choice:** it uses the Waiter's or Chef's task for the turn.
7. **Average satisfaction target** is compared with integers (`sum >= 60 * paid customers`), so there is no rounding error.
   The displayed average has one decimal.
8. **Money** is stored as integer cents everywhere and printed as dollars with two decimals.
9. **End-of-turn results** (served count, unhappy departures, revenue, average satisfaction, turns used) are printed after every turn and again in the final summary.
10. **Statistics are derived from customer state** (never kept in separate counters), so revenue cannot include an unpaid or abandoned order.

## Why the turn-16 customer cannot pay

Seated turn 16, order turn 17 at the earliest, served turn 18 at the earliest (and only for a one-unit dish),
eating finishes turn 19. So at most five customers can pay. This is intended by the brief.
