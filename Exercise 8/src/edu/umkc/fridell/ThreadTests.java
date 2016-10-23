package edu.umkc.fridell;

public class ThreadTests {

  private static int x,y,z;
  //public static final int ITERATIONS = Integer.MAX_VALUE;
  public static final int ITERATIONS = 1000000;

  public static void main(String[] args) throws InterruptedException {
    long startTime = System.nanoTime();
    ///////////////////////////////////
    Thread t1 = new Thread(syncTest);
    Thread t2 = new Thread(syncTest);

    t1.start();
    t2.start();

    t1.join();
    t2.join();
    ///////////////////////////////////
    long endTime = System.nanoTime();
    System.out.println("Computation took " + ((endTime - startTime) / 1000000) + " milliseconds");
    printValues();
  }

  public synchronized static void fsync() {
    x = x + 1;
    y = y + 1;
    z = z + x - y;
  }

  public static void f() {
    x = x + 1;
    y = y + 1;
    z = z + x - y;
  }


  public static void printValues() {
    System.out.println("x = " + x);
    System.out.println("y = " + y);
    System.out.println("z = " + z);
  }

  static Runnable syncTest = () -> {
    for (int i=0; i<ITERATIONS; ++i) {
      fsync();
    }
  };

  static Runnable threadTest = new Runnable() {
    public void run() {
      for (int i=0; i<ITERATIONS; ++i) {
        f();
      }
    }
  };
}
