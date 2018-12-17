package com.vc.sandpin.test;

import com.vc.sandpin.PersistenceManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeManager {
	private static final Logger LOG = LogManager.getLogger();

	public static Employee[] getEmployees() {
		Transaction tx = null;
		try (Session session = PersistenceManager.openSession()) {
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM Employee").list();
			tx.commit();
			return (Employee[]) employees.toArray(new Employee[0]);
		} catch (Exception e) {
			LOG.error("Something went wrong during database session. Transaction has been rolled back.", e);
			if (tx != null) tx.rollback();
			return null;
		}
	}
}
