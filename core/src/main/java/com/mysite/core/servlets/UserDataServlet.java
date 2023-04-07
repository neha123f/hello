package com.mysite.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Login Servlet",
        "sling.servlet.methods=" + "POST", "sling.servlet.methods=" + "GET","sling.servlet.paths=" + "/bin/nodeinfo" })


public class UserDataServlet extends SlingAllMethodsServlet {
	
	private static final long serialVersionUID = -6587862240235618977L;
	
@Override
protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
        throws ServletException, IOException {

    // Get the logged-in user's username from the session
    HttpSession session = request.getSession();
    String username = (String) session.getAttribute("username");

    // Get the ResourceResolver and retrieve the node under the logged-in user's home directory
    ResourceResolver resolver = request.getResourceResolver();
    String nodePath = "/content/bookseat/" + username + "/" + username;
    Resource nodeResource = resolver.getResource(nodePath);

    // Check if the node exists and retrieve its properties
    if (nodeResource != null) {
        ValueMap properties = nodeResource.getValueMap();

        // Set the properties as a request attribute
        request.setAttribute("type", properties.get("type", ""));
        request.setAttribute("from", properties.get("from", ""));
        request.setAttribute("to", properties.get("to", ""));
        request.setAttribute("floor", properties.get("floor", ""));
        request.setAttribute("food", properties.get("food", ""));
        request.setAttribute("shift", properties.get("shift", ""));
        request.setAttribute("status", properties.get("status", ""));
    }

    // Forward the request to the component that renders the node data
    request.getRequestDispatcher("/path/to/component").forward(request, response);
}
}
