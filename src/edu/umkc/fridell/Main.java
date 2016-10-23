package edu.umkc.fridell;

public class Main {

  public static void main(String[] args) throws InterruptedException {
    final int rows = 20;
    final int cols = 10000000;
    new ThreadMatrix(rows, cols);
  }
}
