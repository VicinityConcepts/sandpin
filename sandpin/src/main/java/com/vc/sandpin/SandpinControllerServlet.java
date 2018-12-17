package com.vc.sandpin;

import com.vc.sandpin.test.EmployeeManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@WebServlet(
		name = "SandpinController",
		description = "Main Sandpin back end controller",
		urlPatterns = {"*.do"},
		loadOnStartup = 0
)
public class SandpinControllerServlet extends HttpServlet {
	private static final Logger LOG = LogManager.getLogger();
	private static final String QUERY_DELIMITER = "&";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		LOG.info("Request received from {}", req.getRemoteAddr());
		byte[] response = parseAndProcess(req);
		try (ServletOutputStream out = resp.getOutputStream()) {
			out.write(response);
			out.flush();
		}
	}

	private byte[] parseAndProcess(HttpServletRequest request) {
		String uri = request.getRequestURI().substring(1);
		if (uri.substring(0, uri.indexOf(".do")).equals("db")) {
			return Arrays.toString(EmployeeManager.getEmployees()).getBytes();
		}
		String queryString = request.getQueryString();
		String[] queries = (queryString != null && !queryString.isEmpty()) ? request.getQueryString().split(QUERY_DELIMITER) : new String[0];
		return ("URI: " + uri + "\nQUERY (" + queries.length + "): " + Arrays.toString(queries)).getBytes();
	}
}