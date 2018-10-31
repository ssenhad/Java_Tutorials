package threads.caveofprogramming.part2;

import java.util.Scanner;

/**
 * Tutorial for demonstrating the 'volatile' java keyword.
 */

/**
 * This class is not guaranteed to work because of the confusion
 * between the main thread and the new threads version of
 * of the 'running' variable. The variable update is not propagated
 * correctly due to bad caching.
 */
class Processor1_buggy extends Thread {

    private boolean running1 = true; // terminating condition

    @Override
    public void run() {
        while (running1) {
            System.out.println("Hello from a buggy thread.");
            try {
                Thread.sleep(100);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        System.out.println("Buggy thread shutdown.");

    }

    public void shutdown() {
        running1 = false;
    }
}

// Volatile
class Processor2_volatile extends Thread {

    private volatile boolean running2 = true; // terminating condition

    @Override
    public void run() {
        while (running2) {
            System.out.println("Hello from a volatile thread.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        System.out.println("Volatile thread shutdown.");

    }

    public void shutdown() {
        running2 = false;
    }
}


public class App {
    public static void main (String [] args){
        //Processor1_buggy p1 = new Processor1_buggy();
        //p1.start();

        Processor2_volatile p2 = new Processor2_volatile();
        p2.start();
        // Shutdown on next return key press
        System.out.println("Press enter to shutdown thread\n");
        Scanner s1 = new Scanner(System.in);
        s1.nextLine();
        p2.shutdown();
    }

}
