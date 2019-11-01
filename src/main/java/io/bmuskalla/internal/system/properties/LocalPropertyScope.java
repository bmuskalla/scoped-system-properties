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

import java.util.Properties;

import io.bmuskalla.system.properties.PropertyScope;

public class LocalPropertyScope implements PropertyScope {

	private DelegatingProperties propertyStore;

	private final Properties originalProperties;

	public LocalPropertyScope() {
		originalProperties = System.getProperties();
		propertyStore = new DelegatingProperties(originalProperties);
		System.setProperties(propertyStore);
	}

	@Override
	public void setProperty(String key, String value) {
		propertyStore.setProperty(key, value);
	}

	@Override
	public void close() {
		System.setProperties(originalProperties);
	}

}
