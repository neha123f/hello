package com.mysite.core.servlets;

//@Component(service = Servlet.class,
//property = {
//        Constants.SERVICE_DESCRIPTION + "=Sample Sling Servlet",
//        "sling.servlet.methods=" + HttpConstants.METHOD_GET,
//        "sling.servlet.paths=" + "/bin/sample"
//})


import java.io.IOException;
import java.io.InputStream;
import java.util.List;


import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//
//import org.apache.sling.api.SlingHttpServletRequest;
//import org.apache.sling.api.SlingHttpServletResponse;
//import org.apache.sling.api.resource.Resource;
//import org.apache.sling.api.resource.ResourceResolver;
//import org.apache.sling.api.servlets.HttpConstants;
//import org.apache.sling.api.servlets.SlingAllMethodsServlet;
//import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
//import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysite.core.models.Employee;

@Component(service = Servlet.class, property = {
"sling.servlet.methods=GET",
 "sling.servlet.paths=/bin/viewprofile"
  })
  public class ViewProfileServlet extends SlingSafeMethodsServlet {
   @Override
  protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
  // Load the JSON file
   ResourceResolver resolver = request.getResourceResolver();
   Resource profileDataResource = resolver.getResource("/content/dam/mysite/employees.json");
   InputStream jsonStream = profileDataResource.adaptTo(InputStream.class);
  
   // Parse the JSON data
   ObjectMapper objectMapper = new ObjectMapper();
  List<Employee> profiles = objectMapper.readValue(jsonStream, new TypeReference<List<Employee>>() {});
  
  // Set the data as a request attribute
  request.setAttribute("profiles", profiles);
  
  // Forward to the view
 RequestDispatcher dispatcher = request.getRequestDispatcher("/content/viewprofile.html");
  dispatcher.forward(request, response);
   }
  }
