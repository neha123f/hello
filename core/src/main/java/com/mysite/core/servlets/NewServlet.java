package com.mysite.core.servlets;
import java.io.IOException;
import java.io.PrintWriter;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.ServletException;


import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import com.adobe.internal.util.UUID;
import com.day.cq.commons.jcr.JcrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.sling.commons.json.JSONArray;

import org.apache.sling.commons.json.JSONException;

import org.apache.sling.commons.json.JSONObject;

 
@SlingServlet(methods = {"POST"}, paths = "/bin/getData", generateComponent = true)

@SuppressWarnings({ "serial", "unused" })


@Component(service = { Servlet.class },
property={"sling.servlet.methods="+ HttpConstants.METHOD_GET,
		"sling.servlet.paths="+ "/bin/getData" })



public class NewServlet extends SlingAllMethodsServlet {


private static final String CREATE_PATH = "/content"; /// Set the base path here




@Reference
private SlingRepository repository;

//@Override
//protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//    ResourceResolver resourceResolver = request.getResourceResolver();
//    Session session = resourceResolver.adaptTo(Session.class);
//    
//}

@Override
protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)throws ServletException, IOException {
    ResourceResolver resourceResolver = request.getResourceResolver();
    Resource nodeResource = resourceResolver.getResource("/content/bookseat/"); // Replace// path
    ValueMap nodeProperties = nodeResource.adaptTo(ValueMap.class); 
    JSONObject json = new JSONObject(nodeProperties);
    String jsonString = json.toString();
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(jsonString);
}




protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
    ResourceResolver resourceResolver = request.getResourceResolver();
    Session session = resourceResolver.adaptTo(Session.class);
 
    String type=request.getParameter("type");
    String to = request.getParameter("to");
    String from = request.getParameter("from"); 
    String shift = request.getParameter("shift"); 
    String food = request.getParameter("food");
    String floor = request.getParameter("floor");
    PrintWriter out = response.getWriter();
    try {
    	Node firstProduct = JcrUtil.createPath(CREATE_PATH + "/bookseat"  , "cq:Page", session);
        String nodeName = "subproduct_" + System.currentTimeMillis();
        Node rootNode = JcrUtil.createPath(firstProduct.getPath() + "/" + nodeName, "nt:unstructured", session);
        rootNode.setProperty("type", type);
        rootNode.setProperty("to", to);
        rootNode.setProperty("from", from);
        rootNode.setProperty("shift", shift);
        rootNode.setProperty("food", food);
        rootNode.setProperty("floor", floor);
        session.save();
        String redirectPath = "/content/UserHomePage.html"; //Replace with your actual page path
        response.sendRedirect(redirectPath);
       // response.getWriter().write("Node created successfully!");
    } catch (RepositoryException e) {
        response.getWriter().write("Error creating node: " + e.getMessage());
    } finally {
        //Clean up resources
        if(session != null) {
            session.logout();
        }
        if(resourceResolver != null) {
            resourceResolver.close();
        }
    }
}
}







