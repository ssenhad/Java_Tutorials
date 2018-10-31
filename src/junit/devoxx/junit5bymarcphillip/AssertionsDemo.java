package junit.devoxx.junit5bymarcphillip;

import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.junit.rules.ErrorCollector;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

public class AssertionsDemo {

    @Test
    public void basicAssertions(){
        assertEquals(2, 1 + 1);
        assertEquals(2, 1 +  1, () -> "1 + 1 = 2"); // also supports suppliers () ->
        assertNull(null);
        assertNotNull(this);
        assertSame("foo", "foo");
        assertArrayEquals(new int[] {1, 2, 3}, new int[] {1, 2, 3});
    }

    @Test
    public void hamcrestAssertions() {
        assertThat("some text", allOf(notNullValue(), containsString("x")));
    }

    /* rules are gone
     * @Rule
     * public ErrorCollector errorCollector = new ErrorCollector();
     */
    @Test
    public void multipleFailures() {
        assertAll(
                () -> assertThat("foo", is(notNullValue())),
                () -> assertThat("foo", is(sameInstance("foo")))
        );
        // remove
        //errorCollector.checkThat("foo", ));
        //errorCollector.checkThat("foo", is(sameInstance("bar")));
    }
}
