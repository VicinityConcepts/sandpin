package com.vc.sandpin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PersistentObject {
	protected static final Logger LOG = LogManager.getLogger();

	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "sys_uuid", updatable = false, nullable = false, length = 32)
	private String uuid;

	public PersistentObject() {
	}

	public String getUUID() {
		return uuid;
	}

	public final void save() {
		Transaction tx = null;
		try (Session session = PersistenceManager.openSession()) {
			tx = session.beginTransaction();
			session.save(this);
			tx.commit();
		} catch (Exception e) {
			LOG.error("Something went wrong during database session. Transaction has been rolled back.", e);
			if (tx != null) tx.rollback();
		}
	}
}
