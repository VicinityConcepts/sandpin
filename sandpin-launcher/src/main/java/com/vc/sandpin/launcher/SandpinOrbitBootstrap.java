package com.vc.sandpin.launcher;

import com.vc.lib.cmd.Terminal;
import com.vc.lib.cmd.TerminalCommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

public class SandpinOrbitBootstrap {
	private static final Logger LOG;
	private static final String ARG_INTERACTIVE = "^-i$";
	private static final String ARG_WAR_FILE = "^-war=.*$";

	static {
		// Property must be set before any LogManager calls
		System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
		LOG = LogManager.getLogger();
	}

	private static boolean interactive = false;
	private static File warFile = null;

	public static void main(String[] args) {
		for (String arg : args) parseArg(arg);

		try {
			SandpinOrbit orbit = new SandpinOrbit();
			if (interactive) runInTerminal(orbit);
			else orbit.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void parseArg(String arg) {
		if (arg.matches(ARG_INTERACTIVE)) interactive = true;
		else if (arg.matches(ARG_WAR_FILE)) parseWarFile(arg.substring(arg.indexOf('=') + 1));
		else {
			LOG.error("Unrecognized argument: {}", arg);
			System.exit(1);
		}
	}

	private static void parseWarFile(String path) {
		warFile = new File(path);
		if (!warFile.exists()) {
			LOG.error("Specified file/directory does not exist: {}", warFile.getAbsolutePath());
			System.exit(1);
		} else if (!warFile.isDirectory()) {
			if (!warFile.getName().endsWith(".war")) {
				LOG.error("Not a valid WAR file: {}", warFile.getName());
				System.exit(1);
			}
		}
	}

	private static void runInTerminal(SandpinOrbit orbit) {
		Terminal terminal = new Terminal();
		terminal.addOrReplaceCommand(new TerminalCommand("restart", orbit::restart, "Stop, reinitialize, and start the SandpinOrbit service."));
		terminal.attach(orbit);
	}
}
