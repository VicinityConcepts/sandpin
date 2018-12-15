package com.vicinityconcepts.sandpin;

import com.vicinityconcepts.sandpin.test.Employee;
import com.vicinityconcepts.lib.util.Log;
import com.vicinityconcepts.lib.util.Service;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

public final class PersistenceManager extends Service {
	private static final SessionFactory SESSION_FACTORY;
	private static final Configuration CONFIG;
	private static final int MONITOR_INTERVAL = 1000 * 10;
	private static final PersistenceManager SINGLETON;

	static {
		Log.info("Initializing Hibernate configuration.");
		CONFIG = new Configuration().configure().addAnnotatedClass(Employee.class);
		SESSION_FACTORY = CONFIG.buildSessionFactory();
		Log.info("Hibernate configured and session factory initialized.");
		SINGLETON = new PersistenceManager();
	}

	private PersistenceManager() {
		setLoopRate(MONITOR_INTERVAL);
		start();
	}

	@Override
	protected void run() {
		Log.info(PersistenceManager.class.getSimpleName() + "\n" + getStatusReport());
	}

	public static Session openSession() {
		return SESSION_FACTORY.openSession();
	}

	public static void close() {
		Log.info("Closing Hibernate session factory.");
		SESSION_FACTORY.close();
		SINGLETON.stop();

	}

	private String getStatusReport() {
		Statistics stats = SESSION_FACTORY.getStatistics();
		return stats.toString()
				.replace("[", ":\n")
				.replace("]", "")
				.replace(',', '\n')
				.replace("=", ": ");
	}
}
