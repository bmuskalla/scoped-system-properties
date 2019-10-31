package io.bmuskalla.system.properties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Sets;

class DelegatingProperties extends Properties {

	private Properties baseline;
	
	private final ThreadLocal<Properties> withOverrides = new ThreadLocal<Properties>() {
		protected Properties initialValue() {
			return baseline;
		};
	};

	public DelegatingProperties(Properties baseline) {
		withOverrides.set(new Properties(baseline));
		this.baseline = baseline;
	}

	public void setScopedProperty(String key, String value) {
		store().put(key, value);
	}

	@Override
	public Object get(Object key) {
		return store().get(key);
	}

	@Override
	public String getProperty(String key) {
		return store().getProperty(key);
	}
	
	@Override
	public Enumeration<?> propertyNames() {
		return store().propertyNames();
	}
	
	@Override
	public Set<String> stringPropertyNames() {
		return store().stringPropertyNames();
	}
	
	@Override
	public Set<Object> keySet() {
		return Sets.union(store().keySet(), baseline.keySet());
	}
	
	@Override
	public synchronized Enumeration<Object> keys() {
		return Collections.enumeration(keySet());
	}
	
	@Override
	public synchronized Object remove(Object key) {
		return store().remove(key);
	}
	
	@Override
	public synchronized boolean containsKey(Object key) {
		return store().containsKey(key) || baseline.containsKey(key);
	}
	
	@Override
	public synchronized boolean contains(Object value) {
		return store().containsKey(value) || baseline.containsKey(value);
	}
	
	@Override
	public void store(OutputStream out, String comments) throws IOException {
		baseline.store(out, comments);
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		store().store(byteStream, "");
		String content = byteStream.toString();
		String[] lines = content.split("\\n");
		for (int i = 2; i < lines.length; i++) {
			out.write(lines[i].getBytes());
			out.write("\n".getBytes());
		}
	}
	
	private Properties store() {
		return withOverrides.get();
	}

}
