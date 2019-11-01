package io.bmuskalla.system.properties;

import io.bmuskalla.internal.system.properties.LocalPropertyScope;

/**
 * The scope represents a limited lifecycle and lifetime for system properties.
 * <p>
 * Once a scope is acquired, all system property calls will be handled by the
 * active scope. Changes to system properties will only be visible by the thread
 * holding the scope.
 * </p>
 */
public interface PropertyScope extends AutoCloseable {

	/**
	 * Sets the system property indicated by the specified key.
	 *
	 * The given value is only available to callers with access to this scope. See
	 * the documentation for {@link LocalPropertyScope} for more details.
	 * 
	 * This method is just a convince method to help making the intention clear that
	 * the value for the system property is scoped. The same effect can be achieved
	 * by calling <code>System#setProperty(String, String)</code> within scope.
	 * 
	 * @see System#setProperty(String, String)
	 */
	void setProperty(String key, String value);

	@Override
	void close();

}
