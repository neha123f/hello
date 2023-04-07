package com.mysite.core.servlets;



import com.google.gson.Gson; 
import com.google.gson.reflect.TypeToken;
import com.mysite.core.models.Employee;
import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Change Password Servlet",
		"sling.servlet.methods=" + "POST", "sling.servlet.paths=" + "/bin/newchangepassword" })

public class NewChangePassword extends SlingAllMethodsServlet {
	private static final long serialVersionUID = -6587862240235618977L;
	private static final String JSON_PATH = "/content/dam/mysite/employees.json";

	@Override
	protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession();
		String username = (String) session.getAttribute("username");
		String currentPassword = request.getParameter("otp");
		String newPassword = request.getParameter("password");

		// Get a ResourceResolver to access the JSON file

		ResourceResolver resourceResolver = request.getResourceResolver();

		// Retrieving the JSON file as a Resource object
		Resource jsonResource = resourceResolver.getResource(JSON_PATH);
		if (jsonResource == null) {
			response.sendError(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JSON file not found");
			return;
		}

		InputStream stream = jsonResource.adaptTo(InputStream.class);
		String jsonString = IOUtils.toString(new InputStreamReader(stream));
		Gson gson = new Gson();
		List<Employee> users = gson.fromJson(jsonString, new TypeToken<List<Employee>>() {
		}.getType());
		Boolean changed = false;
		// Find the employee with the matching username and password
		for (Employee user : users) {
			if (user.getUsername().equals(username) && !user.getRole().equals("admin")
					&& user.getPassword().equals(currentPassword)) {
				user.setPassword(newPassword);
				changed = true;
				if (changed == true) {
					String updatedUser = gson.toJson(users);
					String json_path = "/content/dam/mysite/employees.json/jcr:content";
					Resource resourceee = resourceResolver.getResource(json_path);
					ModifiableValueMap map = resourceee.adaptTo(ModifiableValueMap.class);
					map.put("jcr:data", updatedUser);
					resourceResolver.commit();
					response.sendRedirect("/content/changepasssuccess.html");
				}
			}
		}
		if (changed == false) {
			response.sendRedirect("/content/changepassword.html");
		}
	}
}
