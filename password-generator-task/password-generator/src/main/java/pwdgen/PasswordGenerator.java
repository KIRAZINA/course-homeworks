package pwdgen;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class for generating cryptographically strong random passwords.
 * Guarantees at least one character from each of the following categories:
 * lowercase letters, uppercase letters, digits, and special characters.
 */
public final class PasswordGenerator {

    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE = LOWERCASE.toUpperCase();
    private static final String DIGITS    = "0123456789";
    private static final String SPECIAL   = "!@#$%^&*()_+-=[]{}|;:,.<>?/~";

    private static final String ALL_CHARS = LOWERCASE + UPPERCASE + DIGITS + SPECIAL;

    private static final SecureRandom RANDOM = new SecureRandom();

    private PasswordGenerator() {
        // Prevent instantiation
    }

    /**
     * Generates a random password of the specified length.
     * The password is guaranteed to contain at least one lowercase letter,
     * one uppercase letter, one digit, and one special character.
     *
     * @param length the desired password length (must be >= 4)
     * @return a randomly generated password
     * @throws IllegalArgumentException if length is less than 4
     */
    public static String generatePassword(int length) {
        if (length < 4) {
            throw new IllegalArgumentException("Password length must be at least 4");
        }

        List<Character> chars = new ArrayList<>(length);

        // Guarantee one character from each required category
        chars.add(randomChar(LOWERCASE));
        chars.add(randomChar(UPPERCASE));
        chars.add(randomChar(DIGITS));
        chars.add(randomChar(SPECIAL));

        // Fill remaining positions with random characters from all allowed set
        for (int i = 4; i < length; i++) {
            chars.add(randomChar(ALL_CHARS));
        }

        // Shuffle to prevent predictable placement of guaranteed characters
        Collections.shuffle(chars, RANDOM);

        StringBuilder password = new StringBuilder(length);
        chars.forEach(password::append);

        return password.toString();
    }

    private static char randomChar(String source) {
        return source.charAt(RANDOM.nextInt(source.length()));
    }
}