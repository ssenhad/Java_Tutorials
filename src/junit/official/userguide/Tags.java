package junit.official.userguide;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("fast")
@Tag("model")
public class Tags {

    @Test
    @Tag("taxes")
    void testingTaxCalculation(){
        // ...
    }
}
