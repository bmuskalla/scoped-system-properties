/*******************************************************************************
 * Copyright 2019 Benjamin Muskalla
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/

package io.bmuskalla.internal.system.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import io.bmuskalla.system.properties.PropertiesMethodProvider;

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
		assertThat(stream.toString()).endsWith("scopedKey=y\nkey=x\n");
	}

	@Test
	void valuesContainsScopedValues() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");
		
		Collection<Object> values = properties.values();
		
		assertThat(values).containsExactlyInAnyOrder("x", "y");
	}

	@Test
	void valuesContainsOnlyScopedValuesWithOverride() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "key");
		
		Collection<Object> values = properties.values();
		
		assertThat(values).containsExactlyInAnyOrder("y");
	}

	@Test
	void storeWithoutBaselineContainsScopedKeys() throws Exception {
		DelegatingProperties properties = new DelegatingProperties(new Properties());
		properties.setProperty("scopedKey", "x");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();

		properties.store(stream, "comments");

		assertThat(stream.toString()).startsWith("#comment");
		assertThat(stream.toString()).endsWith("\nscopedKey=x\n");
	}
	
	@Test
	void setPropertyStoresValueOnlyScoped() throws Exception {
		Properties baseline = new Properties();
		baseline.put("key", "x");
		DelegatingProperties properties = new DelegatingProperties(baseline);
		properties.setProperty("key", "value");
		
		assertThat(properties.getProperty("key")).isEqualTo("value");
		assertThat(baseline.getProperty("key")).isEqualTo("x");
	}
	
	@Test
	void setPropertyReturnsBaselineValueIfNoScopedValuePresent() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");
		
		Object previousValue = properties.setProperty("key", "value");
		
		assertThat(properties.getProperty("key")).isEqualTo("value");
		assertThat(previousValue).isEqualTo("x");
	}

	@Test
	void getPropertyFallsbackToBaseline() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");
		
		assertThat(properties.getProperty("key")).isEqualTo("x");
	}

	@Test
	void getPropertyWithDefaultWithBaselineKey() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");

		String value = properties.getProperty("key", "notused");
		
		assertThat(value).isEqualTo("x");
	}

	@Test
	void getPropertyWithDefaultWithScopedKey() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");
		
		String value = properties.getProperty("scopedKey", "notused");
		
		assertThat(value).isEqualTo("y");
	}

	@Test
	void getPropertyWithDefaultWithMissingKey() throws Exception {
		DelegatingProperties properties = propertiesWithRegularAndScopedKey("key", "scopedKey");
		
		String value = properties.getProperty("xxx", "default");
		
		assertThat(value).isEqualTo("default");
	}
	
	@Disabled("Only used during development for catching up on API")
	@ParameterizedTest(name = "{1}")
	@ArgumentsSource(PropertiesMethodProvider.class)
	void testWhetherMethodIsImplementedByUs(Method method, String testDisplayName) {
		List<String> worksAsIs = new ArrayList<>();
		worksAsIs.addAll(objectMethods());
		worksAsIs.add("store");
		if (!worksAsIs.contains(method.getName())) {
			List<Method> methodImplementations = Arrays.stream(DelegatingProperties.class.getDeclaredMethods())
					.filter(m -> m.getName().equals(method.getName()))
					.filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
					.collect(Collectors.toList());
			assertThat(methodImplementations).as("no impl found").hasSize(1);
		}
	}

	private List<String> objectMethods() {
		return Arrays.stream(Object.class.getMethods()).map(m -> m.getName()).collect(Collectors.toList());
	}

	private DelegatingProperties propertiesWithRegularAndScopedKey(String regularKey, String scopedKey) {
		Properties baseline = new Properties();
		baseline.put(regularKey, "x");
		DelegatingProperties properties = new DelegatingProperties(baseline);
		properties.setProperty(scopedKey, "y");
		return properties;
	}
}
