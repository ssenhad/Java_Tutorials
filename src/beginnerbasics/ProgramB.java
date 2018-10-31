package beginnerbasics;

public class ProgramB implements Runnable {

    /**
     * The other way to create a thread is to declare a class that implements the Runnable interface.
     * That class then implements the run method.
     * An instance of the class can then be allocated, passed as an argument when creating the
     * Thread, and started.
     */

    @Override
    public void run(){
        System.out.println("Current thread info: " + Thread.currentThread());
        for(int i=0; i!=10; i++){
            try{
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            System.out.println(i);
        }
    }

    public static void main(String args[]){
        System.out.println("Current thread info: " + Thread.currentThread()); // main methods thread
        (new Thread(new ProgramB())).start(); // invoke a new Thread Object and pass Program B, a Runnable Object, to its constructor. Then .start() the Thread.
    }
}
