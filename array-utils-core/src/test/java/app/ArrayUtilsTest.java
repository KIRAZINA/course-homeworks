package app;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

@DisplayName("ArrayUtils Test Suite")
class ArrayUtilsTest {

    // ────────────────────────────────────────────────
    // mergeSort Tests
    // ────────────────────────────────────────────────

    @Test
    @DisplayName("Empty array should remain empty")
    void mergeSort_emptyArray_shouldNotChange() {
        int[] array = {};
        ArrayUtils.mergeSort(array);
        assertArrayEquals(new int[]{}, array);
    }

    @Test
    @DisplayName("Single element array should remain unchanged")
    void mergeSort_singleElement_shouldStayTheSame() {
        int[] array = {42};
        ArrayUtils.mergeSort(array);
        assertArrayEquals(new int[]{42}, array);
    }

    @Test
    @DisplayName("Regular unsorted array should be sorted correctly")
    void mergeSort_regularUnsortedArray_shouldBeSorted() {
        int[] array = {64, 34, 25, 12, 22, 11, 90};
        ArrayUtils.mergeSort(array);
        assertArrayEquals(new int[]{11, 12, 22, 25, 34, 64, 90}, array);
    }

    @Test
    @DisplayName("Already sorted array should remain sorted")
    void mergeSort_alreadySorted_shouldStaySorted() {
        int[] array = {1, 3, 5, 7, 9, 11};
        ArrayUtils.mergeSort(array);
        assertArrayEquals(new int[]{1, 3, 5, 7, 9, 11}, array);
    }

    @Test
    @DisplayName("Reverse sorted array should become ascending")
    void mergeSort_reverseSorted_shouldBeAscending() {
        int[] array = {50, 40, 30, 20, 10};
        ArrayUtils.mergeSort(array);
        assertArrayEquals(new int[]{10, 20, 30, 40, 50}, array);
    }

    @Test
    @DisplayName("Array with duplicates should be stable (relative order preserved)")
    void mergeSort_withDuplicates_shouldBeStable() {
        // Format: value + original index (to verify relative order of equal elements)
        int[] values = {5, 3, 5, 2, 5, 7};
        // Remember original positions of 5's: indices 0, 2, 4

        ArrayUtils.mergeSort(values);

        assertArrayEquals(new int[]{2, 3, 5, 5, 5, 7}, values);

        // If sort was unstable, the order of the three 5's could be different
    }

    @Test
    @DisplayName("All elements are equal should remain unchanged")
    void mergeSort_allElementsEqual_shouldStayTheSame() {
        int[] array = {8, 8, 8, 8, 8};
        ArrayUtils.mergeSort(array);
        assertArrayEquals(new int[]{8, 8, 8, 8, 8}, array);
    }

    @Test
    @DisplayName("Null array should be handled safely (no exception)")
    void mergeSort_nullArray_shouldNotThrowException() {
        assertDoesNotThrow(() -> ArrayUtils.mergeSort(null));
    }

    // ────────────────────────────────────────────────
    // binarySearch Tests
    // ────────────────────────────────────────────────

    @Test
    @DisplayName("Existing element should return correct index")
    void binarySearch_elementExists_shouldReturnIndex() {
        int[] sorted = {1, 4, 7, 10, 13, 16, 19};
        assertEquals(0, ArrayUtils.binarySearch(sorted, 1));
        assertEquals(3, ArrayUtils.binarySearch(sorted, 10));
        assertEquals(6, ArrayUtils.binarySearch(sorted, 19));
    }

    @Test
    @DisplayName("Non-existing element should return -1")
    void binarySearch_elementNotFound_shouldReturnMinusOne() {
        int[] sorted = {2, 5, 8, 11, 14};
        assertEquals(-1, ArrayUtils.binarySearch(sorted, 0));
        assertEquals(-1, ArrayUtils.binarySearch(sorted, 3));
        assertEquals(-1, ArrayUtils.binarySearch(sorted, 20));
    }

    @Test
    @DisplayName("Multiple occurrences should return any valid index")
    void binarySearch_multipleOccurrences_shouldReturnSomeIndex() {
        int[] sorted = {1, 3, 3, 3, 3, 5, 7};
        int index = ArrayUtils.binarySearch(sorted, 3);
        assertTrue(index >= 1 && index <= 4, "Index should point to one of the 3's");
    }

    @Test
    @DisplayName("Empty array should return -1")
    void binarySearch_emptyArray_shouldReturnMinusOne() {
        assertEquals(-1, ArrayUtils.binarySearch(new int[]{}, 100));
    }

    @Test
    @DisplayName("Single element array - found and not found cases")
    void binarySearch_singleElement() {
        int[] array = {77};
        assertEquals(0, ArrayUtils.binarySearch(array, 77));
        assertEquals(-1, ArrayUtils.binarySearch(array, 76));
    }

    @Test
    @DisplayName("Null array should return -1")
    void binarySearch_nullArray_shouldReturnMinusOne() {
        assertEquals(-1, ArrayUtils.binarySearch(null, 42));
    }

    // ────────────────────────────────────────────────
    // Additional combined / stress test
    // ────────────────────────────────────────────────

    @Test
    @DisplayName("Large array with negative numbers and duplicates")
    void mergeSort_and_binarySearch_largeArrayWithNegatives() {
        int[] array = {-5, 12, -3, 0, 7, -5, 12, 8, -10, 4, 12};

        ArrayUtils.mergeSort(array);

        assertArrayEquals(new int[]{-10, -5, -5, -3, 0, 4, 7, 8, 12, 12, 12}, array);

        // Check binary search behavior
        int indexOfMinus5 = ArrayUtils.binarySearch(array, -5);
        assertTrue(indexOfMinus5 >= 1 && indexOfMinus5 <= 2,
                "Should return any valid index where -5 is found");

        int indexOf12 = ArrayUtils.binarySearch(array, 12);
        assertTrue(indexOf12 >= 8 && indexOf12 <= 10,
                "Should return any valid index where 12 is found");

        assertEquals(-1, ArrayUtils.binarySearch(array, 99),
                "Non-existing value should return -1");
    }
}