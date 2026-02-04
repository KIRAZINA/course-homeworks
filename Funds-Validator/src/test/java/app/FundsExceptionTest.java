package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FundsExceptionTest {

    @Test
    void testFundsExceptionMessage() {
        // Exception should store and return the correct message
        FundsException ex = new FundsException("Insufficient funds!");
        assertEquals("Insufficient funds!", ex.getMessage());
    }

    @Test
    void testFundsExceptionIsInstanceOfException() {
        // FundsException should be a subclass of Exception
        FundsException ex = new FundsException("Test");
        assertTrue(ex instanceof Exception);
    }
}
