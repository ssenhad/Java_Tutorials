package threads.caveofprogramming.part14;

import java.util.Random;
import java.util.concurrent.*;

/**
 * Tutorial to demonstrate interrupting threads.
 *
 * A recurring theme of multithreaded code is to catch Exceptions, particularly the
 * InterruptedException which tends to make our code look messy and less understandable in terms of
 * the logic of what the code is actually trying to do.
 */


public class App {

    public void firstDemo () {
        System.out.println("Starting...");

        Thread t1 = new Thread (new Runnable() {

            @Override
            public void run () {
                Random r = new Random();
                for (int i=0; i<1E6; i++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        System.out.println("run() method interrupted...");
                        break;
                    }
                    Math.sin(r.nextDouble());
                }

            }
        });

        t1.start();

        try {
            Thread.sleep(500);
            t1.interrupt(); // interrupts this thread by setting a flag belonging to thread
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done.");
    }


    public void secondDemo () {
        System.out.println("Starting...");

        Thread t1 = new Thread (new Runnable() {

            @Override
            public void run () {
                Random r = new Random();
                for (int i=0; i<1E6; i++) {
                    if (Thread.currentThread().isInterrupted()) { // interrupted flag status
                        System.out.println("run() method interrupted!");
                        break;
                    }
                    Math.sin(r.nextDouble());
                }

            }
        });

        t1.start();

        try {
            Thread.sleep(500);
            t1.interrupt(); // interrupts this thread by setting a flag belonging to thread
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Done.");
    }

    // Main demo using thread pool with Future and Callable
    public static void main (String [] args) throws InterruptedException {

        System.out.println("Starting...");

        ExecutorService exec = Executors.newCachedThreadPool();

        Future<Double> fu = exec.submit(new Callable<Double>() {

            @Override
            public Double call () throws Exception {

                Random ran = new Random();

                double val = 0.0;
                int counter = 0;

                for (int i=0; i<1E8; i++) { // 100 million

                    if (Thread.currentThread().isInterrupted()) { // interrupted flag status
                        System.out.println("run() method interrupted! too many");
                        break;
                    }

                    val = Math.sin(ran.nextDouble());
                    System.out.println("val " + counter + " = " + val);
                    counter++;
                }
                return val;
            }
        });

        exec.shutdown(); // terminate managerial thread once finished running thread computations

        Thread.sleep(500);

        //executioner.shutdownNow(); // set all interrupted flags to true
        /**
         * Use the future object to cancel the running thread
         */
        fu.cancel(true); // cancel execution of this task

        /**
         * Blocks until all tasks have completed execution after a shutdown request, or the
         * timeout occurs, or the current thread is interrupted, whichever happens first.
         */
        exec.awaitTermination(1, TimeUnit.DAYS);

        System.out.println("Done.");

    }
}
