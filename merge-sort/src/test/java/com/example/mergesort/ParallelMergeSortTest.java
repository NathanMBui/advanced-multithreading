package com.example.mergesort;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ParallelMergeSortTest {

    @TestFactory
    public Stream<DynamicTest> testMergeSort() {
        List<TestCase> testMap = List.of(
                TestCase.of(1, 2, 3, 4, 5, 6).expect(1, 2, 3, 4, 5, 6),
                TestCase.of(6, 5, 4, 3, 2, 1).expect(1, 2, 3, 4, 5, 6),
                TestCase.of(0, 5, 3, 6, 1, 2).expect(0, 1, 2, 3, 5, 6),
                TestCase.of(-1, -2, 4, 0, 1, 2).expect(-2, -1, 0, 1, 2, 4),
                TestCase.of(1, -1, 2, -2, 0, 1, -3).expect(-3, -2, -1, 0, 1, 1, 2)
        );

        return testMap.stream().map(testCase ->
                DynamicTest.dynamicTest("test " + Arrays.toString(testCase.input),
                        () -> {
                            new ParallelMergeSort(testCase.input).sort();
                            assertArrayEquals(testCase.expected, testCase.input);
                        }
                ));
    }

    private static class TestCase {
        private final int[] input;
        private int[] expected;

        public TestCase(int... input) {
            this.input = input;
        }

        public TestCase(int[] input, int[] expected) {
            this.input = input;
            this.expected = expected;
        }

        public static TestCase of(int... input) {
            return new TestCase(input);
        }

        public TestCase expect(int... output) {
            this.expected = output;
            return this;
        }
    }
}
