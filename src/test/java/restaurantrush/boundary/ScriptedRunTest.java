package restaurantrush.boundary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;

import restaurantrush.control.GameConfig;
import restaurantrush.control.GameController;

// Runs the real console UI against the reproducible scripts in scripts/.
class ScriptedRunTest {

    @Test
    void stage1VictoryScriptStillWinsWithTheStage1Crowd() throws IOException {
        String output = runScript("scripts/stage1-victory.txt", GameConfig.regularOnlySetting());
        assertTrue(output.contains("\nVICTORY\n"), output);
        assertTrue(output.contains("Revenue: $66.00"));
        assertTrue(output.contains("Turns used: 18"));
    }

    @Test
    void stage1DefeatScriptStillLosesWithTheStage1Crowd() throws IOException {
        String output = runScript("scripts/stage1-defeat.txt", GameConfig.regularOnlySetting());
        assertTrue(output.contains("\nDEFEAT\n"), output);
        assertTrue(output.contains("Paid customers: 0"));
        assertTrue(output.contains("loses patience and leaves without paying"));
    }

    @Test
    void stage2VictoryScriptWinsTheMixedBaseSetting() throws IOException {
        String output = runScript("scripts/stage2-victory.txt", GameConfig.baseSetting());
        assertTrue(output.contains("\nVICTORY\n"), output);
        assertTrue(output.contains("Revenue: $73.50"));
        assertTrue(output.contains("#2 VIP"), "the VIP is part of the run");
        assertTrue(output.contains("#4 Critic"), "the Critic is part of the run");
        assertTrue(output.contains("list price $15.00"), "the VIP discount is visible");
    }

    @Test
    void stage2DefeatScriptLosesTheMixedBaseSetting() throws IOException {
        String output = runScript("scripts/stage2-defeat.txt", GameConfig.baseSetting());
        assertTrue(output.contains("\nDEFEAT\n"), output);
        assertTrue(output.contains("Paid customers: 0"));
    }

    @Test
    void badInputIsRejectedAndAskedAgainWithoutUsingTheStaffTask() {
        // not a number, out of range, unknown customer id, then a valid Wait and Wait.
        String input = "abc\n9\n1\n42\n0\n0\n";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        GameController game = new GameController(GameConfig.baseSetting());
        ConsoleUI ui = new ConsoleUI(game, new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(bytes, true, StandardCharsets.UTF_8), true);
        assertThrows(InputEndedException.class, ui::run); // the input stops during turn 2
        String output = bytes.toString(StandardCharsets.UTF_8);
        assertTrue(output.contains("'abc' is not a whole number"));
        assertTrue(output.contains("Please choose one of [0, 1, 2, 3]"));
        assertTrue(output.contains("There is no customer with id 42."));
        assertTrue(output.contains("Your staff task was not used"));
        assertTrue(output.contains("The Waiter waits."));
        assertEquals(2, game.getTurn());
    }

    private static String runScript(String scriptFile, GameConfig config) throws IOException {
        String script = Files.readString(Path.of(scriptFile));
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        GameController game = new GameController(config);
        new ConsoleUI(game, new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8)),
                new PrintStream(bytes, true, StandardCharsets.UTF_8), true).run();
        return bytes.toString(StandardCharsets.UTF_8);
    }
}
