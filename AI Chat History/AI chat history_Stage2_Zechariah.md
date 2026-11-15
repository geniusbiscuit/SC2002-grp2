# Critic and VIP Customer Code Review - Claude

Source: claude.ai chat "Critic and VIP customer code review", exported to PDF on 15 Nov 2026, 10:26 PM, and converted to Markdown.
The conversation is complete and in order. The PDF export collapses tool steps (shown here in *italics*) and does not include the attached zip file; it is marked where it appeared.

---

### Me

*[attached: LABGroup_AssignmentGrp_stage2_v4_final.zip]*

Based on my implementation of the Critic Customer and VIP Customer classes, please help me to review the code to verify that all method calls to other classes are correctly implemented. Help me to check that both classes properly override the appropriate methods inherited from the Customer base class. Identify any incorrect method calls, overriding issues, or inconsistencies with the existing class implementations.

### Claude

I'll unpack the project and go through the Critic and VIP customer classes against the Customer base class and the classes they call.

*Ran 7 commands*

Clean compile with all lint warnings on. JUnit isn't available offline, so I'll check the controller/UI side and then run my own harness plus the recorded replays.

*Created a file, ran 4 commands*

I found no errors in either class. Every call to another class matches its real signature, both classes override the right methods, and both behave as your README and GAME_NOTES describe.

**How I checked:** I compiled the project with all compiler warnings on and got zero warnings. I couldn't run your JUnit suite because the test library couldn't be downloaded here. Instead I wrote a separate test program (outside your project) that checked which class each method comes from and ran a VIP and a Critic through the whole flow: seat, order, cook, serve, pay. All 32 checks passed. I also replayed `stage2-victory.txt` and `stage2-defeat.txt`, and the output matched your saved transcripts exactly.

**Overriding**

| Method in `Customer` | VIPCustomer | CriticCustomer |
|---|---|---|
| `getTypeName()` (abstract) | overridden → `"VIP"` | overridden → `"Critic"` |
| `getWaitingLoss()` (abstract) | overridden → 5 | overridden → 12 |
| `chooseItem(Menu)` | inherited (normal rotation) | overridden → `menu.mostExpensive()` |
| `billFor(MenuItem)` | overridden → 90% of price | inherited (full price) |

- Both classes implement both abstract methods, so neither has to be abstract itself.
- Each override has `@Override`, stays `public` and returns the same type as the base method.
- Each class overrides only the method that should change and inherits the rest. That's the right design: a Critic pays full price and a VIP orders by the normal rotation without any extra code.
- `toString()`, `waitOneTurn()` and `hasLostPatience()` in `Customer` call `getTypeName()` and `getWaitingLoss()`, so the subclasses' versions are picked up automatically. The checks confirmed this: a VIP drops to 95 after one turn and a Critic to 88. A Critic reaches 0 and leaves after 9 waiting turns, while a VIP is still there after 19.

**Calls to other classes**

- **`menu.mostExpensive()`** (Critic): this method exists in `Menu`, is public and returns a `MenuItem`. Every Critic ID from 1 to 6 orders Pasta.
- **`Money.percentOf(item.getPriceCents(), BILL_PERCENT)`** (VIP): you pass the arguments in the right order (amount first, then percent), and the `import restaurantrush.util.Money` is there. Rounding is half-up as documented: 1500 becomes 1350, and 5 becomes 5 (4.5 rounded up).
- **`item.getPriceCents()`**: correct getter name, and it returns cents as an `int`, which matches your integer-cents money handling.
- **Calls into your classes:** `Waiter.takeOrder()` calls `customer.chooseItem(menu)` and then `customer.billFor(item)`, so the VIP discount is fixed into the `Order` when it's placed. `Cashier` then charges that fixed amount ($13.50 in the replay). `GameController` has no type checks, so all the different behaviour goes through these overrides.

**Small things worth tidying (none are bugs)**

1. **Critic Javadoc:** the comment says the most expensive dish "puts the longest preparation on the Chef's plate". That's only true because Pasta happens to be both the priciest and the slowest dish. `mostExpensive()` picks by price only. To be safe, change it to something like "…which on the classic menu is also the longest to prepare."
2. **Ties in `mostExpensive()`:** if two dishes had the same top price, the one listed first wins. That's predictable, but you could mention it in the `Menu` Javadoc.
3. **Version naming:** your zip is named `..._v4_final`, but the folder inside it is `LABGroup_AssignmentGrp_stage2_v3_final`. Check that's the version you mean to submit.
