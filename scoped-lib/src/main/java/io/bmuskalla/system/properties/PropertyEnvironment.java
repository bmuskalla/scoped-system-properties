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

package io.bmuskalla.system.properties;

import java.util.Properties;

import io.bmuskalla.internal.system.properties.DelegatingProperties;

/**
 * The environment represents a limited lifecycle and lifetime for system
 * properties.
 * <p>
 * Once an environment is acquired, all system property calls will be handled by
 * the active environment. Changes to system properties will only be visible by
 * the thread holding the environment or any new threads started by it.
 * </p>
 */
public class PropertyEnvironment implements AutoCloseable {

	private DelegatingProperties propertyStore;

	private final Properties originalProperties;

	public PropertyEnvironment() {
		originalProperties = System.getProperties();
		propertyStore = new DelegatingProperties(originalProperties);
		System.setProperties(propertyStore);
	}

	/**
	 * Sets the system property indicated by the specified key.
	 *
	 * The given value is only available to callers with access to this environment.
	 * See the documentation for {@link PropertyEnvironment} for more details.
	 * 
	 * This method is just a convince method to help making the intention clear that
	 * the value for the system property is scoped. The same effect can be achieved
	 * by calling <code>System#setProperty(String, String)</code> while a
	 * {@link PropertyEnvironment} is active.
	 * 
	 * @param key   the name of the system property.
	 * @param value the value of the system property.
	 * @see System#setProperty(String, String)
	 */
	public void setProperty(String key, String value) {
		propertyStore.setProperty(key, value);
	}

	@Override
	public void close() {
		System.setProperties(originalProperties);
	}

}
