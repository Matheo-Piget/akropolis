package util;

// The assert is disabled by default so we will make our own assert
public class BetterAssert {
    public static void assertIsTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Assertion failed");
        }
    }
}
