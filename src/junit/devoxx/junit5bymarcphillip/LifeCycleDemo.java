package junit.devoxx.junit5bymarcphillip;

import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LifeCycleDemo {

    /**
     * Executed once before the entire test class (static, not an instance).
     */
    @BeforeAll
    static void beforeAll(){ System.out.println("beforeAll()");
    }

    /**
     * Executed before each test.
     */
    @BeforeEach
    void beforeEach(TestInfo testInfo){
        System.out.println("beforeEach() : " + testInfo.getDisplayName() + " in " + this);
    }

    // Every test gets a fresh instance ID in hashcode to avoid state problems in the tests.

    /**
     * A test.
     */
    @Test
    void onePlusOne(){
        assertEquals(2, 1+1);
    }

    /**
     * Another test.
     */
    @Test
    void zeroPlusTwoIsTwo(){
        assertEquals(2, 0 + 2);
    }

    /**
     * Executed after each test.
     * @param testInfo
     */
    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("afterEach() : " + testInfo.getDisplayName() + " in " + this);
    }

    /**
     * Executed after all tests completed.
     */
    @AfterAll
    static void afterAll(){
        System.out.println("afterAll()");
    }
}
