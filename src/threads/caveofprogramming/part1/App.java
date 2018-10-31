package threads.caveofprogramming.part1;

/**
 * Tutorial for how to instantiate and run a thread.
 */

/**
 * "...a Runnable object, is more general, because the Runnable object can subclass
 * a class other than Thread. The second (Runner1) idiom is easier to use in simple
 * applications, but is limited by the fact that your task class must be a descendant
 * of Thread." - Oracle concurrency tutorial (2018)
 */


// Driver
public class App {

    public static void main (String [] args) {
        new Thread(new Runner1()).start(); // (1)
        new Thread(new Runner2()).start(); // (2)

        // 3) Thread using an anonymous inner class
        new Thread(new Runnable(){
            @Override
            public void run(){
                for(int i=0; i<15; i++){
                    System.out.println("[Runner3] " + i);
                    try{
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
                System.out.println("[Runner3] Done.");
            }
        }).start();
    }

}

// 1) We can subclass the Thread class
class Runner1 extends Thread {

    @Override
    public void run() {
        for(int i=0; i<10; i++){
            System.out.println("[Runner1] " + i);
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        System.out.println("[Runner1] Done.");
    }
}

// 2) Or we can implement the Runnable interface
class Runner2 implements Runnable {

    @Override
    public void run(){
        for(int i=0; i<5; i++){
            System.out.println("[Runner2] " + i);
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
        System.out.println("[Runner2] Done.");
    }
}
