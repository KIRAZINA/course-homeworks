package app;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void testGetBalanceInitial() throws Exception {
        var method = Main.class.getDeclaredMethod("getBalance");
        method.setAccessible(true);
        double balance = (double) method.invoke(null);
        assertEquals(1000.00, balance, 0.001);
    }

    @Test
    void testGetBalanceAfterWithdrawal() throws Exception {
        var method = Main.class.getDeclaredMethod("getBalance", double.class, double.class);
        method.setAccessible(true);
        double balance = (double) method.invoke(null, 1000.00, 200.00);
        assertEquals(800.00, balance, 0.001);
    }

    @Test
    void testValidateAmountInsufficientFunds() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        var method = Main.class.getDeclaredMethod("validateAmount", double.class, double.class);
        method.setAccessible(true);
        method.invoke(null, 1000.00, 1200.00);

        String output = outContent.toString().replace("\r\n", "\n").trim();
        assertTrue(output.contains("Insufficient funds!"));
    }

    @Test
    void testValidateAmountSufficientFunds() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        var method = Main.class.getDeclaredMethod("validateAmount", double.class, double.class);
        method.setAccessible(true);
        method.invoke(null, 1000.00, 200.00);

        String output = outContent.toString().replace("\r\n", "\n").trim();
        assertTrue(output.contains("Funds are OK. Purchase paid."));

        double balance = extractBalance(output);
        assertEquals(800.00, balance, 0.001);
    }

    @Test
    void testValidateAmountExactBalance() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        var method = Main.class.getDeclaredMethod("validateAmount", double.class, double.class);
        method.setAccessible(true);
        method.invoke(null, 1000.00, 1000.00);

        String output = outContent.toString().replace("\r\n", "\n").trim();
        assertTrue(output.contains("Funds are OK. Purchase paid."));

        double balance = extractBalance(output);
        assertEquals(0.00, balance, 0.001);
    }

    @Test
    void testValidateAmountZeroWithdrawal() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        var method = Main.class.getDeclaredMethod("validateAmount", double.class, double.class);
        method.setAccessible(true);
        method.invoke(null, 1000.00, 0.00);

        String output = outContent.toString().replace("\r\n", "\n").trim();
        assertTrue(output.contains("Funds are OK. Purchase paid."));

        double balance = extractBalance(output);
        assertEquals(1000.00, balance, 0.001);
    }

    @Test
    void testValidateAmountNegativeWithdrawal() throws Exception {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        var method = Main.class.getDeclaredMethod("validateAmount", double.class, double.class);
        method.setAccessible(true);
        method.invoke(null, 1000.00, -200.00);

        String output = outContent.toString().replace("\r\n", "\n").trim();
        assertTrue(output.contains("Funds are OK. Purchase paid."));

        double balance = extractBalance(output);
        assertEquals(1200.00, balance, 0.001);
    }

    // Helper method to extract balance number from output
    private double extractBalance(String output) {
        String[] lines = output.split("\n");
        String lastLine = lines[lines.length - 1]; // беремо останній рядок
        String balanceStr = lastLine.substring(lastLine.indexOf("USD") + 4).trim();
        balanceStr = balanceStr.replace(",", "."); // замінюємо кому на крапку
        return Double.parseDouble(balanceStr);
    }
}
