package com.example.mergesort;

import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {

    private final ForkJoinPool fjp = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    private final int[] input;

    public ParallelMergeSort(int[] input) {
        this.input = input;
    }

    public void sort() {
        fjp.invoke(new MergeSortAction(0, input.length - 1));
    }

    public void mergeArrays(int left, int mid, int right) {
        int i = 0, j = 0, k = left;
        int[] arr1 = Arrays.copyOfRange(input, left, mid + 1);
        int[] arr2 = Arrays.copyOfRange(input, mid + 1, right + 1);
        int[] arr3 = input;
        int n1 = arr1.length;
        int n2 = arr2.length;
        // Traverse both array
        while (i < n1 && j < n2) {
            if (arr1[i] < arr2[j])
                arr3[k++] = arr1[i++];
            else
                arr3[k++] = arr2[j++];
        }

        // Store remaining elements of first array
        while (i < n1)
            arr3[k++] = arr1[i++];

        // Store remaining elements of second array
        while (j < n2)
            arr3[k++] = arr2[j++];
    }

    private class MergeSortAction extends RecursiveAction {

        int left;
        int right;

        public MergeSortAction(int left, int right) {
            this.left = left;
            this.right = right;
        }

        @Override
        protected void compute() {
            if (left < right) {
                int mid = (right - left) / 2 + left;
                MergeSortAction task1 = new MergeSortAction(left, mid);
                MergeSortAction task2 = new MergeSortAction(mid + 1, right);
                invokeAll(task1, task2);
                mergeArrays(left, mid, right);
            }
        }
    }
}
