# Game notes

These are the rules we followed where the brief left details open. Each one is covered by a unit test
or a scripted run (see `TEST_NOTES.md`).

## Turn order

1. **Arrivals** (`GameController.beginTurn()`): new customers join the queue at satisfaction 100, before the state is shown.
2. **Manager choices**: the Waiter acts first, then the Chef. A rejected choice is explained and the staff member can still act.
3. **Kitchen and serving**: actions happen straight away, so the Chef can cook an order the Waiter took this turn. A dish can only be served if it became READY on an earlier turn.
4. **Eating and payment** (`endTurn()`): the Cashier takes one payment from a customer who was already ready before this turn, then customers served earlier finish eating.
5. **Waiting** (`Restaurant.applyWaitingLoss()`): unserved customers lose satisfaction; anyone at 0 leaves, their order is cancelled and the table is freed.
6. **Result** (`getResult()`): only after turn 18. The game never ends early.

## Our decisions

1. Customer ids follow arrival order (1 to 6).
2. The manager picks who to seat; they get the lowest-numbered free table.
3. Customer *n* orders dish *(n - 1) mod 3*: Salad, Burger, Pasta, Salad, Burger, Pasta.
4. The Cashier serves whoever has been ready to pay the longest (ties go to the lower id).
5. A READY dish that hasn't been served still counts as unserved, so the customer keeps losing satisfaction.
6. "Wait" counts as the Waiter's or Chef's action for that turn.
7. The satisfaction target is checked with integers (`sum >= 60 * paid`) to avoid rounding errors.
8. Money is stored in cents and shown as dollars.
9. Results (served, unhappy departures, revenue, average satisfaction, turns) are printed every turn and at the end.
10. Statistics are worked out from customer states instead of separate counters, so unpaid orders can't count as revenue.

## Why the turn-16 customer can't pay

Seated on turn 16, ordered on 17, served on 18 at the earliest, and they would only finish eating on 19.
So at most five customers can pay, which the brief intends.
