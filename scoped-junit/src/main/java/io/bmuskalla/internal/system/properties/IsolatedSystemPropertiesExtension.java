package io.bmuskalla.internal.system.properties;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ExtensionContext.Store;
import org.junit.jupiter.api.extension.ExtensionContext.Store.CloseableResource;

import io.bmuskalla.system.properties.ScopedSystemProperties;

public class IsolatedSystemPropertiesExtension implements Extension, BeforeEachCallback {

	private static final String SCOPE_KEY = IsolatedSystemPropertiesExtension.class.getName();

	@Override
	public void beforeEach(ExtensionContext context) throws Exception {
		LocalPropertyScope scope = ScopedSystemProperties.localScope();

		CloseableResource closableScope = wrapAsClosableResource(() -> scope.close());
		getStore(context).put(SCOPE_KEY, closableScope);
	}

	private CloseableResource wrapAsClosableResource(Runnable closingStatement) {
		return new Store.CloseableResource() {

			@Override
			public void close() throws Throwable {
				closingStatement.run();
			}
		};
	}

	private Store getStore(ExtensionContext context) {
		return context.getStore(Namespace.create(getClass(), context.getRequiredTestMethod()));
	}

}
