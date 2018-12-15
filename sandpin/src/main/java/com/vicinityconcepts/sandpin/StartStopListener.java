package com.vicinityconcepts.sandpin;


import com.vicinityconcepts.lib.util.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class StartStopListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		try {
			Class.forName(PersistenceManager.class.getName());
		} catch (Exception e) {
			Log.error("Failed to initialize Sandpin servlet.", e);
			throw new ExceptionInInitializerError("Sandpin initialization failed.");
		}

		Log.info("Sandpin servlet initialization complete.");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		PersistenceManager.close();
		Log.info("Sandpin servlet destroyed.");
	}
}
