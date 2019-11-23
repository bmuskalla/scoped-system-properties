package io.bmuskalla.system.properties;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.junit.jupiter.api.extension.ExtendWith;

import io.bmuskalla.internal.system.properties.IsolatedSystemPropertiesExtension;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(IsolatedSystemPropertiesExtension.class)
public @interface IsolatedSystemProperties {

}
