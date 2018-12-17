package com.vc.sandpin;

import com.vc.lib.util.Service;
import com.vc.sandpin.test.Employee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public final class PersistenceManager extends Service {
	private static final Logger LOG = LogManager.getLogger();
	private static final SessionFactory SESSION_FACTORY;
	private static final Configuration CONFIG;
	private static final int MONITOR_INTERVAL = 1000 * 60;
	private static final PersistenceManager SINGLETON;

	static {
		LOG.info("Initializing Hibernate configuration.");
		CONFIG = new Configuration().configure().addAnnotatedClass(Employee.class);
		SESSION_FACTORY = CONFIG.buildSessionFactory();
		LOG.info("Hibernate configured and session factory initialized.");
		SINGLETON = new PersistenceManager();
	}

	private PersistenceManager() {
		setLoopRate(MONITOR_INTERVAL);
		start();
	}

	@Override
	protected void run() {
		LOG.info("Status: {}", SESSION_FACTORY.getStatistics().getConnectCount());
	}

	public static Session openSession() {
		return SESSION_FACTORY.openSession();
	}

	public static void close() {
		LOG.info("Closing Hibernate session factory.");
		SESSION_FACTORY.close();
		SINGLETON.stop();
	}
}
