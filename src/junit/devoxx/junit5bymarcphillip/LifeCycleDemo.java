package junit.devoxx.junit5bymarcphillip;

import org.junit.jupiter.api.*;

import java.sql.SQLOutput;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LifeCycleDemo {

    @BeforeAll
    static void beforeAll(){ System.out.println("beforeAll()");
    }

    @BeforeEach
    public void beforeEach(TestInfo testInfo){
        System.out.println("beforeEach() : " + testInfo.getDisplayName() + " in " + this);
    }

    @Test
    void onePlusOne(){
        assertEquals(2, 1+1);
    }

    @Test
    void zeroPlusTwoIsTwo(){
        assertEquals(2, 0 + 2);
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        System.out.println("afterEach() : " + testInfo.getDisplayName() + " in " + this);
    }

    @AfterAll
    static void afterAll(){
        System.out.println("afterAll()");
    }
}
