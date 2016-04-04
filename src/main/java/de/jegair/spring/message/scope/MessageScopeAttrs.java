package de.jegair.spring.message.scope;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

public class MessageScopeAttrs {

	private static final Logger LOG = LoggerFactory.getLogger(MessageScopeAttrs.class);

	private final Map<String, Object> beans = new HashMap<String, Object>();
	private final Map<String, Runnable> destructionCallbacks = new LinkedHashMap<String, Runnable>();

	private final Message message;
	private final Session session;

	public MessageScopeAttrs(final Message message, final Session session) {
		this.message = message;
		this.session = session;
	}

	public Message getMessage() {
		return message;
	}

	public Session getSession() {
		return session;
	}

	public Object getBean(final String name) {
		return beans.get(name);
	}

	public boolean containsBean(final String name) {
		return beans.containsKey(name);
	}

	public Object createBean(final String name, final Object bean) {
		beans.put(name, bean);
		return bean;
	}

	public Object removeBean(final String name) {
		return beans.remove(name);
	}

	protected final void registerDestructionCallback(final String name, final Runnable callback) {
		Assert.notNull(name, "[name] must not be null");
		Assert.notNull(callback, "[callback] must not be null");

		destructionCallbacks.put(name, callback);
	}

	/**
	 * Clears beans and processes all bean destruction callbacks.
	 */
	protected final void clear() {
		processDestructionCallbacks();

		beans.clear();
	}

	/**
	 * Processes all bean destruction callbacks.
	 */
	private final void processDestructionCallbacks() {
		for (String name : destructionCallbacks.keySet()) {
			Runnable callback = destructionCallbacks.get(name);

			LOG.debug("Performing destruction callback for '" + name + "' bean" + " on thread '"
					+ Thread.currentThread().getName() + "'.");

			callback.run();
		}

		destructionCallbacks.clear();
	}

}