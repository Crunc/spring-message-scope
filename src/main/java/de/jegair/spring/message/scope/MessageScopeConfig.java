package de.jegair.spring.message.scope;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageScopeConfig {
	
	@Bean
	public static MessageScopeRegistrationBean messageScopeRegistration() {
		return new MessageScopeRegistrationBean();
	}
}
