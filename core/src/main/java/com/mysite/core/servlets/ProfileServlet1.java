package com.mysite.core.servlets;



import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Servlet;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;


@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Login Servlet",
        "sling.servlet.methods=" + "POST", "sling.servlet.methods=" + "GET","sling.servlet.paths=" + "/bin/view" })

public class ProfileServlet1 extends SlingAllMethodsServlet  {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) {
        try {
            response.setContentType("application/json");

            // Get the current date
            Calendar calendar = Calendar.getInstance();
            String currentDate = String.format("%04d-%02d-%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH));

            // Get a resource resolver
            ResourceResolver resolver = request.getResourceResolver();

            // Get the JSON data from a JCR node, assuming it's stored at /content/myproject/data.json
            Resource dataResource = resolver.getResource("/content/dam/mysite/data.json");
            String jsonData = dataResource.getValueMap().get("jsondata", String.class);

            // Parse the JSON data
            JSONArray users = new JSONArray(jsonData);
            int lunchOptedCount = 0;
            for (int i = 0; i < users.length(); i++) {
                JSONObject user = users.getJSONObject(i);
                if (user.has("bookings")) {
                    JSONArray bookings = user.getJSONArray("bookings");
                    for (int j = 0; j < bookings.length(); j++) {
                        JSONObject booking = bookings.getJSONObject(j);
                        if (booking.getString("fromdate").equals(currentDate) && booking.getBoolean("lunch_opted")) {
                            lunchOptedCount++;
                        }
                    }
                }
            }

            // Return the lunch opted user count as a JSON object
            JSONObject result = new JSONObject();
            result.put("lunch_opted_count", lunchOptedCount);
            response.getWriter().write(result.toString());
        } catch (IOException | JSONException  e) {
            // Handle the exception here
        	 e.printStackTrace();
        }
    }

    }

    

    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
//        @Override
//        protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//            List<User> users = getUsers();
//            long count = users.stream()
//                    .filter(user -> user.getBookings() != null)
//                    .flatMap(user -> user.getBookings().stream())
//                    .filter(booking -> booking.isLunchOpted() && booking.getFromDate().equals(LocalDate.now()))
//                    .count();
//            response.getWriter().println("Number of users who have opted for lunch today: " + count);
//        }
//
//        private List<User> getUsers() {
//            ObjectMapper objectMapper = new ObjectMapper();
//            List<User> users = null;
//            try {
//                // read the JSON file and convert it to a list of User objects
//                users = objectMapper.readValue(new File("/content/dam/mysite/data.json"), new TypeReference<List<User>>(){});
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            return users;
//        }
//    }

    
    

    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    










    

//    @Override
//    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
//            throws ServletException, IOException {
//
//        // Get the path to the JSON file
//        String filePath = "/content/dam/mysite/employees.json";
//
//        try {
//            // Get the input stream for the file
//            InputStream inputStream = request.getResourceResolver().getResource(filePath).adaptTo(InputStream.class);
//
//            // Read the contents of the file into a string
//            String jsonText = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
//
//            // Create a JSONObject from the string
//            JSONObject jsonObject = new JSONObject(jsonText);
//
//            // Get the values you need from the JSONObject
//            String username = jsonObject.getString("username");
//            String password = jsonObject.getString("password");
//            String role = jsonObject.getString("role");
//
//            // Output the values as HTML
//            response.setContentType("text/html");
//            response.getWriter().write("<p>Username: " + username + "</p>");
//            response.getWriter().write("<p>Password: " + password + "</p>");
//            response.getWriter().write("<p>Role: " + role + "</p>");
//        } catch (Exception e) {
//            // Handle any exceptions that occur
//            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            response.getWriter().write("Error: " + e.getMessage());
//        }
//    }

