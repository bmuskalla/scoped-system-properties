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

import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Sets;

public class DelegatingProperties extends Properties {

	private static final long serialVersionUID = 1L;

	private Properties baseline;

	private final ThreadLocal<Properties> overrides = new ThreadLocal<Properties>() {
		protected Properties initialValue() {
			return baseline;
		};
	};

	public DelegatingProperties(Properties baseline) {
		overrides.set(new Properties(baseline));
		this.baseline = baseline;
	}

	@Override
	public Object get(Object key) {
		Object overridenValue = overrides().get(key);
		return overridenValue != null ? overridenValue : baseline.get(key);
	}

	@Override
	public String getProperty(String key) {
		return overrides().getProperty(key);
	}

	@Override
	public Enumeration<?> propertyNames() {
		return overrides().propertyNames();
	}

	@Override
	public Set<String> stringPropertyNames() {
		return overrides().stringPropertyNames();
	}

	@Override
	public Set<Object> keySet() {
		return Sets.union(overrides().keySet(), baseline.keySet());
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(keySet());
	}

	@Override
	public Collection<Object> values() {
		Set<Object> baselineKeys = baseline.keySet();
		Stream<Object> overrideValues = overrides().values().stream();
		return Stream.concat(overrideValues, baselineValuesWithoutOverrides(baselineKeys)).collect(toList());
	}

	private Stream<Object> baselineValuesWithoutOverrides(Set<Object> baselineKeys) {
		return baselineKeys.stream().filter(k -> !overrides().keySet().contains(k)).map(k -> get(k));
	}

	@Override
	public synchronized Object remove(Object key) {
		return overrides().remove(key);
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return overrides().containsKey(key) || baseline.containsKey(key);
	}

	@Override
	public synchronized boolean contains(Object value) {
		return overrides().containsKey(value) || baseline.containsKey(value);
	}

	@Override
	public synchronized Object setProperty(String key, String value) {
		Object previousValue = overrides().setProperty(key, value);
		if(previousValue == null) {
			return baseline.get(key);
		}
		return previousValue;
	}
	
	private Properties overrides() {
		return overrides.get();
	}

}
