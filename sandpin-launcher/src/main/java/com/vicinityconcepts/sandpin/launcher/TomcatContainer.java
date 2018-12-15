package com.vicinityconcepts.sandpin.launcher;

import com.vicinityconcepts.lib.util.Log;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.catalina.webresources.WarResourceSet;

import javax.servlet.ServletException;
import java.io.File;

public class TomcatContainer {
	private static final int PORT = 8080;
	private static final String WEBAPP_PATH = System.getProperty("user.dir");
	private static final String CATALINA_HOME = "tomcat";
	private static final String WAR_FILE_EXTENSION = ".war";
	private static final String PROPERTY_CATALINA_HOME = "catalina.home";
	private static final String PROPERTY_CATALINA_BASE = "catalina.base";
	private static final String PROPERTY_CATALINA_LOG = "catalina.out";

	private final Tomcat tomcat;

	public TomcatContainer(File warFile) throws ServletException {
		String catalinaHomePath = new File(CATALINA_HOME).getAbsolutePath();
		System.setProperty(PROPERTY_CATALINA_HOME, catalinaHomePath);
		System.setProperty(PROPERTY_CATALINA_BASE, catalinaHomePath);
		System.setProperty(PROPERTY_CATALINA_LOG, catalinaHomePath);
		tomcat = new Tomcat();
		initialize(warFile);
	}

	private void initialize(File warFile) throws ServletException {
		tomcat.setSilent(true);
		tomcat.setPort(PORT);
		tomcat.getHost().setAppBase(WEBAPP_PATH);
		StandardContext context = (StandardContext) tomcat.addWebapp("", new File(WEBAPP_PATH).getAbsolutePath());

		if (warFile == null || warFile.isDirectory()) {
			Log.info("Searching for WAR file.");
			if (warFile == null) warFile = findWarFile(new File(WEBAPP_PATH));
			else warFile = findWarFile(warFile);
			if (warFile == null) throw new ServletException("Failed to locate WAR file.");
		}

		Log.info("Using WAR file: " + warFile.getAbsolutePath());
		WebResourceRoot resources = new StandardRoot(context);
		resources.addPreResources(new WarResourceSet(resources, "/", warFile.getAbsolutePath()));
		context.setResources(resources);
	}

	private File findWarFile(File directory) {
		File[] warFiles = directory.listFiles((d, n) -> n.endsWith(WAR_FILE_EXTENSION));
		if (warFiles != null && warFiles.length > 0) return warFiles[0];
		else {
			File[] subdirectories = directory.listFiles((d, n) -> new File(d, n).isDirectory());
			if (subdirectories != null) {
				for (File d : subdirectories) {
					File result = findWarFile(d);
					if (result != null) return result;
				}
			}
		}

		return null;
	}

	public void start() throws LifecycleException {
		tomcat.start();
	}

	public void stop() throws LifecycleException {
		tomcat.stop();
		tomcat.destroy();
	}

	public void addLifecycleListener(LifecycleListener listener) {
		tomcat.getServer().addLifecycleListener(listener);
	}
}