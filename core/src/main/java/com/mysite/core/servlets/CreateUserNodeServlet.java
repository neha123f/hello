package com.mysite.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;

import org.apache.jackrabbit.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;





@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Login Servlet",
        "sling.servlet.methods=" + "POST", "sling.servlet.methods=" + "GET","sling.servlet.paths=" + "/bin/seatbook" })



public class CreateUserNodeServlet extends SlingAllMethodsServlet {

    private static final long serialVersionUID = -6587862240235618977L;
//    public static final String PRIMARY_TYPE = "jcr:primaryType";
    
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        // Get the logged-in user's username from the session
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");

        // Get the information to store in the node from the request parameters
        String type = request.getParameter("type");
        String from = request.getParameter("from");
        String to = request.getParameter("to");
        String floor = request.getParameter("floor");
        String food = request.getParameter("food");
        String shift = request.getParameter("shift");
        String status = request.getParameter("status");

        // Get the ResourceResolver and create a node under the logged-in user's home directory
        ResourceResolver resolver = request.getResourceResolver();
        String homePath = "/content/bookseat/" + username;
        Resource homeResource = resolver.getResource(homePath);
        if (homeResource == null) {
            // If the user's home directory doesn't exist, create it
            Map<String, Object> properties = new HashMap<>();
            properties.put(JcrConstants.JCR_PRIMARYTYPE, "nt:unstructured");
            homeResource = resolver.create(resolver.getResource("/content/bookseat"), username, properties);
        }
        
        String nodeName = username;
        Node parentNode = homeResource.adaptTo(Node.class);
        try {
            Node newNode = parentNode.addNode(nodeName);
            newNode.setProperty("type", type);
            newNode.setProperty("from", from);
            newNode.setProperty("to", to);
            newNode.setProperty("floor", floor);
            newNode.setProperty("food", food);
            newNode.setProperty("shift", shift);
            newNode.setProperty("status", status);
            resolver.commit();
        } catch (RepositoryException e) {
            resolver.refresh();
            throw new ServletException("Failed to create node", e);
        }

        // Redirect to the user's home page
//        response.sendRedirect("/content/UserHomePage.html?username=" + username + "&role=user");
        String redirectUrl = "/content/mylogin.html?type=" + type + "&from=" + from + "&to=" + to + "&floor=" + floor + "&food=" + food + "&shift=" + shift + "&status=" + status;
//        response.sendRedirect("/content/mylogin.html");
        response.sendRedirect(redirectUrl);
    }

    
}