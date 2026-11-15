# Restaurant Rush (SC2002 group assignment)

A turn-based restaurant game on the command line. You play the manager and decide each turn what the
staff do: seat customers, take orders, serve food and cook.

**This is Stage 3 (`stage-3`).** We added a Host, combo meals and Happy Hour on top of Stage 2
(VIP and Critic customers) and Stage 1 (Regular customers, Waiter, Chef, Cashier, full turn sequence).

| Customer | Loses per waiting turn | Bill | Special behaviour |
|---|---|---|---|
| Regular | 8 | full price | none |
| VIP | 5 | 10% off | none |
| Critic | 12 | full price | always orders the most expensive dish |

| Stage 3 feature | What it does |
|---|---|
| Host | Seats one queued customer per turn, before the Waiter acts. |
| ComboMeal | Two dishes at 90% of their total price, prep units added (Salad + Pasta = $24.30, 3 units). Customers 3 and 6 order combos. |
| Happy Hour | Can be switched on once, at the start of a turn, for 2 turns. New orders get 20% off (after the VIP discount) and waiting customers recover 15 satisfaction each turn. |

## Running it

Needs JDK 17+.

```
scripts/build.sh                 # compile with javac into ./out
scripts/run.sh                   # Stage 3 game
scripts/run.sh --classic         # Stage 2 setting (no Host, combos or Happy Hour)
scripts/run.sh --regular-only    # Stage 1 setting (six Regular customers)
mvn test                         # unit tests
```

All settings use 18 turns, 2 tables, arrivals on turns 1, 4, 7, 10, 13, 16 and the same targets.

## Packages

- `boundary`: `ConsoleUI`, `Main` (input and output only)
- `control`: `GameController`, `GameConfig` (turn order, settings, win/lose check)
- `entity`: customers, staff (`Waiter`, `Host`, `Chef`, `Cashier`), `Seating`, `Menu`, `MenuItem`, `ComboMeal`, `Order`, `Table`, `HappyHour`, `Restaurant`
- `util`: `Money` (all amounts in integer cents)

See `docs/GAME_NOTES.md` for our rule decisions and `docs/TEST_NOTES.md` for testing.
