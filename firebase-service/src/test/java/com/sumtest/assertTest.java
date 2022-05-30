package com.sumtest;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.assertThat;

public class assertTest {

    @Test
    void givenSequentialStreamOfNumbers_whenReducingSumWithIdentityFive_thenResultIsCorrect() {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        int sum = listOfNumbers.stream().reduce(5, Integer::sum);
        System.out.println(" paraööe 1" + sum );
        assertThat(sum).isEqualTo(15);
    }

    @Test
    void givenParallelStreamOfNumbers_whenReducingSumWithIdentityFive_thenResultIsNotCorrect() {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        int sum = listOfNumbers.parallelStream().reduce(5, Integer::sum);
        System.out.println(" paraööe " + sum );
        assertThat(sum).isNotEqualTo(15);
    }

    @Test
    public void givenParallelStreamOfNumbers_whenUsingCustomThreadPool_thenResultIsCorrect()
            throws InterruptedException, ExecutionException {
        List<Integer> listOfNumbers = Arrays.asList(1, 2, 3, 4);
        ForkJoinPool customThreadPool = new ForkJoinPool(4);
        int sum = customThreadPool.submit(
                () -> listOfNumbers.parallelStream().reduce(0, Integer::sum)).get();
        customThreadPool.shutdown();
        assertThat(sum).isEqualTo(10);
    }
}
