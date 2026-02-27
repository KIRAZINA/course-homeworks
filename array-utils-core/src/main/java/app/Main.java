package app;

import java.util.Arrays;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        // Create an array with random numbers
        int size = 20; // increased size for better demonstration
        int[] array = new int[size];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(100); // Random numbers between 0 and 99
        }

        System.out.println("Original array: " + Arrays.toString(array));

        // Apply merge sort
        ArrayUtils.mergeSort(array);
        System.out.println("Sorted array: " + Arrays.toString(array));

        boolean sortedCorrectly = true;
        for (int i = 1; i < array.length; i++) {
            if (array[i - 1] > array[i]) {
                sortedCorrectly = false;
                break;
            }
        }
        System.out.println("Sort verification: " + (sortedCorrectly ? "SUCCESS" : "FAIL"));

        // Perform binary search for a random target (guaranteed to exist)
        int target = array[random.nextInt(array.length)];
        int index = ArrayUtils.binarySearch(array, target);
        System.out.println("Search for existing value " + target + ": index = " + index);

        // Perform binary search for a non-existing value
        int notFoundTarget = -1; // definitely not in array
        int notFoundIndex = ArrayUtils.binarySearch(array, notFoundTarget);
        System.out.println("Search for non-existing value " + notFoundTarget + ": index = " + notFoundIndex);
    }
}
