# Game notes

These are the rules we followed where the brief left details open. Each one is covered by a unit test
or a scripted run (see `TEST_NOTES.md`).

## Turn order

1. **Arrivals** (`GameController.beginTurn()`): new customers join the queue at satisfaction 100, before the state is shown.
2. **Manager choices**: Happy Hour offer (if still unused), then Host, then Waiter, then Chef. Without a Host it is just Waiter, then Chef. A rejected choice is explained and the staff member can still act.
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
6. "Wait" counts as the Host's, Waiter's or Chef's action for that turn.
7. The satisfaction target is checked with integers (`sum >= 60 * paid`) to avoid rounding errors.
8. Money is stored in cents and shown as dollars.
9. Results (served, unhappy departures, revenue, average satisfaction, turns) are printed every turn and at the end.
10. Statistics are worked out from customer states instead of separate counters, so unpaid orders can't count as revenue.

## Stage 2

11. The 2nd arrival (turn 4) is a VIP and the 4th (turn 10) is a Critic.
12. VIPs pay 90% of the price, rounded half-up to the cent.
13. Our Critic rule: a Critic always orders the most expensive dish (Pasta in Stage 2; see 17 for Stage 3), which takes the Chef longest.
14. `GameController` never checks customer types. Waiting loss, bill and dish choice are overridden in each subclass, and a test checks the controller has no `instanceof`.

## Stage 3

15. Host, combos and Happy Hour are switched on in `GameConfig.fullSetting()`. The `--classic` and `--regular-only` settings leave them off, so Stage 1 and 2 runs replay exactly.
16. The Host acts before the Waiter, so a customer seated by the Host can order in the same turn. Host and Waiter share the seating rules in `Seating`.
17. Customers 3 and 6 order combos (Salad + Pasta $24.30, Burger + Pasta $29.70). Everyone else keeps rule 3. The Critic now orders Burger + Pasta, the most expensive item.
18. Combo price is 90% of the two dishes added together (rounded half-up); prep units are added.
19. Happy Hour turned on at turn *n* covers turns *n* and *n+1*. Waiting customers recover 15 straight away and again at the start of turn *n+1*.
20. Only customers still waiting for a table or food recover. Customers who are eating, paying or gone do not.
21. Discounts apply in order: VIP first, then Happy Hour, each rounded half-up. The price is locked when the order is taken.

## Why the turn-16 customer can't pay

Seated on turn 16, ordered on 17, served on 18 at the earliest, and they would only finish eating on 19.
So at most five customers can pay, which the brief intends.
