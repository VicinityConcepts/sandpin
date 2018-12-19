package com.vc.sandpin.launcher;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.CatalinaBaseConfigurationSource;
import org.apache.catalina.startup.Tomcat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class TomcatContainer {
	//private static final Logger LOG = LogManager.getLogger();
	private static final String CATALINA_HOME = "tomcat";
	private static final String SERVER_XML_PATH = "server.xml";
	//private static final String WAR_FILE_EXTENSION = ".war";
	private static final String PROPERTY_CATALINA_HOME = "catalina.home";

	private final Tomcat tomcat;

	public TomcatContainer(/*File warFile*/) /*throws ServletException*/ {
		System.setProperty(PROPERTY_CATALINA_HOME, CATALINA_HOME);
		tomcat = new Tomcat();
		tomcat.init(new CatalinaBaseConfigurationSource(new File(CATALINA_HOME), SERVER_XML_PATH));
	}

	/*private void initialize(File warFile) throws ServletException {

		//tomcat.setSilent(true);
		//tomcat.setPort(PORT);
		//tomcat.getHost().setAppBase(WEBAPP_PATH);

		// Tomcat 9 no longer automatically adds connectors, so this is necessary
		// in order to trigger the creation of the default connector.
		//tomcat.getConnector();

		/*StandardContext context = (StandardContext) tomcat.addWebapp("", new File(WEBAPP_PATH).getAbsolutePath());
		if (warFile == null || warFile.isDirectory()) {
			LOG.info("Searching for WAR file.");
			if (warFile == null) warFile = findWarFile(new File(WEBAPP_PATH));
			else warFile = findWarFile(warFile);
			if (warFile == null) throw new ServletException("Failed to locate WAR file.");
		}

		LOG.info("Using WAR file: " + warFile.getAbsolutePath());
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
	}*/

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