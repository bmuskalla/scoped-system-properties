package io.bmuskalla.system.properties;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@IsolatedSystemProperties
public class IsolatedSystemPropertiesIntegrationTest {

	static {
		System.setProperty("key", "baseline");
	}
	
	@BeforeEach
	void setUp() {
		assertEquals("baseline", System.getProperty("key"));
		System.setProperty("key", "before");
	}

	@Test
	void setsValueInTest() throws Exception {
		assertEquals("before", System.getProperty("key"));
		System.setProperty("key", "other");
	}
	
	@Test
	void seesBaseline() throws Exception {
		assertEquals("before", System.getProperty("key"));
	}

	@Test
	void seesScopedValue() throws Exception {
		assertEquals("before", System.getProperty("key"));
		System.setProperty("key", "scoped");
		assertEquals("scoped", System.getProperty("key"));
	}

	@Test
	void seesScopedValue2() throws Exception {
		assertEquals("before", System.getProperty("key"));
		System.setProperty("key", "scoped2");
		assertEquals("scoped2", System.getProperty("key"));
	}
	
}
