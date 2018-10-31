package beginnerbasics;

public class ProgramC {

    /**
     * We can also use an anonymous inner classes for Threads.
     */

    public static void main(String args[]){
        System.out.println("Current thread info: " + Thread.currentThread());

        // Anonymous inner class
        Thread t = new Thread() {
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
        };
        t.start();
    }
}