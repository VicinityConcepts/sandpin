package com.vc.sandpin.launcher;

import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.startup.CatalinaBaseConfigurationSource;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class TomcatContainer {
	private static final String CATALINA_HOME = "tomcat";
	private static final String SERVER_XML_PATH = "server.xml";
	private static final String PROPERTY_CATALINA_HOME = "catalina.home";

	private final Tomcat tomcat;

	public TomcatContainer(LifecycleListener listener) {
		System.setProperty(PROPERTY_CATALINA_HOME, CATALINA_HOME);
		tomcat = new Tomcat();
		tomcat.init(new CatalinaBaseConfigurationSource(new File(CATALINA_HOME), SERVER_XML_PATH));
		tomcat.getServer().addLifecycleListener(listener);
	}

	public void start() throws LifecycleException {
		tomcat.start();
	}

	public void stop() throws LifecycleException {
		tomcat.stop();
		tomcat.destroy();
	}
}