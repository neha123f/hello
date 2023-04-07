package com.mysite.core.servlets;



import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.json.JSONArray;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;


@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Login Servlet",
        "sling.servlet.methods=" + "POST", "sling.servlet.methods=" + "GET","sling.servlet.paths=" + "/bin/v" })

//@WebServlet(name = "MyServlet", resourceTypes = "sling/servlet/default", selectors = "data", extensions = "json", methods = HttpConstants.METHOD_GET)
public class profile extends SlingSafeMethodsServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String username = request.getParameter("username");

        InputStream inputStream = null;
        try {
            inputStream = request.getResourceResolver().getResource("/content/dam/mysite/employees.json").adaptTo(InputStream.class);
            String jsonString = IOUtils.toString(inputStream, "UTF-8");
            JSONArray jsonArray = new JSONArray(jsonString);

            JSONObject userJson = null;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.getString("username").equals(username)) {
                    userJson = json;
                    break;
                }
            }

            if (userJson != null) {
                JSONObject resultJson = new JSONObject();
                resultJson.put("username", userJson.getString("username"));
                resultJson.put("password", userJson.getString("password"));
                resultJson.put("role", userJson.getString("role"));
                resultJson.put("email", userJson.getString("email"));
//                resultJson.put("useremail", userJson.getString("useremail"));
//                resultJson.put("Phone Number", userJson.getString("Phone Number"));
                
                response.getWriter().write(resultJson.toString());
                
            
            } else {
                response.getWriter().write("[{,}]");
            }

        } catch (Exception e) {
            throw new ServletException("Error reading JSON data", e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }
}
