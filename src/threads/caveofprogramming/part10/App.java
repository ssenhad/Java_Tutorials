package threads.caveofprogramming.part10;

import java.util.Scanner;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * This tutorial demonstrates reentrant locks by having two threads attempting to
 * increment a count variable. The expected value is 20000, but without using lock()
 * and unlock() we can induce hazards into our program.
 * We expand on the example to show that its better done with a Condition object
 * that uses await() and signal() methods.
 */

public class App {

    public static void main (String [] args) {

        System.out.println("Starting...");

        final JobRunner runner = new JobRunner();

        Thread _1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Running thread1()");
                    runner.thread1();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        Thread _2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Running thread2()");
                    runner.thread2();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        _1.start();
        _2.start();

        try {
            _1.join();
            _2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        runner.finishUp();
    }

}

class JobRunner {

    private int count = 0;

    /**
     * Once a thread has acquired this lock, this lock can be locked again.
     * It also counts the number of times the same thread has locked the lock.
     * To unlock this lock we have to unlock it the same amount of times that it
     * has been locked.
     */
    private Lock lock = new ReentrantLock();


    /**
     * Condition class has methods signal() and await() similar to wait() and notify()
     * methods belonging to the Object class.
     */
    private Condition lockCondition = lock.newCondition();


    /**
     * We could synchronize this method for multiple threads to access/update correct value
     * but we can also use re-entrant locks to achieve the same thing.
     */
    public void incrementCount () {
        for (int i=0; i<10000; i++) count++;
    }

    public void thread1 () throws InterruptedException {
        lock.lock();
        System.out.println("Thread1 waiting...");
        lockCondition.await(); // hand over control of lock
        System.out.println("Woke up thread1.");
        System.out.println("Incrementing from thread1.");
        try {
            incrementCount();
        } finally {
            lock.unlock();
        }
    }

    public void thread2 () throws InterruptedException {
        Thread.sleep(2000); // Demonstrate a waiting signal from thread1
        lock.lock();

        Scanner kb = new Scanner (System.in);
        System.out.println ("Hit the return key...");
        kb.nextLine();
        System.out.println("Return key pressed.");
        lockCondition.signal(); // wake up thread1

        System.out.println("Incrementing from thread2.");
        try {
            incrementCount();
        } finally {
            lock.unlock();
        }
    }

    public void finishUp () {
        System.out.println("Running finishUp()");
        System.out.println("Count is: " + count);

    }
}
