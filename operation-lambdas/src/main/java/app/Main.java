package app;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.Random;

public class Main {

    // Functional interface for math operations
    @FunctionalInterface
    interface MathOperation {
        int operate(int a, int b);
    }

    // Functional interface for string manipulation
    @FunctionalInterface
    interface StringManipulator {
        String manipulate(String input);
    }

    // Class with static method to count uppercase letters
    static class StringProcessor {
        // Count how many uppercase letters are in the string
        public static int countUppercase(String s) {
            if (s == null) return 0; // defensive check
            int count = 0;
            for (char c : s.toCharArray()) {
                if (Character.isUpperCase(c)) {
                    count++;
                }
            }
            return count;
        }
    }

    // Class with static method to generate random numbers
    static class RandomNumberGenerator {
        // Single Random instance for the whole class
        private static final Random RANDOM = new Random();

        // Generate random integer between min and max (inclusive)
        public static int generateRandomNumber(int min, int max) {
            if (min > max) {
                throw new IllegalArgumentException("min must be <= max");
            }
            return min + RANDOM.nextInt(max - min + 1);
        }
    }

    public static void main(String[] args) {
        // Anonymous class implementing addition
        MathOperation addition = new MathOperation() {
            @Override
            public int operate(int a, int b) {
                return a + b;
            }
        };

        // Lambda expression for string manipulation (to uppercase, safe for null)
        StringManipulator toUpperCase = input -> (input == null) ? null : input.toUpperCase();

        // Method reference for counting uppercase letters
        Function<String, Integer> uppercaseCounter = StringProcessor::countUppercase;

        // Supplier for random number between 1 and 100
        Supplier<Integer> random100 = () -> RandomNumberGenerator.generateRandomNumber(1, 100);

        // Demonstration of all created objects/functions
        System.out.println("Addition result (5 + 7): " + addition.operate(5, 7));
        System.out.println("Addition result (-3 + 8): " + addition.operate(-3, 8));

        System.out.println("String to uppercase: " + toUpperCase.manipulate("hello world"));
        System.out.println("String to uppercase with accents: " + toUpperCase.manipulate("café naïve"));
        System.out.println("String to uppercase with null: " + toUpperCase.manipulate(null));

        System.out.println("Uppercase count in 'HeLLo WoRLd': " + uppercaseCounter.apply("HeLLo WoRLd"));
        System.out.println("Uppercase count in 'ЯКЩО ТАК': " + uppercaseCounter.apply("ЯКЩО ТАК"));
        System.out.println("Uppercase count in null: " + uppercaseCounter.apply(null));

        System.out.println("Random number (1-100): " + random100.get());
        System.out.println("Another random number (1-100): " + random100.get());

        System.out.println("Random number in range -5 to 5: " + RandomNumberGenerator.generateRandomNumber(-5, 5));
    }
}
