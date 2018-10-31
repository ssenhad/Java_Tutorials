package threads.caveofprogramming.part8;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Scanner;

public class App {

    public static void main (String [] args) {

        final JobRunner JR = new JobRunner();

        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JR.produce();
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }
        });

        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JR.consume();
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
    }
}

class JobRunner {


    // Produce (put()) thread
    public void produce()
        throws InterruptedException {
        synchronized (this) {
            System.out.println("Thread running produce()..." +
                    "\nproduce() thread is wait()'ing.");
            wait(); // Causes the current thread to wait until another thread invokes the
                    // notify() method or the notifyAll() method for this object.

            /**
             * Every java Object has a wait() method. It can cause threads to wait indefinitely
             * so be careful - or specify the time in its secondary constructor wait(time).
             * wait() will make threads wait so that system resources are used efficiently.
             * wait() takes over the control of the lock, and the synchronized code block will
             * lose the lock. It must be possible then , for the thread to regain control of the
             * lock. We use the method notify().
             */

            System.out.println("Resuming produce() thread.");

        }
    }

    // Consume (take()) thread
    public void consume()
        throws InterruptedException {
        System.out.println("Starting consume() thread.");
        Scanner kb = new Scanner(System.in);
        Thread.sleep(3000);

        synchronized (this) {
            System.out.println("Waiting for return key to be pressed.");
            kb.nextLine();
            System.out.println("Return key pressed.");
            notify(); // Notify the produce thread (that is wait()'ing) so it can reacquire the lock
            Thread.sleep(5000);   // after this 5 seconds the code block relinquishes
                                        // its lock to the produce() code block
        }
        kb.close();
    }
}