# SC2002 Project Code Review - Claude

Source: claude.ai chat "SC2002 project code review", exported to PDF on 15 Nov 2026, 7:46 PM, and converted to Markdown.
The conversation is complete and in order. The PDF export collapses tool steps (shown here in *italics*); no files or images were attached in this chat.

---

### Me

hi claude, i just need your help to help check my sc2002 group project's final stage 3 code for syntax errors etc. let me know how you want me to let you check the code.

### Claude

*Computer: See your computer's connected folders (Failed)*

Sure, happy to go through it. Any of these works, easiest first:

1. **Zip the project and attach it here.** Zip the whole `src` folder (or the full project folder) and drop it into this chat. I'll unzip it, compile everything with `javac` so we catch real syntax and type errors rather than guessing, and then read through for logic issues too.
2. **Send a GitHub link.** If the group repo is on GitHub, send the link (and the branch name, if it isn't `main`). I can clone it and do the same check.
3. **Let me read the folder on your computer.** This chat is linked to your computer, but the computer isn't reachable right now. If you open this chat in the Claude desktop app on that laptop and send a message, I can read the project folder straight from your disk.
4. **Paste the code.** This works if it's only a few files. For a full Restaurant Rush codebase, a zip is much easier.

A couple of things that would help:

- **Your Java version** (whatever `java -version` shows), so I compile against the same one.
- **The Stage 3 requirements**, if you have them handy as a PDF or a screenshot of the brief. Then I can check that the required features exist, not just that the code compiles.

Once I have the code, I'll tell you about each error with the file and line it's on, and I can send back fixed files if you want them.

### Me

https://github.com/geniusbiscuit/SC2002_Project/tree/main/src

here you go, are you able to access it?

### Claude

*Used Add repo, ran a command*

I've got the repo. Next I'll compile it and run the tests.

*Ran 6 commands*

Maven Central is blocked from my workspace, so I can't download JUnit. I'll check what the tests use and try running them with a small stand-in instead.

*Ran 3 commands*

No git tags on GitHub either, and two docs the README links to are missing. Next I'll run the four test classes that don't depend on `TestConfigs`, plus the six replay scripts.

*Ran 4 commands*

The game code itself has **no syntax errors**. Everything compiles cleanly on Java 17, and with every compiler warning turned on the main game code gives zero warnings. The problems are with what's on GitHub: some files and the stage tags never got uploaded. I checked the latest version on `main` (uploaded 30 Oct).

**What passed**

- **Compiling:** all the files under `src/main` (about 3,200 lines including tests) compile. The optional GUI in `extras/` also compiles. Its only warnings are about `serialVersionUID` on the Swing classes, which you can ignore.
- **Replays:** all six scripts (stage 1, 2 and 3, a victory and a defeat for each) give the result you expect. Each one's output matches the transcript in `docs/runs/` character for character. Stage 3 victory still comes out at $88.80 with average satisfaction 80.8.
- **Bad input:** I fed the game 900 runs of random junk, like letters, negative numbers, huge numbers, blank lines and input that cuts off early. It never crashed or threw an exception.
- **Tests:** I can't download JUnit from here, so I ran the test classes with a small stand-in I wrote. The 4 test classes that can compile passed all 43 of their tests.

**What needs fixing before you submit**

1. **`TestConfigs.java` is missing, so `mvn test` won't compile.** `TurnRulesTest` and `CustomerBehaviourTest` import `restaurantrush.support.TestConfigs`, but only `GreedyManager.java` is in `src/test/java/restaurantrush/support/`. Someone probably still has it on their laptop. Upload it, because as things stand any grader who runs `mvn test` gets 34 compile errors. The two test classes that need it are the only ones I couldn't run.
2. **No git tags on GitHub.** The README says there are `stage-1`, `stage-2` and `stage-3` tags, and `git checkout stage-1`, `package_stage.sh` and `check_diagram.py` all depend on them. The repo has just one commit, called "Add files via upload". Uploading through the GitHub website brings over neither the commit history nor the tags. Whoever has the local repo needs to run `git push origin main --tags`.
3. **Two docs are missing.** The README and the team checklist link to `docs/WORKFLOW.md` and `docs/VIDEO_PLAN.md`, but neither file is in the repo.
4. **Placeholders were never filled in:**
   - README lines 8–10: repo URL, lab group, members
   - README lines 79 and 105: `<REPO_URL>` in the compare links
   - README line 137: `<FILL IN AFTER RECORDING>` for the video
   - Report cover page (both the PDF and the .docx): `<LAB GROUP> / Group <N>` and `<PASTE GITHUB REPOSITORY URL>`

Items 1 and 2 matter most, since they're what a grader will notice first. Once you've pushed `TestConfigs.java`, send me the link again and I'll run the full test suite to confirm everything passes.

### Me

alright got it thanks man

### Claude

Anytime. Push `TestConfigs.java` and the tags, then send the link again and I'll rerun the full test suite for you. Good luck with the submission!
