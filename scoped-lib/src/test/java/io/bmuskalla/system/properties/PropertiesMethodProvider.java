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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

public class PropertiesMethodProvider implements ArgumentsProvider {

	@Override
	public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
		return Arrays.stream(Properties.class.getMethods()).filter(m -> !Modifier.isFinal(m.getModifiers())).map(m -> Arguments.of(m, toName(m)));
	}

	private String toName(Method method) {
		String parameterTypes = Arrays.stream(method.getParameters())
				.map(p -> p.getType().getSimpleName()).collect(Collectors.joining(", "));
		return MessageFormat.format("{0}({1})", method.getName(), parameterTypes);
	}

}
