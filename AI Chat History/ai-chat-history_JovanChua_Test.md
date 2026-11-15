# Restaurant Rush Test - Claude

Source: claude.ai chat "SC2002 / Restaurant Rush Test", converted to Markdown.
The conversation is in order. Tool steps are not included; where they happened they are marked in *italics*.

---

### Me

restaurant-rush-repo/restaurant-rush take a look at this can u add a test code for it so we can run and check whether our game is working properly

### Claude

The project already has a solid JUnit suite, but it needs Maven plus internet to download JUnit. Let me check whether JUnit is available here, then read the game code.

*[tool steps not shown]*

I added a test file to your project that checks the whole game. I ran it on your current code and all 41 checks passed, so the game is working properly.

**How to run it**

Double-click `scripts\run-checks.bat` in the `restaurant-rush` folder. You only need Java 17 or newer installed. It prints PASS or FAIL for each check and ends with a summary. On Mac or Linux, run `scripts/run-checks.sh` instead.

**What it checks**

- Prices and money: menu prices, combo prices, the VIP discount, the Happy Hour discount and rounding.
- Customer types: how fast each type loses patience and what each one orders.
- The brief's worked example: played turn by turn, satisfaction goes 100 → 92 → 84 → 76 and the customer pays $18.00 on turn 6.
- Turn rules:
  - The Waiter acts before the Chef.
  - Each staff member gets one task per turn.
  - Invalid choices are rejected without using up the task.
  - A dish finished this turn can't be served until next turn.
  - Ignored customers leave without paying, and the game always lasts 18 turns.
- Host and Happy Hour: Happy Hour can be used once, only at the start of a turn, and lasts 2 turns. The VIP Burger costs $10.80 during Happy Hour.
- Whole games: a sensible automatic manager wins every mode (Stage 1, 2 and 3, with and without Happy Hour). A manager who only presses Wait loses every mode.
- Random stress test: 900 games of random, often invalid moves. After every turn it checks that nothing breaks: satisfaction stays between 0 and 100, a table never has two customers, and revenue never goes down.
- Your recorded scripts: all six victory and defeat scripts are played through the real console and end with the expected result.

To make sure the checks actually catch problems, I planted four bugs in a scratch copy. Every one of them made at least one check fail. Your real code wasn't touched.

**How this fits with your existing tests**

You already have good JUnit tests, but they only run with `mvn test`, which needs internet the first time. The new file needs nothing but Java. It sits next to the JUnit tests, and `mvn test` skips it, so both still work.

**What changed**

I added three files to your `restaurant-rush` folder:

- `src\test\java\restaurantrush\GameCheck.java`
- `scripts\run-checks.bat`
- `scripts\run-checks.sh`

Running the check creates an `out-check` folder, so you may want to add that to `.gitignore` before you commit.
