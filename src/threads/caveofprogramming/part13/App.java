package threads.caveofprogramming.part13;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

/**
 * This tutorial will demonstrate the Callable and Future classes.
 *
 * These two classes enable you to get return results from threads, and they allow your
 * threads to throw exceptions.
 */

public class App {

    // initial discussion
    private void firstDemo () {
        /**
         * Creates a thread pool that creates new threads as needed, but will reuse previously
         * constructed threads when they are available. These pools will typically improve the
         * performance of programs that execute many short-lived asynchronous tasks.
         */
        ExecutorService executioner = Executors.newCachedThreadPool();
        executioner.submit(new Runnable() {

            /**
             * How can we get a returned result from some computation performed in this
             * run() method?
             *
             * We can use some separate runnable implementing class and the
             * code code could store a result, where we store and access it with an instance
             * variable.
             *
             * The alternative is to use the Future class.
             */
            @Override
            public void run() {
                Random randommer = new Random();
                int sleepDuration = randommer.nextInt(5000);

                System.out.println("Sleeping run()...");

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Finished run()...");
            }
        });
        executioner.shutdown();
    }

    // Driver and main demo
    public static void main (String [] args) {
        
        ExecutorService executioner = Executors.newCachedThreadPool();
        Future<Integer> futureObj = executioner.submit(new Callable<Integer>() {

            /**
             * Override Callable's call() method to return a value, similar to Runnable
             * objects run() method, except we can now return a value easily by specifying
             * the type of the parameterized callable object.
             *
             * The call() method returns a Future object that should be parameterized
             * according to the Callable's parameterized type.
             *
             * If we only want to use some of the methods in Future<V> and we dont want to
             * return a result then specify the parameterized type as a <?> wildcard for
             * the Future<V> object and a type of <Void> for the Callable<V> object.
             * The call() method should subsequently return a Void type as well and it
             * should return a null value.
             *
             */
            @Override
            public Integer call() throws Exception {
                Random randommer = new Random();
                int sleepDuration = randommer.nextInt(5000); // up to 5000 msec

                /**
                 * Callable can throw exceptions, but the Future will throw an
                 * ExecutionException (notice that we catch it later when calling
                 * future.get()).
                 */
                if (sleepDuration > 4000) throw new IOException("Duration too long: "
                        + sleepDuration);

                System.out.println("Starting run()...");

                try {
                    System.out.println("sleeping...");
                    Thread.sleep(sleepDuration);
                    System.out.println("...awakening");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println("Finished run()...");

                return sleepDuration;
            }
        });
        executioner.shutdown();

        /**
         * Get the returned result. Note that .get() blocks until the thread associated
         * with this particular future has terminated.
         */
        try {
            Integer randomedSleepDuration = futureObj.get();
            System.out.println("Durations was: " + randomedSleepDuration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            //e.printStackTrace();

            // To get the exception thrown in callable:
            IOException myDurationException = (IOException) e.getCause();
            System.out.println("Encountered IOException: "
                    + myDurationException.getMessage());
        }


    }

}
