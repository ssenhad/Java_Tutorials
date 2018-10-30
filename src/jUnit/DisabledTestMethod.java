package jUnit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class DisabledTestMethod {

    @Disabled
    @Test
    void testWillBeSkipped() {
    }

    @Test
    void testWillBeExecuted() {
    }

}


