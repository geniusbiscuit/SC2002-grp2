package restaurantrush.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class MoneyTest {

    @Test
    void formatsCentsAsDollarsWithTwoDecimals() {
        assertEquals("$9.00", Money.format(900));
        assertEquals("$24.30", Money.format(2430));
        assertEquals("$0.05", Money.format(5));
    }

    @Test
    void percentOfRoundsHalfUpToTheNearestCent() {
        assertEquals(2430, Money.percentOf(2700, 90));
        assertEquals(503, Money.percentOf(1005, 50)); // 502.5 -> 503
        assertEquals(1350, Money.percentOf(1500, 90));
    }
}
