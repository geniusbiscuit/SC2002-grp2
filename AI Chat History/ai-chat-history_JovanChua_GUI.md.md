# Restaurant Rush GUI - Claude

Source: claude.ai chat "SC2002 / Restaurant Rush GUI", exported to PDF on 15 Nov 2026, 8:55 PM, and converted to Markdown.
The conversation is complete and in order. The PDF export collapses tool steps (shown here in *italics*) and does not include the attached images, zip files or screenshots; those are marked where they appeared.

---

### Me

*[attached: restaurant-rush-refined.zip]*

hi can you take a look at the restaurant rush folder can then create the gui for it using the same inputs from the ConsoleUI and connect it to the game controller. The graphics of the characters does not have to be refined and you can make use of stick figure or approximate figures so that everyone can identify the charcter's role. Follow the image I have attached as the basis of the whole restaurant and ask me before doing any changes

### Claude

I'll unpack the project and go through the ConsoleUI and game controller, then come back to you with a plan before I change anything.

*Ran 6 commands*

I've read through the project. Here's what the GUI needs to cover and my plan. Nothing has been changed yet.

**What the ConsoleUI asks for, in order, each turn**

1. Happy Hour: Yes or No (only while it's still available)
2. Host: Seat #id or Wait
3. Waiter: Seat #id, Take order #id, Serve #id or Wait
4. Chef: Prepare #id or Wait
5. endTurn() runs on its own: Cashier payment, eating, waiting loss and people leaving

If the controller rejects a choice, the console asks the same question again. The GUI will do the same: it shows the "Not allowed" message and stays on that step.

**Plan (following your mock-up)**

- Swing, so nothing extra to install. It builds with plain javac like the rest of the project.
- Window: 1200×800, using the colours from your colour key.
  - Top bar: Turn, Revenue, Paid, Avg Satisfaction, Happy Hour. Chips turn green when a target is met and gold while Happy Hour is ON.
  - Left: the queue, with a red carpet and a sign.
  - Kitchen: Chef at the stove, plus a pass counter showing READY dishes.
  - Restaurant floor: checker tiles with 2 tables. Name tags turn green when a table is free.
  - Right: the Controls panel with the Host / Waiter / Chef / Service step tabs, the prompt, the hint and the coloured pill buttons. Buttons are greyed out when they can't be used.
  - Bottom: a terminal log that prints exactly what the console prints, with turn headers in gold, good events in green and bad events in red.
- Stick figures, each with a label so the role is obvious:
  - Host: bow tie, standing at the queue
  - Waiter: tray
  - Chef: white chef hat
  - Cashier: by the till
  - Customers: a different colour for Regular, VIP (gold crown) and Critic (notepad). Each one shows its #id and a satisfaction bar.
- New files only: `boundary/GameWindow.java`, plus a small drawing panel class. It only calls the existing GameController methods. None of the game rules change, and the tests and replay scripts stay as they are.

One catch: `scripts/check_diagram.py` fails if any Java class is missing from `class-diagram.puml`, so new GUI classes will break that check unless the diagram is updated too.

*Questions asked (answers shown as in the export):*

- How should the Restaurant Rush GUI be launched? (Main currently starts the console game, which the replay scripts and tests depend on.) — **Add a --gui flag (Recommended)**
- After pressing an action button like Seat / Take order / Serve / Prepare, how should the customer id be chosen? — **Id chips under prompt (Recommended)**
- In your mock-up the 4th tab is 'Service'. What should that step be? — **End-of-turn step (Recommended)**
- `scripts/check_diagram.py` fails if a class isn't in `class-diagram.puml`. Should I add the new GUI classes to the diagram? — **Yes, update .puml (Recommended)**

*Created 2 files, shared 2 files, and 27 more steps*

The GUI is built and wired to the game controller, laid out like your mock-up. To start it, run `scripts/run.sh --gui`, or on Windows `java -cp out restaurantrush.boundary.Main --gui`. You can add `--classic` or `--regular-only` as well. The console game is still the default and works exactly as before.

**How it plays**

- Same inputs as the ConsoleUI: Happy Hour yes/no, then Host → Waiter → Chef → Service.
  - The step tabs show where you are: gold for the current step, green for steps already done.
  - Buttons grey out when they can't be used.
  - After you press Seat, Take order, Serve or Prepare, buttons for the valid customers appear (like #2 VIP), and those customers get a gold ring in the restaurant.
  - Service has an End turn button. It runs the Cashier, eating and walk-outs, then starts the next turn.
- Rejected choices show as "Not allowed" in red, and you pick again, the same as the console.
- Characters:
  - Host has a bow tie and stands by the queue.
  - Waiter carries a tray and walks to the table he last worked at.
  - Chef has a white hat, next to the stove.
  - Cashier stands by the till.
  - Customers: Regulars are blue, VIPs are purple with a crown, and Critics wear a beret and carry a notepad. Each has an id tag and a satisfaction bar.
  - Staff tags change to "done" once that person has acted this turn.
- The terminal at the bottom prints the same messages as the console, coloured as in your colour key.
- When the game ends you get a VICTORY or DEFEAT screen with a Play again button.

**Checks**

- I drove the saved victory and defeat runs through the GUI's buttons. They ended with the same results as the console: Stage 3 victory with 5 paid and $88.80, Stage 2 victory with $73.50, and Stage 3 defeat.
- All six console replays still match the saved transcripts word for word.
- `scripts/check_diagram.py` passes.
- I couldn't run `mvn test`, because Maven can't download JUnit with network access off here.

**What changed**

- Added: `boundary/GameWindow.java` and `boundary/RestaurantPanel.java`.
- Edited: `Main.java` (the `--gui` flag) and `class-diagram.puml`.
- No game rules were touched.

**Still to do on your side**

- Diagram image: `class-diagram.png` still shows the old diagram. PlantUML isn't available here, so someone needs to re-render it from the updated .puml.
- README: I haven't added the `--gui` command to its build-and-run section, since you asked me to check before any changes. Want me to add it?

*[file: Restaurant rush gui (ZIP) — Download all]*

### Me

*[attached: sprite sheet image]*

Can u regenerate this with the attached sprites

### Claude

*Wiring floating text and departure effects into the game window*

I'll redraw every character, table, dish and kitchen piece in code to match your sprite sheet. The game logic and controls stay the same.

*Created 2 files, read a file, and 5 more steps*

Close to the sprite sheet. The Chef's tag is hidden behind the pass counter, so I'll make the kitchen a little taller. First, the defeat run to check moods and walk-outs:

*Shared 2 files, read 4 files, and 9 more steps*

The GUI now uses your sprite designs. I drew them in Java code rather than cutting them out of the picture, so they stay sharp at any size and the project needs no image files. The controls and game rules are the same as before.

**What's in it now**

- Staff:
  - Host: red shirt, yellow bow tie and clipboard.
  - Waiter: black vest and bow tie.
  - Chef: tall hat and frying pan.
  - Cashier: green apron, behind a desk with a register.
- Who acts now: whoever's turn it is stands on a yellow ring with a gold "HOST <- your call" tag. Staff who have already acted show "done".
- Customers:
  - Regulars, half of them wearing a red cap.
  - VIPs in purple with a crown.
  - Critics with a beret, glasses, scarf and notepad.
  - Skin and hair vary by customer.
- Moods: faces are happy above 66, neutral from 34 to 66, and upset at 33 or below. The satisfaction bar turns green, orange or red to match.
- At the table: round wooden tables with chairs. A bubble above each customer shows what they're waiting for:
  - "order?" before the Waiter takes the order.
  - The dish with cooking progress, such as 1/2.
  - "ready!" once the dish is cooked.
  - "$ bill" with the amount when they're ready to pay.
  - The dish sits on the table while they eat, and an empty plate is left when they're waiting to pay.
- Kitchen:
  - A stove with a hood; a burner glows while there's food to cook.
  - Order tickets show the dish and one dot per cooking step.
  - Ready dishes sit on the steel pass with green #id tags, next to a bell.
- Furniture: a "Today's Menu" chalkboard (prices come from the game's menu) and two plants.
- Floating text that fades after about 2 seconds: "+$X" at the till, "zzz" when a staff member waits, "Yum!" when food is served, "DING! #3 ready", and "Happy Hour!". A customer who runs out of patience turns red with "!!" and "Left angry!", then walks off.

In the screenshot, several floating texts overlap because my test clicked through turns instantly. In real play each one fades before the next turn.

**Checks**

- The GUI plays the Stage 3 victory and defeat runs and the Stage 2 victory run to the same results as the console.
- All six console replays still match the saved transcripts exactly.
- The diagram check passes.

**What changed**

- New file: `boundary/Sprites.java`, which holds all the drawing code.
- Edited: `RestaurantPanel.java` and `GameWindow.java`, for the new drawings and the floating text.
- Diagram: I added Sprites to `class-diagram.puml`, because the checker fails on any class that's missing from it.

`class-diagram.png` still needs re-rendering from the .puml, and the README still doesn't mention `--gui`.

*[file: Restaurant rush gui (ZIP) — Download all]*
