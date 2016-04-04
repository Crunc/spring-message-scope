package de.jegair.spring.jms.sample;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import de.jegair.spring.message.scope.MessageScopeConfig;
import de.jegair.spring.message.scope.ScopeMessageListenerContainerFactory;

@SpringBootApplication
@EnableJms
@Import({ MessageScopeConfig.class })
public class App {

	private static final Logger LOG = LoggerFactory.getLogger(App.class);

	public static final String DESTINATION = "myDestination";
	public static final String FACTORY = "myJmsContainerFactory";

	@Bean(name = FACTORY)
	public JmsListenerContainerFactory<?> myJmsContainerFactory(final ConnectionFactory connectionFactory) {
		final ScopeMessageListenerContainerFactory factory = new ScopeMessageListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		return factory;
	}

	public static void main(String[] args) {

		ActiveMQData.delete();

		final ApplicationContext ctx = SpringApplication.run(App.class, args);

		final JmsTemplate jmsTemplate = ctx.getBean(JmsTemplate.class);

		LOG.info("sending message");

		jmsTemplate.send(DESTINATION, new MessageCreator() {

			@Override
			public Message createMessage(final Session session) throws JMSException {
				return session.createTextMessage("ping!");
			}

		});
	}
}
