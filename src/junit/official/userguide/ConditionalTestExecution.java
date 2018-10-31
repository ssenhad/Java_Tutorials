package junit.official.userguide;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.condition.JRE.JAVA_10;
import static org.junit.jupiter.api.condition.JRE.JAVA_8;
import static org.junit.jupiter.api.condition.JRE.JAVA_9;
import static org.junit.jupiter.api.condition.OS.LINUX;
import static org.junit.jupiter.api.condition.OS.MAC;
import static org.junit.jupiter.api.condition.OS.WINDOWS;


/**
 * Each of the conditional annotations listed in the following sections can only be
 * declared once on a given test interface, test class, or test method. Only the first
 * such annotation discovered by JUnit will be used; any additional declarations will
 * be silently ignored.
 */

public class ConditionalTestExecution {
    // ...
}

/**
 * Annotations for OS conditions.
 */

class OperatingSystemConditions {

    /**
     * Simple conditional test execution method using annotations.
     */
    @Test
    @EnabledOnOs(MAC)
    void onlyOnMacOS(){
        // ...
    }

    /**
     * Custom composed conditional operating system annotation:
     *
     * We can type a single annotation name now instead of having to do it like
     * we did above with onlyOnMacOs method.
     *
     * We are required to use ab @interface TestOnMac where we specificy the
     * annotations there, instead of here.
     *
     * The interface can also specify the @ExtendWith("SomeExeternalExtension".class)
     * annotation.
     *
     * In both cases you must use the interfaces name as the annotation for the test
     * method.
     *
     * Note that by specifying a custom composed annotation we are creating a single,
     * reusable annotation that can be applied to any test method.
     */
    @CustomComposedAnnotationForTestOnMacOs
    void testOnMac(){
        // ...
    }

    @Test
    @EnabledOnOs({LINUX, MAC})
    void onLinuxOrMac(){
        // ...
    }

    @Test
    @DisabledOnOs(WINDOWS)
    void notOnWindows(){
        // ...
    }

    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @Test
    @EnabledOnOs(MAC)
    @interface CustomComposedAnnotationForTestOnMacOs {
    }

}

/**
 * Annotations for JRE conditions.
 */
class JavaRuntimeEnvironmentConditions {

    @Test
    @EnabledOnJre(JAVA_8)
    void onlyOnJava8(){
        // ...
    }

    @Test
    @EnabledOnJre({ JAVA_9, JAVA_10 })
    void onJava9Or10(){
        // ...
    }

    @Test
    @DisabledOnJre(JAVA_9)
    void notOnJava9(){
        // ...
    }

}

/**
 * Annotations for system property conditions.
 */
class SystemPropertyConditions {

    @Test
    @EnabledIfSystemProperty(named = "os.arch", matches = ".*64.*")
    void onlyOn64BitArchitectures() {
        // ...
    }

    @Test
    @DisabledIfSystemProperty(named = "ci-server", matches = "true")
    void notOnCiServer() {
        // ...
    }
}

/**
 * Annotations for environment variable conditions.
 */
class EnvironmentVariableConditions {

    @Test
    @EnabledIfEnvironmentVariable(named = "ENV", matches = "staging-server")
    void onlyOnStagingServer() {
        // ...
    }

    @Test
    @DisabledIfEnvironmentVariable(named = "ENV", matches = ".*development.*")
    void notOnDeveloperWorkstation() {
        // ...
    }

}

/**
 * Annotations for script-based conditions.
 */
class ScriptbasedConditions {

    @Test // Static JavaScript expression.
    @EnabledIf("2 * 3 == 6")
    void willBeExecuted() {
        // ...
    }

    @RepeatedTest(10) // Dynamic JavaScript expression.
    @DisabledIf("Math.random() < 0.314159")
    void mightNotBeExecuted() {
        // ...
    }

    @Test // Regular expression testing bound system property.
    @DisabledIf("/32/.test(systemProperty.get('os.arch'))")
    void disabledOn32BitArchitectures() {
        assertFalse(System.getProperty("os.arch").contains("32"));
    }

    @Test
    @EnabledIf("'CI' == systemEnvironment.get('ENV')")
    void onlyOnCiServer() {
        assertTrue("CI".equals(System.getenv("ENV")));
    }

    @Test // Multi-line script, custom engine name and custom reason.
    @EnabledIf(
            value = {
                "load('nashorn:mozilla_compat.js')",
                "importPackage(java.time)",
                "",
                "var today = LocalDate.now()",
                "var tomorrow = today.plusDays(1)",
                "tomorrow.isAfter(today)"
            },
            engine = "nashorn",
            reason = "Self-fulfilling: {result}")
    void theDayAfterTomorrow() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        assertTrue(tomorrow.isAfter(today));
    }

}