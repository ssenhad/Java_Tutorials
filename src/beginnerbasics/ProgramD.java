package beginnerbasics;

public class ProgramD {

    /**
     * Another way of using threads in Java is to make use of Java 1.8s (SDK 8) ability called lambda expressions.
     * A lambda expression is an unnamed block of code, with optional parameters, that can be stored, passed around,
     * and executed later.
     *
     * Lambda expressions are awesome because we dont have to write out all the code to do the same thing, see example 1
     * and its comparison.
     *
     * You can store lambda expressions in a variable and pass that variable to methods when ever
     * necessary as shown in example 2.
     */

    public static void main(String args[]){
        System.out.println("Current thread info: " + Thread.currentThread()); // main method thread

        // example 1: Nice and compact
        new Thread(() -> System.out.println("Hello from thread A")).start();


        // Compared to anonymous inner class version of ProgramC: MORE CODE
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Hello from anonymous inner class");
            }
        }).start();


        // example 2: passing variable
        Runnable r = () -> System.out.println("Hello from thread B "); // newly created thread
        new Thread(r).start();


    }
}