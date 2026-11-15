# Restaurant Rush (SC2002 group assignment)

A turn-based restaurant game on the command line. You play the manager and decide each turn what the
staff do: seat customers, take orders, serve food and cook.

**This is Stage 1 (`stage-1`).** Regular customers, the menu, a Waiter, Chef and Cashier, the full
turn sequence, and the victory and defeat endings.

## Running it

Needs JDK 17+.

```
scripts/build.sh    # compile with javac into ./out
scripts/run.sh      # play the game
mvn test            # unit tests
```

## Packages

- `boundary`: `ConsoleUI`, `Main` (input and output only)
- `control`: `GameController`, `GameConfig` (turn order, settings, win/lose check)
- `entity`: `Customer`, `Order`, `Menu`, `Table`, `Waiter`, `Chef`, `Cashier`, `Restaurant`
- `util`: `Money` (all amounts in integer cents)

See `docs/GAME_NOTES.md` for our rule decisions and `docs/TEST_NOTES.md` for testing.
