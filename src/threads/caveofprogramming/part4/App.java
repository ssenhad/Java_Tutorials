package threads.caveofprogramming.part4;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * This tutorial will demonstrate multiple locks and synchronized code blocks.
 */

public class App {

    private List<Integer> list1 = new ArrayList<Integer>();
    private List<Integer> list2 = new ArrayList<Integer>();

    private Object lock1 = new Object();
    private Object lock2 = new Object();

    private Random random = new Random();

    public static void main (String [] args) {
        App a = new App();
        a.main();
    }

    public void main(){

        System.out.println("Starting App...");
        long start = System.currentTimeMillis();

        Thread t1 = new Thread(new Runnable(){
            @Override
            public void run() {
                processSomethings();
            }
        });


        Thread t2 = new Thread(new Runnable(){
            @Override
            public void run() {
                processSomethings();
            }
        });

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.currentTimeMillis();
        System.out.println("Execution time... " + (end - start) + "msec");
        System.out.println("list1... " + list1.size() + "\t;\tlist2... " + list2.size());
    }

    // Make threads wait for object/intrinsic lock
    public synchronized void first_buggy() {
        try{
            Thread.sleep(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        list1.add(random.nextInt(100));
    }

    // Make threads wait for monitor object/intrinsic lock
    public synchronized void second_buggy() {
        try{
            Thread.sleep(1);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        list2.add(random.nextInt(100));
    }

    /**
     * The problem is that there is only one intrinsic lock for the worker object.
     * This results in one thread running first() method, then any other thread waiting
     * to run second() method cannot acquire the lock becuase it has to acquire the same
     * lock yet the methods are independent. We want a system where no two threads can run
     * first() and no two threads can run second() method, but one thread can run first()
     * and another thread can run second() because they are not writing to the same data so
     * we can use separate locks and synchronize them separately.
     *
     * We can create two Object variables and use synchronized code blocks instead of
     * synchronized methods!
     *
     * The buggy versions of the first() and second() methods give a time of 4000ms. We
     * expect a timing of 2000ms. Lets fix it by using synchronized code blocks.
     */

    // Two threads can now run this method at the same time but if one thread has ALREADY
    // ENTERED the code block, the other thread will have to wait until the first thread
    // releases the lock, so that the other thread can acquire the lock.
    public void first() {
        synchronized (lock1){
            try{
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            list1.add(random.nextInt(100));
        }
    }

    public void second() {
        synchronized (lock2){
            try{
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            list2.add(random.nextInt(100));
        }
    }

    public void processSomethings() {
        for(int i=0; i<1000; i++) {
            first();
            second();
        }
    }
}
