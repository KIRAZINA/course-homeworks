package app;

// Custom exception for insufficient funds
public class FundsException extends Exception {

    public FundsException(String message) {
        super(message);
    }
}
