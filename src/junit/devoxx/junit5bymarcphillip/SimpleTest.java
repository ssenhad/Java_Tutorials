package junit.devoxx.junit5bymarcphillip;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SimpleTest {

    @Test
    void myFirstTest(){
        assertEquals(2, 1 + 1);
    }

    @Test
    @Disabled("Disabled mySecondTest() test method because we know it doesn't work")
    void mySecondTest(){
        assertEquals(0, 1 + 1);
    }

}
