# Scoped System Properties

[![Build](https://github.com/bmuskalla/scoped-system-properties/workflows/build/badge.svg)](https://github.com/bmuskalla/scoped-system-properties/actions)

## tldr;

Easy to use, lock-free, thread-safe scoped system properties to isolate changes to global state.

## Motivation

If you need to change system properties for specific calls to 3rd party APIs, you could just use a `try/finally` construct. The downside is that you need to ensure that no other code in parallel tries to read/write the same system properties. Other folks use a global lock to around the `try/finally` to avoid any other thread in the system to see this particular state.

While generally, your code should abstract away access to global state like system properties, 3rd party code you use may not do that. Too many libraries rely on specific system properties for configuration or feature toggles.

This library helps to deal with those 3rd party dependencies that require the usage of system properties by isolating access and mutation to said system properties.

## Library

### Gradle
*Groovy*
```groovy
implementation 'io.github.bmuskalla:scoped-system-properties:1.0.0'
```

*Kotlin*
```kotlin
implementation("io.github.bmuskalla:scoped-system-properties:1.0.0")
```

### Maven
```xml
<dependency>
    <groupId>io.github.bmuskalla</groupId>
    <artifactId>scoped-system-properties</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Example (as library)

```java
System.setProperty("someKey", "global value");
try (PropertyEnvironment env = ScopedSystemProperties.newPropertyEnvironment()) {
    env.setProperty("someKey", "scopedValue");
    // or using the usual Java APIs
    // System.setProperty("someKey", "scopedValue");

    System.getProperty("someKey"); // = "scopedValue"

    // another thread calling System.getProperty("someKey") will see `global value`
}
System.getProperty("someKey") // = "global value"
````

## JUnit 5 Extension

### Gradle
*Groovy*
```groovy
implementation 'io.github.bmuskalla:scoped-system-properties-junit:1.0.0'
```

*Kotlin*
```kotlin
implementation("io.github.bmuskalla:scoped-system-properties-junit:1.0.0")
```

### Maven
```xml
<dependency>
    <groupId>io.github.bmuskalla</groupId>
    <artifactId>scoped-system-properties-junit</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Example (in JUnit Jupiter)

```java
@BeforeEach
void setUp() {
    assertEquals("baseline", System.getProperty("key"));
    System.setProperty("key", "before");
}

@Test
@IsolatedSystemProperties
void setsValueInTest() throws Exception {
    assertEquals("before", System.getProperty("key"));
    System.setProperty("key", "other");
}

@AfterAll
static void tearDown() {
    assertEquals("baseline", System.getProperty("key"));		
}
```

`@IsolatedSystemProperties` can be used on individual test methods or on the whole test class. In both cases, the individual test (and the respective lifecycle methods like `@BeforeEach`) will be isolated against the rest of the environment. 

#### Alternative approach using JUnit

Proper isolation can be achieved by leveraging a global lock using JUnits [@ResourceLock](https://junit.org/junit5/docs/current/api/org/junit/jupiter/api/parallel/ResourceLock.html) (see [Synchronization when running Parallel Tests](https://junit.org/junit5/docs/current/user-guide/#writing-tests-parallel-execution-synchronization)). While this has the same effect of avoiding race conditions on the system properties, such tests cannot be run in parallel if they require mutating access to system properties.

# Changelog

## 1.0.0 Reworked API
* Favor `PropertyEnvironment` as concept

## 0.5.0 Initial release
* Support as standalone library
* Support as JUnit 5 extension