package app.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ArrayUtils methods tests")
class ArrayUtilsTest {

    // ────────────────────────────────────────────────────────────────
    // findMax
    // ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findMax should return the maximum value in a normal array")
    void findMax_normalArray_returnsMax() {
        int[] arr = {5, 2, 9, 1, 7};
        assertEquals(9, ArrayUtils.findMax(arr));
    }

    @Test
    @DisplayName("findMax should return the only element when array has one element")
    void findMax_singleElement_returnsThatElement() {
        int[] arr = {42};
        assertEquals(42, ArrayUtils.findMax(arr));
    }

    @Test
    @DisplayName("findMax should return negative max correctly")
    void findMax_allNegative_returnsLeastNegative() {
        int[] arr = {-5, -1, -10, -3};
        assertEquals(-1, ArrayUtils.findMax(arr));
    }

    @ParameterizedTest(name = "[{index}] array = {0} → expected max = {1}")
    @MethodSource("maxProvider")
    @DisplayName("findMax with various inputs via parameterized test")
    void findMax_parameterized(int[] input, int expected) {
        assertEquals(expected, ArrayUtils.findMax(input));
    }

    static Stream<Arguments> maxProvider() {
        return Stream.of(
                Arguments.of(new int[]{1, 3, 2}, 3),
                Arguments.of(new int[]{10}, 10),
                Arguments.of(new int[]{-100, -50, -200}, -50),
                Arguments.of(new int[]{0, 0, 0}, 0)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("findMax should throw IllegalArgumentException on null or empty array")
    void findMax_nullOrEmpty_throwsException(int[] invalid) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> ArrayUtils.findMax(invalid)
        );
        assertTrue(ex.getMessage().contains("null or empty"));
    }

    // ────────────────────────────────────────────────────────────────
    // findMin
    // ────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("findMin should return the minimum value in a normal array")
    void findMin_normalArray_returnsMin() {
        int[] arr = {5, 2, 9, 1, 7};
        assertEquals(1, ArrayUtils.findMin(arr));
    }

    @Test
    @DisplayName("findMin should return the only element when array has one element")
    void findMin_singleElement_returnsThatElement() {
        int[] arr = {-7};
        assertEquals(-7, ArrayUtils.findMin(arr));
    }

    @ParameterizedTest(name = "[{index}] array = {0} → expected min = {1}")
    @MethodSource("minProvider")
    void findMin_parameterized(int[] input, int expected) {
        assertEquals(expected, ArrayUtils.findMin(input));
    }

    static Stream<Arguments> minProvider() {
        return Stream.of(
                Arguments.of(new int[]{4, 1, 7, 3}, 1),
                Arguments.of(new int[]{0}, 0),
                Arguments.of(new int[]{-3, -8, -1, -5}, -8),
                Arguments.of(new int[]{100, 100, 100}, 100)
        );
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("findMin should throw IllegalArgumentException on null or empty array")
    void findMin_nullOrEmpty_throwsException(int[] invalid) {
        assertThrows(IllegalArgumentException.class, () -> ArrayUtils.findMin(invalid));
    }
}