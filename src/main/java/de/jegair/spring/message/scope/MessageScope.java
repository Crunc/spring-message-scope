package de.jegair.spring.message.scope;

import java.util.concurrent.Callable;

import javax.jms.JMSException;
import javax.jms.Message;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

/**
 * A {@link Scope} implementation which uses the {@link MessageScopeAttrs} for
 * storing and retrieving
 * 
 * @author Hauke Jäger, hauke.jaeger@nterra.com
 *
 */
public class MessageScope implements Scope {

	public static final String SCOPE_NAME = "jmsMessage";

	/**
	 * Resolves the current conversation-ID.
	 * 
	 * @see MessageScope#getConversationId()
	 */
	private final Callable<String> conversationIdResolver;

	/**
	 * Creates a new scope which uses the current JMS Message-ID as
	 * conversation-ID.
	 */
	public MessageScope() {
		this(new JMSMessageIDResolver());
	}

	/**
	 * Creates a new scope which uses the given callback to determine it's
	 * conversation-ID.
	 */
	public MessageScope(final Callable<String> conversationIdResolver) {
		this.conversationIdResolver = conversationIdResolver;
	}

	@Override
	public Object get(final String name, final ObjectFactory<?> factory) {

		final MessageScopeAttrs scopeAttrs = MessageScopeContextHolder.current();

		if (!scopeAttrs.containsBean(name)) {
			return scopeAttrs.createBean(name, factory.getObject());
		} else {
			return scopeAttrs.getBean(name);
		}
	}

	@Override
	public Object remove(final String name) {

		final MessageScopeAttrs scopeAttrs = MessageScopeContextHolder.current();
		return scopeAttrs.removeBean(name);
	}

	@Override
	public void registerDestructionCallback(final String name, final Runnable callback) {

		final MessageScopeAttrs scopeAttrs = MessageScopeContextHolder.current();
		scopeAttrs.registerDestructionCallback(name, callback);
	}

	@Override
	public Object resolveContextualObject(final String key) {

		final MessageScopeAttrs scopeAttrs = MessageScopeContextHolder.current();
		return scopeAttrs.getMessage();
	}

	/**
	 * Gets current thread name as the conversation id.
	 */
	public String getConversationId() {
		try {
			return conversationIdResolver.call();
		} catch (final Exception e) {
			throw new IllegalStateException("failed to retrieve message scope conversationId", e);
		}
	}

	/**
	 * Retrieves the current JMS Message ID.
	 */
	private static class JMSMessageIDResolver implements Callable<String> {

		@Override
		public String call() throws Exception {

			final MessageScopeAttrs scopeAttrs = MessageScopeContextHolder.current();
			final Message message = scopeAttrs.getMessage();

			try {
				return message.getJMSMessageID();
			} catch (final JMSException e) {
				throw new IllegalStateException("unable to retrieve JMS message ID from message " + message
						+ " in session " + scopeAttrs.getSession(), e);
			}
		}

	}

}