package threads.caveofprogramming.part5;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This tutorial demonstrates the use of the ExecutorService and a fixed thread pool.
 */

// Driver
public class App {

    public static void main (String [] args) {
        int numThreadsInPool = 2;
        System.out.println("Number of worker threads... " + numThreadsInPool + "\n");
        System.out.println("Running jobs now...\n");
        ExecutorService executor = Executors.newFixedThreadPool(numThreadsInPool);
        for (int i=0; i<5; i++) {
            executor.submit(new JobRunner(i));
        }
        executor.shutdown();
    }
}

// Do something with a Runnable object
class JobRunner
        implements Runnable {

    private int i = 0;

    public JobRunner (int jobID) {
        i = jobID;
    }

    /**
     * The executor service will only allocate two worker threads to the thread pool.
     * Thus, only two threads will invoke the run method at any given time - meaning
     * that only two jobRunners can be running at a time. Once a thread is done
     * executing the run() method, it can proceed to work on another job. By recycling
     * the threads in the thread pool we avoid some overhead by not having to allocate
     * the 'light-weight' thread resources.
     */
    @Override
    public void run () {
        System.out.println("Starting job... " + i);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        System.out.println("Finished job... " + i);
    }
}
