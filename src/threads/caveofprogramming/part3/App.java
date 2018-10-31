package threads.caveofprogramming.part3;

import java.util.concurrent.TimeUnit;

/**
 * This program demonstrates the use of the Java 'synchronized' keyword.
 * Part two demonstrates caching bugs, but the 'volatile' keyword doesnt address
 * the interleaving nature of these two threads. We can use the AtomicInteger class,
 * or the 'synchronized' keyword to solve this interleaving issue.
 */

public class App {

    private int count = 0;

    // Only one thread can acquire the intrinsic lock at a time
    public synchronized void incrementCount(){
        count++;
    }

    public static void main (String [] args) {
        App a1 = new App();
        a1.doSomething();        
    }

    private void doSomething() {
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<10000; i++){
                    //count++;
                    incrementCount();
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i=0; i<10000; i++){
                    //count++;
                    incrementCount();
                }
            }
        });

        t1.start();
        t2.start();

        // Without waiting (.join()) before both threads finish executing their loops,
        // we get count = 0, because the loops don't even start incrementing - the method
        // just moves to the print statement.

        // Sometimes this will give the correct answer, and sometimes it won't. Try to
        // run the program multiple times and you will see random incorrect answers.
        // The reason for this is because count is not atomic/synchronized therefore
        // each thread might not update and access the correct value after incrementing
        // the count variable.
        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Count is " + count); // count must = 20000 for correct answer
    }
}



