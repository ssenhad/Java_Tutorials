package threads.caveofprogramming.part7;

import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This tutorial demonstrates the producer-consumer multi-threading paradigm.
 */

public class App {

    /**
     * Classes in the concurrent package are thread-safe... no synchronization needed.
     */
    private static BlockingQueue<Integer> BQ = new ArrayBlockingQueue<Integer>( 5); // put and take threads in LIFO order

    // put() into blocking queue
    private static void produce ()
        throws InterruptedException {

        // Produce threads that store a random int
        Random randomer = new Random();
        while (true) {
            int i = randomer.nextInt(100);
            try {
                BQ.put(i);
                System.out.println("Inserted: " + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // take() from blocking queue every 100 milli seconds
    private static void consume ()
        throws InterruptedException {

        Random randomer = new Random();

        while (true) {
            Thread.sleep(200);
            int j = randomer.nextInt(100);
            if (j<=10) {
                Integer consumedVal = (Integer) BQ.take(); // Retrieve head
                System.out.println("Consumed value: " + consumedVal
                        + " List size: " + BQ.size()
                        + " Consume condition: " + j);
            } else {
                System.out.println("Failed consume condition: (" + j +
                        ")\nQueue: " + BQ.toString());
            }


        }
    }

    // Driver
    public static void main (String [] args) {

        Thread a = new Thread (new Runnable() {
            public void run () {
                try{
                    produce();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        Thread b = new Thread (new Runnable(){
            public void run () {
                try{
                    consume();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        a.start();
        b.start();
        try {
            a.join();
            b.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("\nFinished.");
    }
}
