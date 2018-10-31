# 1. Annotations
JUnit Jupiter supports annotations for configuring tests and extended the framework. All core annotations are located in the org.junit.jupiter.api package in the junit-jupiter-api module.

# 2. Test classes and methods
A *test method* is any instance method that is directly or meta-annotated with @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, or @TestTemplate. A *test class* is any top level or static member class that contains at least one test method.

# 3. Custom display names for test runners and reporting
Test classes and test methods can declare custom display names — with spaces, special characters, and even emojis — that will be displayed by test runners and test reporting. Use *@DisplayName("...")*

# 4. Assertions
Assertions are  *statically imported methods* that allow us to test for a known input, that we get a known output. The test will either pass or fail. JUnit Jupiter comes with many of the assertion methods that JUnit 4 has and adds a few that lend themselves well to being used with Java 8 lambdas. All JUnit Jupiter assertions are static methods in the org.junit.jupiter.api.Assertions class.
