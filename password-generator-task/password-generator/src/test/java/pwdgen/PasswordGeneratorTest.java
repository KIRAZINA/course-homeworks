package pwdgen;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;

class PasswordGeneratorTest {

    @ParameterizedTest
    @ValueSource(ints = {4, 8, 12, 16, 20})
    void shouldGeneratePasswordOfExactRequestedLength(int length) {
        String password = PasswordGenerator.generatePassword(length);
        assertThat(password).hasSize(length);
    }

    @Test
    void shouldContainAtLeastOneLowercaseLetter() {
        String password = PasswordGenerator.generatePassword(12);
        assertThat(password).matches(".*[a-z].*");
    }

    @Test
    void shouldContainAtLeastOneUppercaseLetter() {
        String password = PasswordGenerator.generatePassword(12);
        assertThat(password).matches(".*[A-Z].*");
    }

    @Test
    void shouldContainAtLeastOneDigit() {
        String password = PasswordGenerator.generatePassword(12);
        assertThat(password).matches(".*[0-9].*");
    }

    @Test
    void shouldContainAtLeastOneSpecialCharacter() {
        String password = PasswordGenerator.generatePassword(12);
        assertThat(password).matches(".*[!@#$%^&*()_+=\\[\\]{}|;:,.<>?/~-].*");
    }

    @Test
    void shouldThrowExceptionWhenLengthIsTooSmall() {
        assertThatThrownBy(() -> PasswordGenerator.generatePassword(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("at least 4");
    }

    @Test
    void shouldThrowExceptionWhenLengthIsZeroOrNegative() {
        assertThatThrownBy(() -> PasswordGenerator.generatePassword(0))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> PasswordGenerator.generatePassword(-5))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(ints = {50, 100})
    void shouldUseDifferentCharactersAcrossMultipleGenerations(int iterations) {
        Set<String> generated = new HashSet<>();

        for (int i = 0; i < iterations; i++) {
            String pw = PasswordGenerator.generatePassword(16);
            generated.add(pw);
        }

        assertThat(generated).hasSizeGreaterThan(iterations / 2);
    }
}