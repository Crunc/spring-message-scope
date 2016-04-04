package de.jegair.spring.message.scope;

public class MessageScopeContextHolder {

	private static final ThreadLocal<MessageScopeAttrs> holder = new InheritableThreadLocal<MessageScopeAttrs>();

	public static MessageScopeAttrs get() {
		return holder.get();
	}

	public static void set(final MessageScopeAttrs attrs) {
		holder.set(attrs);
	}

	public static MessageScopeAttrs current() throws IllegalStateException {
		final MessageScopeAttrs attrs = holder.get();

		if (attrs == null) {
			throw new IllegalStateException(
					"there are no message scoped attributes associated with the current Thread");
		}

		return attrs;
	}

	public static void clear() {
		holder.remove();
	}

}