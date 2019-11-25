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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;

class ScopedSystemPropertiesTest {

	@Test
	void onlySeesScopedValueWithinScope() {
		try (PropertyEnvironment env = ScopedSystemProperties.newPropertyEnvironment()) {
			env.setProperty("scopedKey", "scopedValue");
			assertThatSystemPropertyHasValue("scopedKey", "scopedValue");
		}
		assertThatSystemPropertyHasValue("scopedKey", null);
	}

	@Test
	void otherThreadDoesNotSeeScopedValue() throws Exception {
		CountDownLatch setupLatch = new CountDownLatch(1);
		CountDownLatch assertionLatch = new CountDownLatch(1);
		new Thread(() -> {
			try {
				setupLatch.await(1, TimeUnit.MINUTES);
			} catch (InterruptedException e) {
				throw new AssertionError(e);
			}
			assertThatSystemPropertyHasValue("scopedKey", null);
			assertionLatch.countDown();
		}, "outside scope").start();
		try (PropertyEnvironment env = ScopedSystemProperties.newPropertyEnvironment()) {
			env.setProperty("scopedKey", "scopedValue");
			setupLatch.countDown();
			assertThatSystemPropertyHasValue("scopedKey", "scopedValue");
		}
		assertionLatch.await(1, TimeUnit.MINUTES);

		assertThatSystemPropertyHasValue("scopedKey", null);
	}

	@Test
	void booleanMethodUsesScopedValue() {
		try (PropertyEnvironment env = ScopedSystemProperties.newPropertyEnvironment()) {
			env.setProperty("scopedKey", "true");
			assertThat(Boolean.getBoolean("scopedKey")).isTrue();
		}
		assertThatSystemPropertyHasValue("scopedKey", null);
	}

	
	private void assertThatSystemPropertyHasValue(String key, String value) {
		assertThat(System.getProperty(key)).isEqualTo(value);
	}
}
