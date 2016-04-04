package de.jegair.spring.jms.sample;

import javax.jms.JMSException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {

	private static final Logger LOG = LoggerFactory.getLogger(Receiver.class);

	private final ConfigurableApplicationContext appCtx;
	private final MessageAwareProcessor processor;

	@Autowired
	public Receiver(final ConfigurableApplicationContext appCtx, final MessageAwareProcessor processor) {
		this.appCtx = appCtx;
		this.processor = processor;
	}

	@JmsListener(destination = App.DESTINATION, containerFactory = App.FACTORY)
	public void receiveMessage(final String message) throws JMSException {

		LOG.info("Received <{}>", message);

		processor.doSomething();
		
		appCtx.close();

		ActiveMQData.delete();
	}
}
