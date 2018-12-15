package com.vicinityconcepts.sandpin.test;

import com.vicinityconcepts.sandpin.PersistenceManager;
import com.vicinityconcepts.lib.util.Log;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeeManager {

	public static Employee[] getEmployees() {
		Transaction tx = null;
		try (Session session = PersistenceManager.openSession()) {
			tx = session.beginTransaction();
			List employees = session.createQuery("FROM Employee").list();
			tx.commit();
			return (Employee[]) employees.toArray(new Employee[0]);
		} catch (Exception e) {
			Log.error("Something went wrong during database session. Transaction has been rolled back.", e);
			if (tx != null) tx.rollback();
			return null;
		}
	}
}
