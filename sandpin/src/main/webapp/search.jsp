<%@ page import="com.vc.sandpin.test.*" %>

<html>
	<body>
		<%
			String firstName = request.getParameter("first_name");
    		String lastName = request.getParameter("last_name");
			Employee e = EmployeeManager.getEmployee(firstName, lastName);
		%>
		<% if (e != null) { %>
			<h3>Success!</h3>
			<p><%= String.format("%s %s (%d)", e.getFirstName(), e.getLastName(), e.getSalary()) %></p>
		<% } else { %>
			<h3>Failed</h3>
			<p>Employee <%= String.format("%s %s", firstName, lastName) %> not found.</p>
		<% } %>
	</body>
</html>