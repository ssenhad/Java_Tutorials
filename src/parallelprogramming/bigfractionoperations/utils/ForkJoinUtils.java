package parallelprogramming.bigfractionoperations.utils;

/*
 * SUMMARY OF applyAllIter, applyAllSplit, AND applyAllSplitIndex:
 * 
 * Iterative fork()/join() is simple to program and understand but incurs more
 * work stealing overhead. Every element was put inside first worker threads deque
 * and hence the high steal count (31) and overhead. 
 * 
 * Recursive decomposition implementations (two splitter task versions) had
 * much less steals due to recursively partitioning the search space, so sub-tasks
 * were assigned to worker threads and increased runtime performace - lower worker
 * thread contention/overhead. They have significantly less number of steals. 
 * This method is faster, but more difficult to implement and understand. The
 * index version is a little bit faster than the initial recursive version.
 * 
 * This means that reducing stealing and copying makes things run faster!
 * 
 * RecursiveAction implementation is slightly faster than RecursiveTask
 * implementation; because we dont have to merge sublists together.
 * 
 */

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;
import static parallelprogramming.bigfractionoperations.utils.ExceptionUtils.rethrowFunction;

/**
 * A Java utility class that defines useful helper methods for
 * fork-join operations.
 */
public class ForkJoinUtils {
	
    private ForkJoinUtils() {
    	
    }

    /**
     * Apply {@code op} to all items in the {@code list} using
     * iterative calls to fork-join in order to run the computations in
     * parallel.
     */
    public static <T> List<T> applyAllIter(List<T> list,
                                           Function<T, T> op,
                                           ForkJoinPool forkJoinPool) {
        /* VARIANT 1
         * 
         * Straight forward and simple implementation:
         * 
         * Invoke a new task in the fork-join pool using an anonymous
         * RecursiveTask whose compute() method creates sub-tasks and
         * executes them in the fork join pool forkJoinPool.
         * 
         * invoke() the task on the fork-join pool and wait for and return
         * the results which is a List<T>.
         * 
         * invoke() enables a non-ForkJoinTask client to insert a task into
         * the shared queue where all worker threads take tasks (FIFO) and
         * push them onto their work-stealing queue.
         * 
         * Initially everything gets added inside a single worker threads 
         * work queue/deque and the other worker threads are empty. As the
         * worker thread processes tasks in LIFO order inside its deque,
         * other worker threads steal work from its tail in FIFO.
         * This is how the work gets dispersed to run in parallel over
         * different CPU cores.
         * 
         * The overhead of work-stealing is high, but copying and method calls
         * has very low overhead.
         * 
         * When the loop for join()'ing is going on, it will
         */
        return forkJoinPool.invoke(new RecursiveTask<List<T>>() {
                
        		/*
                 * Entry point into the new task. Compute implements the main
                 * fork-join task.
                 */
                protected List<T> compute() {
                    /*
                     * Create a list to hold the forked tasks, and a list
                     * to hold the results of applying the fork-join tasks.
                     */
                    List<ForkJoinTask<T>> forks = new LinkedList<>();
                    List<T> results = new LinkedList<>();

                    /*
                     * Iterate through list of unreduced BigFractions,
                     * fork all the tasks, compute the results of performing
                     * the op operation, and add the result to the forks list.
                     * 
                     * When this loop is done we will have forked off n
                     * ForkJoinTasks, where n is the number of items in the list,
                     * and each of those tasks will be queued waiting to be 
                     * processed by the threads waiting in the ForkJoinPool.
                     * This implementation relies on work-stealing to disperse
                     * the tasks to worker threads.
                     */
                    for (T t : list) {
                        forks.add(new RecursiveTask<T> () {
                                // Apply the operation.
                                protected T compute() {
                                    return op.apply(t);
                                }
                            }
                            // Fork a new task on the calling threads deque.
                            .fork());
                    }

                    /*
                     * Iterate through all the results of the forked tasks
                     * and join the results, logically blocking (waiting)
                     * for the other join results.
                     */
                    for (ForkJoinTask<T> task : forks) results.add(task.join());

                    // Return the results via comput() method.
                    return results;
                }});
    }

    /*
     * VARIANT 3
     * 
     * Demonstrates how to get around all the extra copying done when creating 
     * sub-lists, so we will reduce copy overhead if everything is stored in an
     * Array that is shared by all the various tasks. Index values can then be
     * used to iterate over the array for partitions thus reducing the copy 
     * overhead of creating new partitions. Secondly a different class will
     * be used - RecursiveAction, which doesnt return a result.
     * 
     * The invoke() method places the non-ForkJoinTask client inside the shared
     * queue. We fork() tasks recursively, according to lo and hi index values,
     * and they are dispersed to worker threads. The fork()'ed sub-task and 
     * the comput() sub-task can run in parallel. Compute() runs in the same task
     * as its parent to optimize performance - it borrows the parents' thread to 
     * do the compute(). join() return no value but it serves as a synchronization
     * point.
     */
    
    /**
     * Apply {@code op} to all items in the {@code list} by
     * recursively splitting up calls to fork-join method.
     */
    public static <T> List<T> applyAllSplitIndex(List<T> list,
                                                 Function<T, T> op,
                                                 ForkJoinPool forkJoinPool) {
        /*
         *  Create a new local array of a certain size, of a certain
         *  type, that is a generic type; to hold the results.
         */
        T[] results = (T[]) Array.newInstance(list.get(0).getClass(),
                                              list.size());

        /**
         * This task partitions list recursively and runs each half in
         * a ForkJoinTask.  It uses indices to avoid the overhead of
         * copying.
         */
        class SplitterTask 
              extends RecursiveAction {
        	
            /**
             * The lo index in this partition.
             */
            private int mLo;

            /**
             * The hi index in this partition.
             */
            private int mHi;

            /**
             * Constructor initializes the fields.
             */
            private SplitterTask(int lo, int hi) {
                mLo = lo;
                mHi = hi;
            }

            
            /**
             * Recursively perform the computations in parallel using
             * the fork-join pool. Does not return a value.
             */
            protected void compute() {
            	
                // Find the midpoint.
                int mid = (mLo + mHi) >>> 1; // divide by 2 // right shift by 1

                // If there's just a single element then apply the op operation.
                if (mLo == mid) {
                    // Update the mLo location with the results of applying op
                    results[mLo] = op.apply(list.get(mLo));
                }
                /*
                 * Create a new SplitterTask to handle the left-hand side of the
                 * list and fork it.
                 */
                else {
                    ForkJoinTask<Void> leftTask = new SplitterTask(mLo, mLo = mid)
                            .fork();

                    /*
                     * Compute the right-hand side in parallel with the forked
                     * left-hand side.
                     */
                    compute();
                    
                    /*
                     * Join with the left-hand side.  This is asynchronization
                     * point (nothing returns) to ensure proper memory visibility.
                     */
                    leftTask.join();
                }
            }
        }

        /*
         * Invoke a new SplitterTask in the fork-join pool, waiting for results
         * The splittertask splits the entire array from 0 to end.
         */
        
        forkJoinPool.invoke(new SplitterTask(0, list.size()));

        // Create a list from the array of results and return it.
        return Arrays.asList(results);
    }
    
    /* 
     * VARIANT 2
     * 
     * This next approach is the common way of using the fork-join pool.
     * The problem that we will encounter with the first method is that we
     * end up with high work-stealing overhead. This second approach can help
     * with that.
     */
    
    /**
     * Apply {@code op} to all items in the {@code list} by
     * recursively splitting up calls to fork-join methods.
     */
    public static <T> List<T> applyAllSplit(List<T> list,
                                            Function<T, T> op,
                                            ForkJoinPool forkJoinPool) {
        /**
         * This class splits up the list recursively (partitions)
         * and runs each half of the partition in a ForkJoinTask. Then each 
         * of those tasks will split their sublist into sub-sublists, etc.
         */
        class SplitterTask
              extends RecursiveTask<List<T>> {

        	/**
             * A reference to a portion of the original list.
             */
            private List<T> mList;

            /**
             * Constructor initializes the field.
             */
            private SplitterTask(List<T> list) {
                mList = list;
            }

            /*
             * Recursively perform the computations in parallel using
             * the fork-join pool. Notice the divide-and-conquer approach
             * here. This is basically "recursive decomposition" to disperse
             * the partitions and perform the task on each of the halves.
             * 
             * This implementation is harder to understand and code due to its
             * recursive nature. It reduces worker thread overhead but increases
             * copying and method call overhead.
             * 
             * Previously everything was given to a single worker thread where
             * other workers stole work from it. In this case, we are
             * partitioning, forking, and computing in different threads. So
             * very quickly we end up with a balanced tree of subtasks that
             * are more evenly partitioned accross the threads in the
             * ForkJoinPool. Thus less work stealing overhead due to threads
             * getting assigned work, and not stealing work.
             * 
             * The fork()'ed sub-tasks and the compute() sub-tasks run in
             * parallel. The fork() will be owned by a different thread of
             * control, and the compute() call will be running in the calling
             * thread which is different from the thread that will run the
             * fork(). 
             * 
             * This way of doing things is canonical - you typically borrow the
             * parent thread to compute the right hand side, meaning less forking
             * and joining going on, and there is a balanced tree of join() calls.
             */
            protected List<T> compute() {
                // The base case for the recursion.
                if (mList.size() <= 1) {
                    // Create a new list to hold the result (if any).
                    List<T> result = new ArrayList<>();

                    // Iterate through the list.
                    for (T t : mList)
                        // Apply the operation and add the result to
                        // the result list.
                        result.add(op.apply(t));

                    // Return the result list.
                    return result;
                } else {
                    // Determine the midpoint of the list.
                    int mid = mList.size() / 2;

                    // Create a new SplitterTask to handle the
                    // left-hand side of the list and fork it.
                    ForkJoinTask<List<T>> leftTask = 
                        new SplitterTask(mList.subList(0, mid))
                        .fork();
                                         
                    // Update mList to handle the right-hand side of
                    // the list.
                    mList = mList.subList(mid, mList.size());

                    // Compute the right-hand side.
                    List<T> rightResult = compute();
                    
                    // Join the left-hand side results.
                    List<T> leftResult = leftTask.join();

                    // Combine the left-hand and the right-hand side
                    // results.
                    leftResult.addAll(rightResult);
                    
                    // Return the joined results.
                    return leftResult;
                }
            }
        }

        /*
         * Invoke a new SpliterTask in the fork-join pool, and then wait,
         * and return the results. This invokation inserts a task from the
         * non-ForkJoinTask client into the the internal shared queue.
         */
        return forkJoinPool.invoke(new SplitterTask(list));
    }

    
    /**
     * Apply {@code op} to all items in the {@code list} by
     * recursively splitting up calls to fork-join methods.
     */
    public static <T> void applyAllSplitIndexEx(List<T> list,
                                                Function<T, T> op,
                                                ForkJoinPool forkJoinPool,
                                                T[] results) {
        /**
         * This task partitions list recursively and runs each half in
         * a ForkJoinTask.  It uses indices to avoid the overhead of
         * copying.
         */
        class SplitterTask
                extends RecursiveAction {
        	
            /**
             * The lo index in this partition.
             */
            private int mLo;

            /**
             * The hi index in this partition.
             */
            private int mHi;

            /**
             * Constructor initializes the fields.
             */
            private SplitterTask(int lo, int hi) {
                mLo = lo;
                mHi = hi;
            }

            /**
             * Recursively perform the computations in parallel using
             * the fork-join pool.
             */
            protected void compute() {
                // Find the midpoint.
                int mid = (mLo + mHi) >>> 1;

                // If there's just a single element then apply
                // the operation.
                if (mLo == mid) {
                    // Update the mLo location with the results of
                    // applying the operation.
                    results[mLo] = op.apply(list.get(mLo));
                } else {
                    // Create a new SplitterTask to handle the
                    // left-hand side of the list and fork it.
                    ForkJoinTask<Void> leftTask =
                            new SplitterTask(mLo, mLo = mid)
                                    .fork();

                    // Compute the right-hand side in parallel with
                    // the left-hand side.
                    compute();

                    // Join with the left-hand side.  This is a
                    // synchronization point.
                    leftTask.join();
                }
            }
        }

        // Invoke a new SplitterTask in the fork-join pool.
        forkJoinPool.invoke(new SplitterTask(0, list.size()));
    }

    

    /**
     * Apply {@code op} to all items in the {@code list} using the
     * fork-join pool invokeAll() method.
     */
    public static <T> List<T> invokeAll(List<T> list,
                                        Function<T, T> op,
                                        ForkJoinPool forkJoinPool) {
        // Create a new list of callables.
        List<Callable<T>> tasks =
            new ArrayList<>();

        // Add all the ops to the list.
        for (T t : list)
            tasks.add(() -> op.apply(t));


        // Create a list of elements from the list of futures and
        // return it.
        return forkJoinPool
            // Call invokeAll() to process all elements in the list.
            .invokeAll(tasks)

            // Convert the list of futures to a stream.
            .stream()

            // Map the futures to elements.
            .map(rethrowFunction(Future::get))

            // Collect the results into a list.
            .collect(toList());
    }
}