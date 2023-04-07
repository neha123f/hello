package com.mysite.core.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mysite.core.models.Employee;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Login Servlet",
        "sling.servlet.methods=" + "POST", "sling.servlet.methods=" + "GET","sling.servlet.paths=" + "/bin/dopost" })



public class loginServlet extends SlingAllMethodsServlet{
	private static final long serialVersionUID = -6587862240235618977L;
	
	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
	        throws ServletException, IOException {

	    // Get the user's entered credentials from the request
	    String username = request.getParameter("username");
	    String password = request.getParameter("password");

	    // Load the employee data from the JSON file
	    ResourceResolver resolver = request.getResourceResolver();
	    Resource resource = resolver.getResource("/content/dam/mysite/employees.json");
	    InputStream stream = resource.adaptTo(InputStream.class);
	    String jsonString = IOUtils.toString(new InputStreamReader(stream));
	    Gson gson = new Gson();
	    List<Employee> employees = gson.fromJson(jsonString, new TypeToken<List<Employee>>() {}.getType());

	    // Find the employee with the matching username and password
	    Employee matchedEmployee = null;
	    for (Employee employee : employees) {
	        if (employee.getUsername().equals(username) && employee.getPassword().equals(password)) {
	            matchedEmployee = employee;
	            break;
	        }
	    }

	    // If the entered credentials match an employee, redirect to the appropriate home page based on the user's role
	    if (matchedEmployee != null) {
	        HttpSession session = request.getSession();
	        session.setAttribute("username", username);
	        session.setAttribute("employee", matchedEmployee);

	        // Redirect to the appropriate home page based on the user's role
	        String redirectUrl = "/content/";
	        if (matchedEmployee.getRole().equals("user")) {
	            redirectUrl += "UserHomePage.html?username=" + matchedEmployee.getUsername() + "&role=user";
	        } else if (matchedEmployee.getRole().equals("admin")) {
	            redirectUrl += "demo0.html?username=" + matchedEmployee.getUsername() + "&role=admin";
	        }
	        response.sendRedirect(redirectUrl);
	    } else {
	        // If no matching employee is found, display an error message on the login page
	        request.setAttribute("error", "Invalid username or password");
//	        request.getRequestDispatcher("/content/Login.html").forward(request, response);
	        response.sendRedirect("/content/Login.html?error=Invalid%20username%20or%20password");
	    }

	}

}
