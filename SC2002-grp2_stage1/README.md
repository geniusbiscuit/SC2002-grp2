# Restaurant Rush (SC2002 group assignment)

A turn-based restaurant management game for the command line. You are the manager: staff time is
limited, so you decide whom to seat, whose order to take, what to serve and what the Chef cooks first.

**This is the Stage 1 version (`stage-1`).** Regular customers, the menu, Waiter, Chef, Cashier, the
full turn sequence, and the victory and defeat endings.

## Build and run

Requires JDK 17 or newer.

```
scripts/build.sh      # compiles to ./out (plain javac, no Maven)
scripts/run.sh        # play interactively
mvn test              # run the unit tests
```

Or with Maven: `mvn package` then `java -jar target/restaurant-rush.jar`.

## Packages

| Package | Role (Boundary / Control / Entity) |
|---|---|
| `restaurantrush.boundary` | `ConsoleUI`, `Main`: read choices, print state. No game rules. |
| `restaurantrush.control` | `GameController`, `GameConfig`: the turn sequence and win/lose check |
| `restaurantrush.entity` | `Customer`, `Order`, `Menu`, `Table`, `Waiter`, `Chef`, `Cashier`, `Restaurant`: the game rules |
| `restaurantrush.util` | `Money` (integer cents) |

More detail: `docs/GAME_NOTES.md` (rules and decisions), `docs/TEST_NOTES.md` (tests and reproducible runs).
