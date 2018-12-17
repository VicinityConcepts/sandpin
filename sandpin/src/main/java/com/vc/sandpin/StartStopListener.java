package com.vc.sandpin;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartStopListener implements ServletContextListener {
	private static final Logger LOG = LogManager.getLogger();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.info("Sandpin servlet initializing.");

		try {
			//Class.forName(PersistenceManager.class.getName());
		} catch (Exception e) {
			LOG.error("Failed to initialize Sandpin servlet.", e);
			throw new ExceptionInInitializerError("Sandpin initialization failed.");
		}

		LOG.info("Sandpin servlet initialization complete.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		PersistenceManager.close();
		LOG.info("Sandpin servlet destroyed.");
	}
}
