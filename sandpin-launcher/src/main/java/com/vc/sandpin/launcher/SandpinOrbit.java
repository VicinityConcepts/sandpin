package com.vc.sandpin.launcher;

import com.vc.lib.util.Service;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SandpinOrbit extends Service implements LifecycleListener {
	private static final Logger LOG = LogManager.getLogger();
	private static final int SERVICE_UPDATE_FREQUENCY = 1000 * 60;
	private static final int SERVICE_RESTART_MAX_ATTEMPTS = 5;

	private TomcatContainer tomcat;

	public SandpinOrbit() {
		setLoopRate(SERVICE_UPDATE_FREQUENCY);
	}

	@Override
	public boolean start() {
		boolean success = super.start();
		if (tomcat == null) {
			try {
				tomcat = new TomcatContainer(this);
				tomcat.start();
				return success;
			} catch (LifecycleException e) {
				LOG.error("Failed to start Tomcat container.", e);
				stop();
				return false;
			}
		} else return success;
	}

	@Override
	public boolean stop() {
		boolean success = super.stop();
		if (tomcat != null) {
			try {
				tomcat.stop();
				tomcat = null;
				return success;
			} catch (LifecycleException e) {
				LOG.error("Failed to stop Tomcat container.", e);
				return false;
			}
		} else return success;
	}

	@Override
	public void restart() {
		int attempts = 1;
		while (attempts <= SERVICE_RESTART_MAX_ATTEMPTS) {
			LOG.info("Restarting orbit. (Attempt " + attempts + ")");
			try {
				super.restart();
				return;
			} catch (Exception e) {
				LOG.error("Service encountered an error during restart.", e);
				attempts++;
			}
		}
	}

	@Override
	protected void run() {
	}

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		LOG.info("Lifecycle Event: " + event.getLifecycle().getStateName() + " (" + event.getType() + ")");
	}
}
