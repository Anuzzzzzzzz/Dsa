import java.util.Arrays;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ForkJoinPool;


public class ParallelMergeSort {

    private static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int start;
        private final int end;

        public MergeSortTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= 1) {
                return;
            }

            int mid = (start + end) / 2;

            MergeSortTask leftTask = new MergeSortTask(array, start, mid);
            MergeSortTask rightTask = new MergeSortTask(array, mid, end);

            invokeAll(leftTask, rightTask);

            merge(array, start, mid, end);
        }

        private void merge(int[] array, int start, int mid, int end) {
            int[] merged = new int[end - start];
            int leftIndex = start;
            int rightIndex = mid;
            int mergedIndex = 0;

            while (leftIndex < mid && rightIndex < end) {
                if (array[leftIndex] < array[rightIndex]) {
                    merged[mergedIndex++] = array[leftIndex++];
                } else {
                    merged[mergedIndex++] = array[rightIndex++];
                }
            }

            while (leftIndex < mid) {
                merged[mergedIndex++] = array[leftIndex++];
            }

            while (rightIndex < end) {
                merged[mergedIndex++] = array[rightIndex++];
            }

            System.arraycopy(merged, 0, array, start, merged.length);
        }
    }

    public static void parallelMergeSort(int[] array) {
        try (ForkJoinPool pool = new ForkJoinPool()) {
            MergeSortTask task = new MergeSortTask(array, 0, array.length);
            pool.invoke(task);
        }
    }

    public static void main(String[] args) {
        int[] inputArray = {5, 3, 9, 1, 7, 2, 8, 4, 6};
        parallelMergeSort(inputArray);
        System.out.println("Sorted array: " + Arrays.toString(inputArray));
    }
}
