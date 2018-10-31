package threads.caveofprogramming.part6;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This tutorial will demonstrate countdown latches.
 */

public class App {

    public static void main (String [] args) {
        /**
         * There are a few thread-safe classes in Java that allow us to
         * avoid "synchronization" hazards.
         */
        CountDownLatch latch = new CountDownLatch(3); // wait until latch counts down to 0
        ExecutorService exe = Executors.newFixedThreadPool(3);

        // Submit some Jobs
        for (int i=0; i<3; i++) {
            exe.submit(new Job(latch));
        }
        try {
            // Causes the current thread to wait until the latch has counted down to zero
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        exe.shutdown();
        System.out.println("Exit.");
    }
}

class Job
        implements Runnable {

    private CountDownLatch latch;

    public Job (CountDownLatch l) {
        latch = l;
    }

    @Override
    public void run() {
        System.out.println("Running... " + Thread.currentThread());
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        latch.countDown();
        System.out.println("Finished... " + Thread.currentThread());
    }
}
