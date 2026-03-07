package app.utils;

import app.annotations.MethodInfo;
import app.annotations.Author;

/**
 * Utility class for array operations.
 */
@Author(firstName = "Kirilo", lastName = "Zinchenko")
public class ArrayUtils {

    /**
     * Finds the maximum element in the array.
     *
     * @param array the input array
     * @return the maximum value in the array
     * @throws IllegalArgumentException if the array is null or empty
     */
    @MethodInfo(description = "Finds the maximum element in the array")
    public static int findMax(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        int max = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] > max) {
                max = array[i];
            }
        }
        return max;
    }

    /**
     * Finds the minimum element in the array.
     *
     * @param array the input array
     * @return the minimum value in the array
     * @throws IllegalArgumentException if the array is null or empty
     */
    @MethodInfo(description = "Finds the minimum element in the array")
    public static int findMin(int[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array must not be null or empty");
        }
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (array[i] < min) {
                min = array[i];
            }
        }
        return min;
    }
}
