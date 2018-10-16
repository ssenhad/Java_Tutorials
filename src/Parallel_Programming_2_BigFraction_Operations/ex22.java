package Parallel_Programming_2_BigFraction_Operations;

// utility class imports
import Parallel_Programming_2_BigFraction_Operations.utils.BigFraction;
import Parallel_Programming_2_BigFraction_Operations.utils.RunTimer;

import static Parallel_Programming_2_BigFraction_Operations.utils.ForkJoinUtils.*;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Function;
import java.util.stream.Stream;
import static java.util.stream.Collectors.toList;



/**
 * This example demonstrates various operations performed on a BigFraction
 * class, such as multiply and reduce, using 5 different implementations
 * of the Fork-Join Pool Framework. We will assess the performance advantages and
 * disadvantages of the different implementations runtime speed.The Function<T, T>
 * interface, lambda expressions, and Java 8 Streams are also used throughout this
 * program.
 * 
 * Some of the 5 implementations run faster than others, and we will attempt to
 * asses why.
 * 
 * This code has been adapted from douglas Douglas Schmidt Live Lessons ex22.
 */
public class ex22 {
	
    /**
     * Number of big fractions to process via a fork-join pool.
     */
    private static int sMAX_FRACTIONS = 30;

    /**
     * A big reduced fraction constant.
     */
    private static BigFraction sBigReducedFraction = 
        BigFraction.valueOf(new BigInteger("846122553600669882"),
                            new BigInteger("188027234133482196"),
                            true);

    /**
     * Stores a completed future with a BigFraction value of sBigReducedFraction.
     */
    private static CompletableFuture<BigFraction> mBigReducedFractionFuture =
        CompletableFuture.completedFuture(sBigReducedFraction);

    /**
     * Main entry point into the test program.
     */
    public static void main (String[] args) throws IOException {
    	
        display("Starting ForkJoinTest\n");

        /*
         *  Generate a list of random unreduced BigFractions and Limit the size of the list
         *  to sMAX_FRACTIONS. Return the result as List<BigFractions>.
         */
        List<BigFraction> fractionList = Stream.generate(() -> makeBigFraction(new Random(), false))
            .limit(sMAX_FRACTIONS)
            .collect(toList());

        /*
         *  Define a BigFraction operation where we reduce an unreduced BigFraction,
         *  multiply it,
         *  return a result of multiplying the reduced BigFraction some constant.
         *  
         *  This operation can take a long time to run when using BigFractions, as reduce
         *  and multiply are expensive operations.
         */
        Function<BigFraction, BigFraction> op =	bigFraction -> BigFraction
        		.reduce(bigFraction)
        		.multiply(sBigReducedFraction);

        // Run all the different fork-join tests
        ForkJoinPool fjp1 = new ForkJoinPool();
        RunTimer.timeRun(() -> testApplyAllIter(fractionList, op, fjp1), "testApplyAllIter()");
        System.out.println("applyAllIter() steal count = " + fjp1.getStealCount());

        System.gc();
        ForkJoinPool fjp2 = new ForkJoinPool();
        RunTimer.timeRun(() -> testApplyAllSplitIndex(fractionList, op, fjp2), "testApplyAllSplitIndex()");
        System.out.println("applyAllSplitIndex() steal count = " + fjp2.getStealCount());
        
        System.gc();
        ForkJoinPool fjp3 = new ForkJoinPool();
        RunTimer.timeRun(() -> testApplyAllSplit(fractionList, op, fjp3), "testApplyAllSplit()");
        System.out.println("applyAllSplit() steal count = " + fjp3.getStealCount());

        System.gc();
        ForkJoinPool fjp4 = new ForkJoinPool();
        RunTimer.timeRun(() -> testInvokeAll(fractionList, op, fjp4), "testInvokeAll()");
        System.out.println("invokeAll() steal count = " + fjp4.getStealCount());

        System.gc();
        ForkJoinPool fjp5 = new ForkJoinPool();
        RunTimer.timeRun(() -> testApplyAllSplitIndexEx(fractionList, op, fjp5), "testApplyAllSplitIndexEx()");
        System.out.println("applyAllSplitIndexEx() steal count = " + fjp5.getStealCount());

        // Print the results of the tests.
        display(RunTimer.getTimingResults());

        display("\nFinishing ForkJoinTest");
    }

    /**
     * A factory method that returns a large random BigFraction whose
     * creation is performed synchronously.
     *
     * @param random A random number generator
     * @param reduced A flag indicating whether to reduce the fraction or not
     * @return A large random BigFraction
     */
    private static BigFraction makeBigFraction(Random random, boolean reduced) {
        // Create a large random big integer.
        BigInteger numerator = new BigInteger(150000, random);

        // Create a denominator that's between 1 to 10 times smaller
        // than the numerator.
        BigInteger denominator = numerator.divide(BigInteger.valueOf(random.nextInt(10) + 1));

        // Return a big fraction.
        return BigFraction.valueOf(numerator, denominator, reduced);
    }

    /*
     * Each of these test methods will get their own ForkJoinPool that
     * runs on the number of cores that the JVM knows about.
     */
    
    /**
     * Test the applyAllIter() utility method.
     */
    private static void testApplyAllIter(List<BigFraction> fractionList,
                                         Function<BigFraction, BigFraction> op,
                                         ForkJoinPool fjp) {
        // Test big fraction operations using applyAllIter().
        applyAllIter(fractionList, op, fjp);
    }

    /**
     * Test the applyAllSplitIndex() utility method.
     */
    private static void testApplyAllSplitIndex(List<BigFraction> fractionList,
                                               Function<BigFraction, BigFraction> op,
                                               ForkJoinPool fjp) {
        // Test big fraction operations using applyAllSplitIndex().
        applyAllSplitIndex(fractionList, op, fjp);
    }

    /**
     * Test the applyAllSplitIndex() utility method.
     */
    private static void testApplyAllSplitIndexEx(List<BigFraction> fractionList,
                                                 Function<BigFraction, BigFraction> op,
                                                 ForkJoinPool fjp) {
        BigFraction[] results = new BigFraction[fractionList.size()];
        // Test big fraction operations using applyAllSplitIndex().
        applyAllSplitIndexEx(fractionList, op, fjp, results);
    }

    /**
     * Test the applyAllSplit() utility method.
     */
    private static void testApplyAllSplit(List<BigFraction> fractionList,
                                          Function<BigFraction, BigFraction> op,
                                          ForkJoinPool fjp) {
        // Test big fraction operations using applyAllSplit().
        applyAllSplit(fractionList,
                      op,
                      fjp);
    }

    /**
     * Test the invokeAll() utility method.
     */
    private static void testInvokeAll(List<BigFraction> fractionList,
                                      Function<BigFraction, BigFraction> op,
                                      ForkJoinPool fjp) {
        // Test big fraction operations using invokeAll()
        invokeAll(fractionList,
                  op,
                  fjp);
    }

    /**
     * Display the {@code string} after prepending the thread id.
     */
    private static void display(String string) {
        System.out.println("["
                           + Thread.currentThread().getId()
                           + "] "
                           + string);
    }
}