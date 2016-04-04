package de.jegair.spring.message.scope;

import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SimpleMessageListenerContainer;

public class ScopeMessageListenerContainer extends SimpleMessageListenerContainer {

	private static final Logger LOG = LoggerFactory.getLogger(ScopeMessageListenerContainer.class);

	@Override
	protected void processMessage(final Message message, final Session session) {

		LOG.info("creating message scope context for message <{}> and session <{}>", message, session);
		MessageScopeContextHolder.set(new MessageScopeAttrs(message, session));

		try {
			super.processMessage(message, session);
		} finally {
			MessageScopeContextHolder.clear();
			LOG.info("cleared message scope context for message <{}> and session <{}>", message, session);
		}
	}
}
