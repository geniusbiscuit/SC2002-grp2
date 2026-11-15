# Test notes

## How to run

```
# unit tests (needs Maven and internet the first time, to download JUnit)
mvn test

# build and play yourself (no Maven needed)
scripts/build.sh
scripts/run.sh

# replay a recorded run
scripts/replay.sh scripts/stage1-victory.txt
scripts/replay.sh scripts/stage1-defeat.txt
```

Each line of a script file is exactly what a human types at a prompt, and the game echoes it, so
the replay output reads like a real session. Full transcripts are saved in `docs/runs/`.

## Reproducible runs (Stage 1, base setting)

| Run | Script | Result |
|---|---|---|
| Victory | `scripts/stage1-victory.txt` | VICTORY: 5 paid customers, revenue $66.00, average satisfaction 79.2, 0 unhappy departures |
| Defeat | `scripts/stage1-defeat.txt` | DEFEAT: a manager who only presses Wait. 0 paid, $0.00, 2 unhappy departures |

How the victory run is played: each turn the Waiter serves a READY dish if there is one, otherwise takes an
order, otherwise seats the least satisfied queued customer; the Chef works on the order with the fewest units
left. The script was produced by `src/test/java/restaurantrush/support/GreedyManager.java` and is replayed
through the real console UI by `ScriptedRunTest`.

## What the automated tests cover

| Test class | What it proves |
|---|---|
| `TurnRulesTest` | The worked example of the brief, turn by turn (satisfaction 92, 84, 76, 76, 76, paid on turn 6); Waiter before Chef; one task per turn; a dish finished this turn is served next turn; invalid choices explain themselves and keep the task; abandonment at 0 cancels the order and frees the table; abandoned orders never count as revenue; victory targets are inclusive at the boundary; the game always runs 18 turns; at most five customers can pay |
| `EntityRulesTest` | Deterministic food rule; Cashier pays one customer per turn, oldest ready first, never on the turn they became ready; payment frees the table and records satisfaction; served customers lose nothing; satisfaction never below 0 |
| `ScriptedRunTest` | Victory and defeat scripts through the real UI; bad input (text, out of range, unknown id) is rejected and asked again |
| `MoneyTest` | Integer cents formatting and rounding |
