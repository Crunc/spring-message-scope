package de.jegair.spring.jms.sample;

import javax.jms.JMSException;
import javax.jms.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageAwareProcessor {
	
	private static final Logger LOG = LoggerFactory.getLogger(MessageAwareProcessor.class);

	private final Message message;

	@Autowired
	public MessageAwareProcessor(final Message message) {
		this.message = message;
	}
	
	public void doSomething() throws JMSException {
		
		LOG.info("processing message {}", message.getJMSMessageID());
	}
}
