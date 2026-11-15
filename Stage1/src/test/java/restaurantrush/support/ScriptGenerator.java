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

        List<String> victory = GreedyManager.playToEnd(new GameController(GameConfig.baseSetting()));
        Files.write(dir.resolve("stage1-victory.txt"), victory);

        // Defeat run: a passive manager who waits every turn (18 turns x 2 staff = 36 "wait" lines).
        Files.write(dir.resolve("stage1-defeat.txt"), Collections.nCopies(36, "0"));

        System.out.println("Wrote " + victory.size() + " victory lines and 36 defeat lines to " + dir.toAbsolutePath());
    }
}
