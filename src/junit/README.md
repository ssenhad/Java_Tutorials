# 1. Annotations
JUnit Jupiter supports annotations for **configuring tests** and **extending the framework**. All core annotations are located in the org.junit.jupiter.api package in the junit-jupiter-api module. Find a summary of annotations in the JUnit 5 user guide, page 4.

# 2. Test classes and methods
A **test method** is any instance method that is directly or meta-annotated with @Test, @RepeatedTest, @ParameterizedTest, @TestFactory, or @TestTemplate. A **test class** is any top level or static member class that contains at least one test method. See StandardTests.java.

# 3. Custom display names for test runners and reporting
Test classes and test methods can declare custom display names — with spaces, special characters, and even emojis — that will be displayed by test runners and test reporting. Use **@DisplayName("...")**. See DisplayNameDemo.java.

# 4. Assertions
Assertions are  **statically imported methods** that allow us to test for a known input, that we get a known output. The test will either pass or fail. JUnit Jupiter comes with many of the assertion methods that JUnit 4 has and adds a few that lend themselves well to being used with Java 8 lambdas. All JUnit Jupiter assertions are **static methods** in the org.junit.jupiter.api.Assertions class. See AssertionsDemo.java.

# 5. Assumptions
JUnit Jupiter comes with a subset of the **assumption methods** that JUnit 4 provides and adds a few that lend themselves well to being used with Java 8 lambdas. All JUnit Jupiter assumptions are **static methods** in the org.junit.jupiter.api.Assumptions class. See AssumptionsDemo.java.

# 6. Disabling tests
*Test classes* and *test methods* can be **disabled** by using the @Disabled annotation, via one of the annotations discussed in the Conditional Test Execution section, or via a custom ExecutionCondition. Examples for disabling both a test class and method are found in DisabledTestClass.java and DisabledTestMethod.java classes respectively.

# 7. Conditional test execution
We can either *enable* or *disable* a container or test based on certain conditions *programmatically* by using Jupiters ExecutionCondition extenstion API. The simplest example of such a condition is the built-in DisabledCondition which supports the @Disabled annotation (see Disabling tests). In addition to @Disabled, JUnit Jupiter also supports several other annotation-based conditions in the org.junit.jupiter.api.condition package that allow developers to enable or disable containers and tests declaratively.

Note that custom conditional annotations can also be used as a meta-annotation in order to create a custom composed annotation e.g. combining @Test and @EnabledOnOs into a single reusable annotation @TestOnMac.

Each of the conditional annotations listed in the following sections can only be declared once on a given test interface, test class, or test method.If a conditional annotation is directly present, indirectly present, or meta-present multiple times on a given element, only the first such annotation discovered by JUnit will be used; any additional declarations will be silently ignored. Note, however, that each conditional annotation may be used in conjunction with other conditional annotations in the org.junit.jupiter.api.condition package.

## 7.1 Operating system conditions
A container or test may be enabled or disabled on a particular operating system by using the @EnabledOnOs or @DisabledOnOs annotations.
