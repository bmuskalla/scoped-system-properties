package io.bmuskalla.system.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.Properties;

import org.junit.jupiter.api.Test;

public class DelegatingPropertiesTest {

	@Test
	void propertyNamesIncludesScopedValues() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "newKey");

		assertThat(Collections.list(properties.propertyNames())).asList().containsExactlyInAnyOrder("key", "newKey");
	}

	@Test
	void stringPropertyNamesIncludesScopedValues() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "newKey");

		assertThat(properties.stringPropertyNames()).containsExactlyInAnyOrder("key", "newKey");
	}

	@Test
	void keysIncludesScopedValues() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "newKey");

		assertThat(properties.keySet()).containsExactlyInAnyOrder("key", "newKey");
		assertThat(Collections.list(properties.keys())).asList().containsExactlyInAnyOrder("key", "newKey");
	}

	@Test
	void canRemoveScopedKey() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");

		properties.remove("scopedKey");

		assertThat(properties.keySet()).containsExactlyInAnyOrder("key");
	}

	@Test
	void containsSeesScopedKey() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");

		boolean contains = properties.contains("scopedKey");
		boolean containsKey = properties.containsKey("scopedKey");

		assertThat(contains).isTrue();
		assertThat(containsKey).isTrue();
	}

	@Test
	void storeContainsScopedKeys() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		properties.store(stream, "comments");

		assertThat(stream.toString()).startsWith("#comment");
		assertThat(stream.toString()).endsWith("key=x\nscopedKey=x\n");
	}

	@Test
	void storeWithoutBaselineContainsScopedKeys() throws Exception {
		DelegatingProperties properties = new DelegatingProperties(new Properties());
		properties.setScopedProperty("scopedKey", "x");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		
		properties.store(stream, "comments");
		
		assertThat(stream.toString()).startsWith("#comment");
		assertThat(stream.toString()).endsWith("\nscopedKey=x\n");
	}

	private DelegatingProperties propertiesWithRegularAndScopedKey(String regularKey, String scopedKey) {
		Properties baseline = new Properties();
		baseline.put(regularKey, "x");
		DelegatingProperties properties = new DelegatingProperties(baseline);
		properties.setScopedProperty(scopedKey, "x");
		return properties;
	}
}
