package junit.devoxx.junit5bymarcphillip;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * A simple demonstration of the various ways to perform exception testing with JUnit 3 and 4.
 *
 * A test is deemed successful when the exception is thrown; and it fails if a different or no
 * exception is thrown.
 */

public class ExceptionTestingJUnit3And4Demo {


    /**
     * JUnit 3 way (Verbose)
     */
    @Test
    public void oldSchoolApproach() {
        try{
            Integer.parseInt("foo");                    // if this doesnt throw an exception
            fail("NumberFormatException expected");        // fail it
        } catch (NumberFormatException expected) {         // and if it does throw an exception, catch the exception
            assertEquals("For input string: \"foo\"", expected.getMessage()); // and make an assertion
        }
    }


    /**
     * JUnit 4 way of using an optional parameter, "expected", that takes as values subclasses of Throwable.
     *
     * The exception is thrown somewhere in this methods code block but we don't know where exactly it will
     * be thrown. This way of exception testing will test if ANY code in the block throws the exception.
     */
    @Test(expected = NumberFormatException.class) // expect this kind of exception
    public void annotationParameterApproach() {
        Integer.parseInt("foo");
    }

    /**
     * The above approach is useful for simple cases but it has its limits (can't test the message value
     * belonging to the exception, or the state of a domain object after the exception has been thrown.
     * To address this you can use the try-catch idiom as in the JUnit 3 way.
     *
     * Alternatively we can use the ExpectedException @Rule to verify that our code throws a specific exception,
     * and to indicate not only what exception you are expecting, but also the message of the exception.
     *
     * Remember that @Rules are not used in JUnit 5 Jupiter.
     *
     * Note that the rule won't affect the existing tests.
     *
     * After specifying the type of the expected exception your test is successful when such an exception is
     * thrown and it fails if a different or no exception is thrown.
     *
     * You don't have to specify the exception type, you can characterize the exception by other criteria too
     * such as expectMessage, expectCause, and expect. You can combine any of the expect-methods, but the test
     * will only be successful if ALL of the specifications are met.
     */
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void ruleBasedApproach() {
        thrown.expect(NumberFormatException.class);
        thrown.expectMessage("For input string: \"foo\""); // fail message
        int fooInt = Integer.parseInt("foo"); // this causes the NumberFormatException to throw
    }

}