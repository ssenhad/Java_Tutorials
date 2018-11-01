package junit.devoxx.junit5bymarcphillip;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 *  Simple class to demonstrate JUnit 5 Jupiter approach to exception testing that leverages Java 8.
 */

public class ExceptionTestingJUnit5Demo {


    @Test
    void newApproach() {
        // state the type of exception, and then provide the block of code that throws the exception
        NumberFormatException expected = assertThrows(NumberFormatException.class, () -> {
            Integer.parseInt("foo"); // this will fail and throw a NumberFormatException
        });
        assertEquals("For input string: \"foo\"", expected.getMessage()); // we can also assert messages
      }
      // parsing "42" fails becuase no exception was thrown
      // parsing "bar" fails becuase the message is not what is expected
      // parsing "foo" passes becuase the exception is thrown and the message is as expected

}