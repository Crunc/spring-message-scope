package de.jegair.spring.jms.sample;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileSystemUtils;

public final class ActiveMQData {

	private static final Logger LOG = LoggerFactory.getLogger(ActiveMQData.class);

	private static final String LOCATION = "activemq-data";

	public static void delete() {
		LOG.info("deleting ActiveMQ data at {}", LOCATION);
		FileSystemUtils.deleteRecursively(new File(LOCATION));
	}
}
