package edu.umkc.fridell;

import java.util.Random;

public class ThreadMatrix {

  private int ROWS;
  private int COLUMNS;

  private int [][] matrix;
  private volatile int[] rowSum;

  public ThreadMatrix(int rows, int columns) throws InterruptedException {
    this.ROWS = rows;
    this.COLUMNS = columns;
    matrix = new int[ROWS][COLUMNS];
    rowSum = new int[ROWS];

    long startTime = System.nanoTime();
    ///////////////////////////////////
    Random rand = new Random();

    Thread mainThread = new Thread(() -> {
      for (int row=0; row < ROWS; ++row) {
        for (int col=0; col < COLUMNS; ++col) {
          matrix[row][col] = rand.nextInt(200);
        }
      }

      for (int row=0; row < ROWS; ++row) {

        Runnable r = new MyThread(row);
        new Thread(r).start();
      }
    });

    mainThread.start();
    while (mainThread.activeCount() > 1) {
      wait(50);
    }
    ///////////////////////////////////
    long endTime = System.nanoTime();
    System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
    System.out.println("Result: " + sumRows());
  }

  private int sumRows() {
    int sum = 0;
    for(int x : rowSum) {
      sum += x;
    }
    return sum;
  }
  private class MyThread implements Runnable {

    private int row;
    MyThread(int row) {
      this.row = row;
    }

    public void run() {
      for(int col = 0; col<COLUMNS; ++col) {
        rowSum[row] += (int) Math.log(matrix[row][col]);
      }
    }
  }

}
