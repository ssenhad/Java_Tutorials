package Buggy_Parallelism;

import static org.junit.Assert.assertEquals;
import org.junit.Test;


import java.util.concurrent.atomic.AtomicInteger;

/**
 * This test program is intended to induce race conditions between the produce and consumer
 * threads because no synchronization of object internal states are in place.
 *
 * Execute the testBug() method that uses JUNit to demonstrate the bugginess.
 */
public class BuggyUnsynchronizedBoundedQueueTest {

    private final static int maxIterations = 100000; // max number of iterations

    private final static int bqSize = 10; // maximum queue size (space for 10 elements)

    /**
     * Atomic counter to track number of iterations
     */
    private final static AtomicInteger count = new AtomicInteger(0);

    /**
     * This producer runs in a separate Java thread and passes strings to a consumer
     * thread via a shared BoundedQueue.
     */
    private static class Producer<BQ extends BoundedQueue<Integer>>
            implements Runnable {

        // Queue that is shared with the Consumer
        private final BQ mtQueue;

        Producer(BQ boundedQueue){
            mtQueue = boundedQueue;
        }

        /**
         * Runs in a separate thread tasked with receiving elements from a producer thread
         * via a shared BoundedQueue object.
         */
        @Override
        public void run() {
            for (int i = 0; i < maxIterations; ) {
                if (mtQueue.offer(i)) {
                    i++;
                    count.incrementAndGet();
                } else {
                    Thread.yield();
                }
            }
        }
    }

    /**
     * This consumer runs in a separate Java thread and receives strings from a producer
     * thread via a shared BoundedQueue.
     */
    private static class Consumer <BQ extends BoundedQueue<Integer>>
            implements Runnable {

        // Queue shared with the Producer
        private final BQ mtQueue;

        Consumer(BQ boundedQueue) {
            mtQueue = boundedQueue;
        }

        @Override
        public void run() {

            Integer integer = null;

            int nullCount = 0;

            // Get the first item from the queue
            Integer previous = null;

            // Get the first non-null value
            while((previous = mtQueue.poll()) == null) continue;

            count.decrementAndGet();

            for(int i = 1; i < maxIterations; ) {
                integer = mtQueue.poll();

                // Update state only if we get a non-null value from take() method
                if (integer != null) {
                    // Ensure ordering of the entries
                    assertEquals(previous + 1, integer.intValue());
                    previous = integer;

                    if ((i % (maxIterations/10)) == 0) System.out.println(integer);
                    count.decrementAndGet();
                    i++;

                } else {
                    nullCount++;
                    Thread.yield();
                }
            }

            assertEquals(0, count.get());

            System.out.println("Final size of the queue is "
                    + mtQueue.size()
                    + "\nmCount is "
                    + count.get()
                    + "\nFinal value is "
                    + integer
                    + "\nnumber of null returns from take() is "
                    + nullCount
                    + "\nmCount + nullCount is "
                    + (count.get() + nullCount));
        }
    }

    // Run this method to demo the buggy-ness of un-synchronized objects
    @Test(timeout = 10000)
    public void testBug() {
        final BuggyUnsynchronizedBoundedQueue<Integer> buggy =
                new BuggyUnsynchronizedBoundedQueue<>(bqSize);

        try {
            Thread[] threads = new Thread[] {
                    new Thread(new Producer<>(buggy)),
                    new Thread(new Consumer<>(buggy))
            };

            long startTime = System.nanoTime();

            for (Thread t : threads) t.start();

            for (Thread t : threads) t.join();

            System.out.println("Test ran in "
                    + (System.nanoTime() - startTime)/ 1000000
                    + " msecs");

        } catch (Exception e) {
            System.out.println("BuggyTest caught exception");
            e.printStackTrace();
        }
    }

}
