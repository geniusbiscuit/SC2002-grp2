# Restaurant Rush (SC2002 group assignment)

A turn-based restaurant game on the command line. You play the manager and decide each turn what the
staff do: seat customers, take orders, serve food and cook.

**This is Stage 2 (`stage-2`).** We added VIP and Critic customers to the Stage 1 game (Regular
customers, Waiter, Chef, Cashier, full turn sequence).

| Customer | Loses per waiting turn | Bill | Special behaviour |
|---|---|---|---|
| Regular | 8 | full price | none |
| VIP | 5 | 10% off | none |
| Critic | 12 | full price | always orders the most expensive dish |

The default game has a mixed crowd: Regular, **VIP**, Regular, **Critic**, Regular, Regular.

## Running it

Needs JDK 17+.

```
scripts/build.sh                 # compile with javac into ./out
scripts/run.sh                   # Stage 2 game
scripts/run.sh --regular-only    # Stage 1 crowd (six Regular customers)
mvn test                         # unit tests
```

## Packages

- `boundary`: `ConsoleUI`, `Main` (input and output only)
- `control`: `GameController`, `GameConfig` (turn order, settings, win/lose check)
- `entity`: `Customer` and its subclasses, staff (`Waiter`, `Chef`, `Cashier`), `Menu`, `Order`, `Table`, `Restaurant`
- `util`: `Money` (all amounts in integer cents)

See `docs/GAME_NOTES.md` for our rule decisions and `docs/TEST_NOTES.md` for testing.
