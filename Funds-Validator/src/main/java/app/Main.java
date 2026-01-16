package app;

import java.util.Scanner;

public class Main {

    static double balance;

    public static void main(String[] args) {
        balance = getBalance();
        validateAmount(balance, getAmount());
    }

    // Method returns initial balance
    private static double getBalance() {
        return 1000.00;
    }

    // Method prompts user for purchase amount
    private static double getAmount() {
        System.out.printf("Balance is USD %.2f.%nEnter purchase amount, USD: ", balance);
        Scanner scanner = new Scanner(System.in);
        return scanner.nextDouble();
    }

    // Method validates funds and prints result
    private static void validateAmount(double balance, double withdrawal) {
        if (withdrawal > balance) {
            try {
                throw new FundsException("Insufficient funds!");
            } catch (FundsException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            balance = getBalance(balance, withdrawal);
            System.out.printf("Funds are OK. Purchase paid.%nBalance is USD %.2f%n", balance);
        }
    }

    // Method calculates remaining balance after purchase
    private static double getBalance(double balance, double withdrawal) {
        return balance - withdrawal;
    }
}
