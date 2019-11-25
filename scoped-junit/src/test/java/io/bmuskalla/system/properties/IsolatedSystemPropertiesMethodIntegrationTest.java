package io.bmuskalla.system.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class IsolatedSystemPropertiesMethodIntegrationTest {

	static {
		System.setProperty("key", "baseline");
	}
	
	@BeforeEach
	void setUp() {
		assertEquals("baseline", System.getProperty("key"));
		System.setProperty("key", "before");
	}

	@Test
	@IsolatedSystemProperties
	void setsValueInTest() throws Exception {
		assertEquals("before", System.getProperty("key"));
		System.setProperty("key", "inTest");
		assertEquals("inTest", System.getProperty("key"));
	}
	
	@AfterAll
	static void tearDown() {
		assertEquals("baseline", System.getProperty("key"));		
	}
	
}
