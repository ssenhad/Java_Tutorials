package Buggy_Parallelism;

public interface BoundedQueue<E> {

    /**
     * Utility method to obtain the number of elements inside the bounded queue.
     * @return the number of elements in the bounded queue
     */
    int size();

    /**
     * Utility method to check if the bounded queue has any elements inside it.
     * @return true if no elements, false if one or more elements are present
     */
    boolean isEmpty();

    /**
     * Utility method to check if the bounded queue has space for another element to be
     * inserted inside it.
     * @return return true if no space, false if one or more spaces are available
     */
    boolean isFull();

    /**
     * Inserts the element e of type E into the bounded queue. This method will wait
     * until space becomes available if the bounded queue has reached its capacity.
     * @param e the generic element to be inserted of type E
     * @throws InterruptedException if interrupted while waiting
     */
    default void put(E e)
            throws InterruptedException {
        System.out.println("Default put");
    }

    /**
     * Removes and retrieves an element e of type E from the head of the bounded queue.
     * This method will wait if the bounded queue is empty until an element becomes
     * available.
     * @return the element removed from the head of the bounded queue
     * @throws InterruptedException if interrupted while waiting
     */
    default E take()
            throws InterruptedException {
        System.out.println("Default take");
        return null;
    }

    /**
     * Removes and retrieves an element e of type E from the head of the bounded queue.
     * If no elements are present in the bounded queue (empty) then it returns null.
     * @return the element at the head of the bounded queue, or null if it's empty
     */
    default E poll(){
        System.out.println("Default poll");
        return null;
    }

    /**
     * Attempts to insert the element e of type E into the bounded queue so long as
     * the capacity restriction of this bounded queue is not violated. This method
     * returns a boolean value upon success of insertion.
     * @param e the generic element of type E to be inserted into the bounded queue
     * @return true if the element was inserted, false if the queue is full
     */
    default boolean offer(E e) {
        System.out.println("Default offer");
        return false;
    }
}
