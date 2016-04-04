package de.jegair.spring.message.scope;

import org.springframework.jms.config.AbstractJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

/**
 * A {@link JmsListenerContainerFactory} which creates instances of
 * {@link ScopeMessageListenerContainer}.
 * 
 * @author Hauke Jäger, hauke.jaeger@nterra.com
 *
 */
public class ScopeMessageListenerContainerFactory
		extends AbstractJmsListenerContainerFactory<ScopeMessageListenerContainer> {

	@Override
	protected ScopeMessageListenerContainer createContainerInstance() {
		return new ScopeMessageListenerContainer();
	}

}
