# Scoped System Properties

### tldr;

Easy to use, lock-free, thread-safe scoped system properties to avoid changing global state.

### Description

While generally, your code should abstract away access to global state like system properties, 3rd party code you use may not do that. Too many libraries rely on specific system properties for configuration or feature toggles.

If you need to change system properties for specific calls to 3rd party APIs, you could just use a `try/finally` construct. The downside is that you need to ensure that no other code in parallel tries to read/write the same system properties.

### Example

```java
System.setProperty("someKey", "global value");
try (SystemPropertyScope scope = ScopedSystemProperties.scoped()) {
    scope.setProperty("someKey", "scopedValue");
    // or using the usual Java APIs
    // System.setProperty("someKey", "scopedValue");

    System.getProperty("someKey"); // = "scopedValue"

    // another thread calling System.getProperty("someKey") will yield `global value`
}
System.getProperty("someKey") // = "global value"
````


