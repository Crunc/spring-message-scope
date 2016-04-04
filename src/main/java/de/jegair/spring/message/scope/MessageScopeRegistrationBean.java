package de.jegair.spring.message.scope;

import java.io.Serializable;

import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.util.Assert;

/**
 * A {@link BeanFactoryPostProcessor} which registers the {@link MessageScope}
 * with the Spring application context.
 * 
 * @author Hauke Jäger, hauke.jaeger@nterra.com
 *
 */
public class MessageScopeRegistrationBean implements BeanFactoryPostProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(MessageScopeRegistrationBean.class);

	/**
	 * The name of the scope which can be used with {@link Scope}.
	 */
	private final String scopeName;

	/**
	 * The scope instance which will be registered.
	 */
	private final MessageScope scope;

	/**
	 * Creates a new registration bean which registers a new
	 * {@link MessageScope} instance under the default name
	 * {@link MessageScope#SCOPE_NAME}.
	 */
	public MessageScopeRegistrationBean() {
		this(MessageScope.SCOPE_NAME);
	}

	/**
	 * Creates a new registration bean which registers a new
	 * {@link MessageScope} instance under the given name.
	 */
	public MessageScopeRegistrationBean(final String scopeName) {
		this(scopeName, new MessageScope());
	}

	/**
	 * Creates a new registration bean which registers the given scope instance
	 * under the given name.
	 */
	public MessageScopeRegistrationBean(final String scopeName, final MessageScope scope) {
		Assert.notNull(scopeName, "[scopeName] must not be null");
		Assert.notNull(scope, "[scope] must not be null");
		
		this.scopeName = scopeName;
		this.scope = scope;
	}

	@Override
	public void postProcessBeanFactory(final ConfigurableListableBeanFactory beanFactory) throws BeansException {

		LOG.info("Registering JMS message scope as '{}'", scopeName);

		beanFactory.registerScope(scopeName, scope);
		beanFactory.registerResolvableDependency(Message.class, new MessageObjectFactory());
		beanFactory.registerResolvableDependency(Session.class, new SessionObjectFactory());
	}

	@SuppressWarnings("serial")
	private static class MessageObjectFactory implements ObjectFactory<Message>, Serializable {

		@Override
		public Message getObject() {
			return MessageScopeContextHolder.current().getMessage();
		}

		@Override
		public String toString() {
			return "Current JMS Message";
		}
	}

	@SuppressWarnings("serial")
	private static class SessionObjectFactory implements ObjectFactory<Session>, Serializable {

		@Override
		public Session getObject() {
			return MessageScopeContextHolder.current().getSession();
		}

		@Override
		public String toString() {
			return "Current JMS Session";
		}
	}

}
