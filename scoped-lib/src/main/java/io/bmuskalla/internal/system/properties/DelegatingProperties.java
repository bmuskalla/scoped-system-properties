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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

public class DelegatingProperties extends Properties {

	private static final long serialVersionUID = 1L;

	private Properties baseline;

	private final ThreadLocal<Properties> environmentProperties = new ThreadLocal<Properties>() {
		protected Properties initialValue() {
			return baseline;
		};
	};

	public DelegatingProperties(Properties baseline) {
		Properties newEnvironment = buildNewEnvironment(baseline);
		this.environmentProperties.set(newEnvironment);
		this.baseline = baseline;
	}

	private Properties buildNewEnvironment(Properties baseline) {
		Properties frozenBaseline = (Properties) baseline.clone();
		Properties newEnvironment = new Properties();
		frozenBaseline.stringPropertyNames().forEach(k -> newEnvironment.setProperty(k, frozenBaseline.getProperty(k)));
		return newEnvironment;
	}

	@Override
	public synchronized Object get(Object key) {
		return environmentProperties.get().get(key);
	}

	@Override
	public Enumeration<?> propertyNames() {
		return environmentProperties.get().propertyNames();
	}

	@Override
	public synchronized Object put(Object key, Object value) {
		return environmentProperties.get().put(key, value);
	}

	@Override
	public synchronized Object setProperty(String key, String value) {
		return environmentProperties.get().setProperty(key, value);
	}

	@Override
	public String getProperty(String key, String defaultValue) {
		return environmentProperties.get().getProperty(key, defaultValue);
	}

	@Override
	public synchronized boolean contains(Object value) {
		return environmentProperties.get().contains(value);
	}

	@Override
	public synchronized boolean containsKey(Object key) {
		return environmentProperties.get().containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return environmentProperties.get().containsValue(value);
	}

	@Override
	public Collection<Object> values() {
		return environmentProperties.get().values();
	}

	@Override
	public synchronized Enumeration<Object> keys() {
		return environmentProperties.get().keys();
	}

	@Override
	public Set<Object> keySet() {
		return environmentProperties.get().keySet();
	}

	@Override
	public void store(OutputStream out, String comments) throws IOException {
		environmentProperties.get().store(out, comments);
	}

	@Override
	public void store(Writer writer, String comments) throws IOException {
		environmentProperties.get().store(writer, comments);
	}

	@Override
	public void storeToXML(OutputStream os, String comment) throws IOException {
		environmentProperties.get().storeToXML(os, comment);
	}

	@Override
	public void storeToXML(OutputStream os, String comment, String encoding) throws IOException {
		environmentProperties.get().storeToXML(os, comment, encoding);
	}

	// only @since Java 10
	// no @Override to stay backwards compatible 
	public void storeToXML(OutputStream os, String comment, Charset charset) throws IOException {
		storeToXML(os, comment, charset.name());
	}

	public void load(Reader reader) throws IOException {
		environmentProperties.get().load(reader);
	}

	public int size() {
		return environmentProperties.get().size();
	}

	public boolean isEmpty() {
		return environmentProperties.get().isEmpty();
	}

	public Enumeration<Object> elements() {
		return environmentProperties.get().elements();
	}

	public void load(InputStream inStream) throws IOException {
		environmentProperties.get().load(inStream);
	}

	public Object remove(Object key) {
		return environmentProperties.get().remove(key);
	}

	public void putAll(Map<? extends Object, ? extends Object> t) {
		environmentProperties.get().putAll(t);
	}

	public void clear() {
		environmentProperties.get().clear();
	}

	public Object clone() {
		return environmentProperties.get().clone();
	}

	public String toString() {
		return environmentProperties.get().toString();
	}

	public Set<java.util.Map.Entry<Object, Object>> entrySet() {
		return environmentProperties.get().entrySet();
	}

	@SuppressWarnings("deprecation")
	public void save(OutputStream out, String comments) {
		environmentProperties.get().save(out, comments);
	}

	public boolean equals(Object o) {
		return environmentProperties.get().equals(o);
	}

	public int hashCode() {
		return environmentProperties.get().hashCode();
	}

	public Object getOrDefault(Object key, Object defaultValue) {
		return environmentProperties.get().getOrDefault(key, defaultValue);
	}

	public void forEach(BiConsumer<? super Object, ? super Object> action) {
		environmentProperties.get().forEach(action);
	}

	public void replaceAll(BiFunction<? super Object, ? super Object, ? extends Object> function) {
		environmentProperties.get().replaceAll(function);
	}

	public Object putIfAbsent(Object key, Object value) {
		return environmentProperties.get().putIfAbsent(key, value);
	}

	public boolean remove(Object key, Object value) {
		return environmentProperties.get().remove(key, value);
	}

	public boolean replace(Object key, Object oldValue, Object newValue) {
		return environmentProperties.get().replace(key, oldValue, newValue);
	}

	public void loadFromXML(InputStream in) throws IOException, InvalidPropertiesFormatException {
		environmentProperties.get().loadFromXML(in);
	}

	public Object replace(Object key, Object value) {
		return environmentProperties.get().replace(key, value);
	}

	public Object computeIfAbsent(Object key, Function<? super Object, ? extends Object> mappingFunction) {
		return environmentProperties.get().computeIfAbsent(key, mappingFunction);
	}

	public Object computeIfPresent(Object key,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return environmentProperties.get().computeIfPresent(key, remappingFunction);
	}

	public Object compute(Object key, BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return environmentProperties.get().compute(key, remappingFunction);
	}

	public Object merge(Object key, Object value,
			BiFunction<? super Object, ? super Object, ? extends Object> remappingFunction) {
		return environmentProperties.get().merge(key, value, remappingFunction);
	}

	public String getProperty(String key) {
		return environmentProperties.get().getProperty(key);
	}

	public Set<String> stringPropertyNames() {
		return environmentProperties.get().stringPropertyNames();
	}

	public void list(PrintStream out) {
		environmentProperties.get().list(out);
	}

	public void list(PrintWriter out) {
		environmentProperties.get().list(out);
	}

}
