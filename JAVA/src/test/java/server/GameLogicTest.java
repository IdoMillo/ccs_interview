package server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class GameLogicTest {
    private final GameLogic gameLogic = new GameLogic();

    @Test
    void testValidateGuess() {
        // Guess correctness
        String outOfBounds = "Number out of range, please guess between 1 and 100.";
        String invalidIn = "Invalid input, please enter a number.";
        Exception exception = assertThrows(IllegalArgumentException.class, () -> gameLogic.validateGuess("1"));
        assertEquals(outOfBounds, exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> gameLogic.validateGuess("100"));
        assertEquals(outOfBounds, exception.getMessage());
        exception = assertThrows(IllegalArgumentException.class, () -> gameLogic.validateGuess("abc"));
        assertEquals(invalidIn, exception.getMessage());

        // Number generator
        int secret = gameLogic.getSecretNumber();
        assertTrue(gameLogic.checkGuessCorrectness(secret));
    }
}
