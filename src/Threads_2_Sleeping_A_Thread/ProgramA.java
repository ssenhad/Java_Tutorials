package Threads_2_Sleeping_A_Thread;

/**
 * Causes the currently executing thread to sleep (temporarily cease
 * execution) for the specified number of milliseconds, subject to
 * the precision and accuracy of system timers and schedulers. The thread
 * does not lose ownership of any monitors.
 *
 * @param  millis
 *         the length of time to sleep in milliseconds
 *
 * @throws  IllegalArgumentException
 *          if the value of {@code millis} is negative
 *
 * @throws  InterruptedException
 *          if any thread has interrupted the current thread. The
 *          <i>interrupted status</i> of the current thread is
 *          cleared when this exception is thrown.
 */

public class ProgramA {

    public static void main(String args[]){

        String[] wordsData = {"Hello", "a", "Pie", "My name Geoff", "Yoda sats: Thread pro, I am."};

        new Thread(() -> {
           for(String s : wordsData) {
               try{
                   Thread.sleep(1000);
               } catch (InterruptedException ie){
                   ie.printStackTrace();
               }
               System.out.println(s);
           }
        }).start();

    }

}
