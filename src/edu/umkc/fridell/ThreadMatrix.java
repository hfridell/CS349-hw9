package edu.umkc.fridell;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadMatrix {

  public ThreadMatrix(int rows, int columns) throws InterruptedException {
    int[][] matrix = new int[rows][columns];
    long total = 0;
    long startTime = System.nanoTime();
    Random rand = new Random();
    ExecutorService pool = Executors.newFixedThreadPool(rows);
    ExecutorCompletionService<Long> completionService = new ExecutorCompletionService<>(pool);

    System.out.println("Running with...");
    System.out.println("Rows: " + rows + " Columns: " + columns);
    ///////////////////////////////////
    // Randomization
    for (int row = 0; row < rows; ++row) {
      for (int col = 0; col < columns; ++col) {
        matrix[row][col] = rand.nextInt(200);
      }
    }

    System.out.println("Randomization took " + ((System.nanoTime() - startTime) / 1000000) + " milliseconds");
    startTime = System.nanoTime();

    // Computation
    for (int[] row : matrix) {
      completionService.submit(() -> {
        long sum = 0;
        for (int col : row) {
          sum += (int) Math.log(row[col]);
        }
        return sum;
      });
    }

    for (int i = 0; i < rows; ++i) {
      final Future<Long> future = completionService.take();
      try {
        final Long rowSum = future.get();
        total += rowSum;
      } catch (ExecutionException e) {
        System.err.println("Oops! Something went wrong.");
      }
    }
    ///////////////////////////////////
    System.out.println("Computation took " + ((System.nanoTime() - startTime) / 1000000) + " milliseconds");
    System.out.println("Result: " + total);

    pool.shutdown();
  }
}
