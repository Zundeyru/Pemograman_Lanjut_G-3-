import org.example.MinFinder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinFinderTest {

    // Skenario 1: a=1, b=2, c=3 → minimum 1
    @Test
    void testFindMin_PositiveAscending() {
        assertEquals(1, MinFinder.findMin(1, 2, 3));
    }

    // Skenario 2: a=-1, b=-2, c=-3 → minimum -3
    @Test
    void testFindMin_Negatives() {
        assertEquals(-3, MinFinder.findMin(-1, -2, -3));
    }

    // Skenario 3: a=0, b=0, c=1 → minimum 0
    @Test
    void testFindMin_WithZerosAndTie() {
        assertEquals(0, MinFinder.findMin(0, 0, 1));
    }
}
