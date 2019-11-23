package io.bmuskalla.system.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.bmuskalla.internal.system.properties.IsolatedSystemPropertiesExtension;

public class IsolatedSystemPropertiesTest {

	@Test
	void hasRuntimeRetention() throws Exception {
		Retention retention = IsolatedSystemProperties.class.getAnnotation(Retention.class);

		assertThat(retention.value()).isEqualTo(RetentionPolicy.RUNTIME);
	}

	@Test
	void hasExtension() throws Exception {
		ExtendWith extendWith = IsolatedSystemProperties.class.getAnnotation(ExtendWith.class);

		assertThat(extendWith.value()).containsExactly(IsolatedSystemPropertiesExtension.class);
	}

	@Test
	void applicableToMethodOrClass() throws Exception {
		Target target = IsolatedSystemProperties.class.getAnnotation(Target.class);

		List<Object> supportedTargets = Arrays.asList(target.value());
		
		assertThat(supportedTargets).containsExactlyInAnyOrder(ElementType.TYPE, ElementType.METHOD);
	}

}
