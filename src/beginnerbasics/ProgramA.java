package beginnerbasics;

public class ProgramA extends Thread {

    /**
     *  Note that the Thread class implements the Runnable functional interface and it extends the Object class.
     *  The Thread class also provides a number of useful methods, such as .start(), .join(), .sleep(), amongst others.
     */

    /**
     * There are two ways to create a new thread of execution. One is to
     * declare a class to be a subclass of Thread.
     * This subclass should override the run method of class Thread.
     * An instance of the subclass can then be allocated and started.
     */

    @Override
    public void run(){
        System.out.println("Current thread info: " + Thread.currentThread());
        for(int i=0; i!=10; i++){
            try{
                Thread.sleep(1000); // sleep for 1000 millis = 1 second
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            System.out.println(i);
        }
    }

    public static void main(String args[]){
        System.out.println("Current thread info: " + Thread.currentThread()); // main methods thread
        (new ProgramA()).start(); // create an instance of ProgramA and start executing it in a separate thread of execution.

        /**
         * Because the Thread class implements Runnable we do not have to provide a Runnable object to the constructor of Thread.
         * This is not the case in ProgramB.
         * Also, if you pass a null object to the Thread classes constructor, you will get a NullPointerException.
         */
    }
}
