package threads.caveofprogramming.part12;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * This tutorial demonstrates the use of semaphore objects in Java.
 *
 * Semaphores are Java objects that maintain a count. Semaphores are usually used to control access to some resource,
 * and they can be used with thread objects in contrast to intrinsic locks. We can assign an intial number of permits to a
 * semaphore, and we can increment and decrement the number of permits by releasing or acquiring a permit respectively.
 *
 * release(): Releases a permit, increasing the number of available permits by one. If any threads are trying to acquire
 * a permit, then one is selected and given the permit that was just released.
 *
 * acquire(): Acquires a permit, if one is available and returns immediately, reducing the number of available permits by
 * one. It will block until one is available, or the thread is interrupted.
 *
 * These two methods are similar performing to the lock() and unlock() methods. A semaphore with 1 permit is a lock, but
 * the only difference is that you can release from different threads to where you did the acquire; instead of having to
 * unlock() intrinsic locks from the same thread you lock()'ed.
 *
 */

public class App {

    // First try
    public void demo (int i) {
        System.out.println("Allocating " + i + " permits");
        Semaphore s = new Semaphore(i); // count = s.availablePermits()
        System.out.println("Available permits: " + s.availablePermits());
        s.release(); // increment permits
        try {
            s.acquire(); // decrement permits
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Driver
    public static void main (String [] args) {

        Connection.getConnectionInstance().connect();

        /**
         * Creates a thread pool that creates new threads as needed, but will reuse previously constructed threads when
         * they are available. Threads are terminated and removed from cache when they have not been used for more than
         * 60 seconds.
         */
        ExecutorService executioner = Executors.newCachedThreadPool();

        for (int i=0; i<10; i++) {
            executioner.submit(new Runnable() {
                @Override
                public void run() {
                    Connection.getConnectionInstance().connect();
                }
            });
        }
        executioner.shutdown();
    }
}


// Singleton class for only one Connection object at a time
class Connection {

    private static Semaphore sem = new Semaphore(10, true);
    private static Connection conn = new Connection();
    private int conn_count = 0; // number of connections

    // Constructor
    private Connection () {

    }

    public static Connection getConnectionInstance () {
        return conn;
    }

    public void connect () {
        try {
            sem.acquire(); // acquire a permit before you can open a connection
            System.out.println("Acquired permit..." + sem.toString());
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        try{
            doConnect();
        } finally {
            sem.release();
            System.out.println("Released permit..." + sem.toString());
        }
    }

    public void doConnect (){

        // increment number of connections
        synchronized (this) {
            conn_count++;
            System.out.println("Current number of connections: " + conn_count);
        }

        try{
            Thread.sleep(2000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }

        // decrement number of connections
        synchronized (this) {
            conn_count--;
        }

        System.out.println("Done connecting..." + sem.toString());
    }

}
