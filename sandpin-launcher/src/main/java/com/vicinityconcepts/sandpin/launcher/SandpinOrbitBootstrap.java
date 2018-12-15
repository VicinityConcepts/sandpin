package com.vicinityconcepts.sandpin.launcher;

import com.vicinityconcepts.lib.cmd.Terminal;
import com.vicinityconcepts.lib.cmd.TerminalCommand;

import java.io.File;

public class SandpinOrbitBootstrap {
	private static final String ARG_INTERACTIVE = "^-i$";
	private static final String ARG_WAR_FILE = "^-war=.*$";

	private static boolean interactive = false;
	private static File warFile = null;

	public static void main(String[] args) {
		for (String arg : args) parseArg(arg);

		try {
			SandpinOrbit orbit = new SandpinOrbit(warFile);
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
			System.err.println("Unrecognized argument: " + arg);
			System.exit(1);
		}
	}

	private static void parseWarFile(String path) {
		warFile = new File(path);
		if (!warFile.exists()) {
			System.err.println("Specified file/directory does not exist: " + warFile.getAbsolutePath());
			System.exit(1);
		} else if (!warFile.isDirectory()) {
			if (!warFile.getName().endsWith(".war")) {
				System.err.println("Not a valid WAR file: " + warFile.getName());
				System.exit(1);
			}
		}
	}

	private static void runInTerminal(SandpinOrbit orbit) {
		Terminal terminal = new Terminal();
		terminal.addOrReplaceCommand(new TerminalCommand("start", orbit::start, "Start the SandpinOrbit service."));
		terminal.addOrReplaceCommand(new TerminalCommand("stop", orbit::stop, "Stop the SandpinOrbit service."));
		terminal.addOrReplaceCommand(new TerminalCommand("restart", orbit::restart, "Stop, reinitialize, and start the SandpinOrbit service."));
		terminal.attach(orbit);
	}
}
