package io.bmuskalla.system.properties;

import java.util.Properties;

class DelegatingProperties extends Properties {

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

	public void addOverride(String key, String value) {
		overrides.get().put(key, value);
	}

	@Override
	public Object get(Object key) {
		return overrides.get().get(key);
	}

	@Override
	public String getProperty(String key) {
		return overrides.get().getProperty(key);
	}

}
