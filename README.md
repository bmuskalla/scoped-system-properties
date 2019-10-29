# Scoped System Properties

### tldr;

Easy to use, lock-free, thread-safe scoped system properties to avoid global state.

### Example

```java
System.setProperty("someKey", "global value");
try (SystemPropertyScope scope = ScopedSystemProperties.scoped()) {
    scope.setProperty("someKey", "scopedValue");
    
    System.getProperty("someKey"); // = "scopedValue"

    // another thread calling System.getProperty("someKey") will yield `global value`
}
System.getProperty("someKey") // = "global value"
````

