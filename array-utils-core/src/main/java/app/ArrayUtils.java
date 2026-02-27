package app;

public class ArrayUtils {

    /**
     * Sorts the given array using merge sort algorithm.
     *
     * @param array the array to be sorted
     */
    public static void mergeSort(int[] array) {
        if (array == null || array.length <= 1) {
            return; // Nothing to sort
        }
        mergeSortRecursive(array, 0, array.length - 1);
    }

    // Recursive helper method for merge sort
    private static void mergeSortRecursive(int[] array, int left, int right) {
        if (array == null) return; // paranoid safety check
        if (left < right) {
            int middle = (left + right) / 2;

            // Sort left half
            mergeSortRecursive(array, left, middle);

            // Sort right half
            mergeSortRecursive(array, middle + 1, right);

            // Merge both halves
            merge(array, left, middle, right);
        }
    }

    // Merges two sorted halves of the array
    private static void merge(int[] array, int left, int middle, int right) {
        int n1 = middle - left + 1;
        int n2 = right - middle;

        int[] leftArray = new int[n1];
        int[] rightArray = new int[n2];

        // Copy data to temporary arrays
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, middle + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;

        // Merge the temp arrays back into the main array
        while (i < n1 && j < n2) {
            // <= ensures stable sort (equal elements keep relative order)
            if (leftArray[i] <= rightArray[j]) {
                array[k++] = leftArray[i++];
            } else {
                array[k++] = rightArray[j++];
            }
        }

        // Copy remaining elements of leftArray if any
        while (i < n1) {
            array[k++] = leftArray[i++];
        }

        // Copy remaining elements of rightArray if any
        while (j < n2) {
            array[k++] = rightArray[j++];
        }
    }

    /**
     * Performs binary search on a sorted array.
     *
     * @param array  the sorted array
     * @param target the value to search for
     * @return index of target if found, otherwise -1
     */
    public static int binarySearch(int[] array, int target) {
        if (array == null) {
            return -1;
        }

        int left = 0;
        int right = array.length - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            if (array[middle] == target) {
                return middle;
            } else if (array[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }
        return -1;
    }
}