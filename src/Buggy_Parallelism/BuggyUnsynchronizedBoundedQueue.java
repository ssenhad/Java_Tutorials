package Buggy_Parallelism;

import java.util.LinkedList;

class BuggyUnsynchronizedBoundedQueue<E>
        implements BoundedQueue<E> {

    private LinkedList<E> mtl = new LinkedList<>(); // queue is LinkedList of E e's

    private final int mtlCapacity; // maximum capacity of the bounded queue

    public BuggyUnsynchronizedBoundedQueue() {
        this.mtlCapacity = Integer.MAX_VALUE;
    }

    public BuggyUnsynchronizedBoundedQueue(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException();
        mtl = new LinkedList<>();
        mtlCapacity = capacity;
    }

    public int size() {
        return mtl.size();
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean isFull() {
        return size() == mtlCapacity;
    }

    public E poll() {
        if (!isEmpty()) return mtl.remove(0); // remove first element in mtl
        else return null;

    }

    public boolean offer(E e) {
        if (!isFull()) {
            mtl.add(e); // append to end of mtl
            return true;
        } else return false;
    }

    // put() and take() methods are implemented in the
    // BuggyUnsynchronizedBoundedQueueTest class
}
