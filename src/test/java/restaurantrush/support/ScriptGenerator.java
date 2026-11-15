package restaurantrush.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

import restaurantrush.control.GameConfig;
import restaurantrush.control.GameController;

// Regenerates the reproducible input scripts in scripts/.
// Run from the project root after compiling main and test code:
// java -cp out:out-test restaurantrush.support.ScriptGenerator
public class ScriptGenerator {

    public static void main(String[] args) throws IOException {
        Path dir = Path.of("scripts");
        Files.createDirectories(dir);

        // Defeat runs: a passive manager who waits every turn (18 turns x 2 staff = 36 "wait" lines).
        List<String> passive = Collections.nCopies(36, "0");

        // Stage 1 crowd (six Regular customers): replay with --regular-only.
        Files.write(dir.resolve("stage1-victory.txt"),
                GreedyManager.playToEnd(new GameController(GameConfig.regularOnlySetting())));
        Files.write(dir.resolve("stage1-defeat.txt"), passive);

        // Stage 2 base setting (includes a VIP and a Critic).
        Files.write(dir.resolve("stage2-victory.txt"),
                GreedyManager.playToEnd(new GameController(GameConfig.baseSetting())));
        Files.write(dir.resolve("stage2-defeat.txt"), passive);

        // Stage 3 game (Host + combos + Happy Hour): the manager switches Happy Hour on when the VIP is
        // waiting to order, so the run shows the brief's example (VIP Burger during Happy Hour = $10.80).
        Files.write(dir.resolve("stage3-victory.txt"),
                GreedyManager.playToEnd(new GameController(GameConfig.fullSetting()),
                        GreedyManager.WHEN_A_VIP_IS_WAITING));
        // Passive manager again: 18 turns x 4 prompts (Happy Hour, Host, Waiter, Chef) = 72 "0" lines.
        Files.write(dir.resolve("stage3-defeat.txt"), Collections.nCopies(72, "0"));

        System.out.println("Scripts written to " + dir.toAbsolutePath());
    }
}
