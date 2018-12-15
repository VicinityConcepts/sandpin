package com.vicinityconcepts.sandpin.launcher;

import com.vicinityconcepts.lib.util.Log;
import com.vicinityconcepts.lib.util.Service;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;

import javax.servlet.ServletException;
import java.io.File;

public class SandpinOrbit extends Service implements LifecycleListener {
	private static final int SERVICE_UPDATE_FREQUENCY = 1000 * 60;
	private static final int SERVICE_RESTART_MAX_ATTEMPTS = 5;

	private TomcatContainer tomcat;
	private final File warFile;

	public SandpinOrbit(File warFile) {
		this.warFile = warFile;
		setLoopRate(SERVICE_UPDATE_FREQUENCY);
	}

	@Override
	public void start() {
		super.start();
		if (tomcat == null) {
			try {
				tomcat = new TomcatContainer(warFile);
				tomcat.addLifecycleListener(this);
				tomcat.start();
			} catch (ServletException | LifecycleException e) {
				Log.error("Failed to start Tomcat container.", e);
				stop();
			}
		}
	}

	@Override
	public void stop() {
		super.stop();
		if (tomcat != null) {
			try {
				tomcat.stop();
				tomcat = null;
			} catch (LifecycleException e) {
				Log.error("Failed to stop Tomcat container.", e);
			}
		}
	}

	@Override
	public void restart() {
		int attempts = 1;
		while (attempts <= SERVICE_RESTART_MAX_ATTEMPTS) {
			Log.info("Restarting orbit. (Attempt " + attempts + ")");
			try {
				super.restart();
				return;
			} catch (Exception e) {
				Log.error("Service encountered an error during restart.", e);
				attempts++;
			}
		}
	}

	@Override
	protected void run() {
	}

	@Override
	public void lifecycleEvent(LifecycleEvent event) {
		Log.info("Lifecycle Event: " + event.getLifecycle().getStateName() + " (" + event.getType() + ")");
	}
}
