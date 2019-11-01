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

import io.bmuskalla.internal.system.properties.LocalPropertyScope;

/**
 * Provides a way to work with system properties without changing global state.
 */
public class ScopedSystemProperties {

	/**
	 * Returns a {@link PropertyScope} that is bound to the current thread.
	 * <p>
	 * Any changes to system properties are only visible within the thread that
	 * opened the scope. A scope should be closed after usage to restore the regular
	 * behavior of system properties within the thread.
	 * </p>
	 * <p>
	 * Changes to system properties within the scope/thread are not visible to other
	 * scopes/threads.
	 * </p>
	 * 
	 * @return a thread-local {@link PropertyScope}
	 */
	public static LocalPropertyScope localScope() {
		return new LocalPropertyScope();
	}

	private ScopedSystemProperties() {
		// prevent direct instantiation
	}

}
