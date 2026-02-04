package app;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testCurrencyConstant() {
        // Currency constant should be EUR
        assertEquals("EUR", Constants.CURRENCY);
    }

    @Test
    void testMeasureConstant() {
        // Measure constant should be pcs.
        assertEquals("pcs.", Constants.MEASURE);
    }
}
