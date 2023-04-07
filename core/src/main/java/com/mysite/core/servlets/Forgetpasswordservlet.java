package com.mysite.core.servlets;



import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.commons.io.IOUtils;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysite.core.models.Employee;

@Component(service = Servlet.class, property = { Constants.SERVICE_DESCRIPTION + "=Forgot Password Servlet",
		"sling.servlet.methods=" + "POST", "sling.servlet.methods=" + "GET","sling.servlet.paths=" + "/bin/forgotpassword" })
public class Forgetpasswordservlet extends SlingAllMethodsServlet {

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Forgetpasswordservlet.class);
	
    private static final String JSON_FILE_PATH = "/content/dam/mysite/data.json";
    
    private static final String EMAIL_FROM = "secureseatbookingapp@gmail.com";
    private static final String EMAIL_PASSWORD = "vjpzjkilnluacewz";
    private static final String EMAIL_SMTP_HOST = "smtp.gmail.com";
    private static final int EMAIL_SMTP_PORT = 587;
    
    private String receipent_email = "";

    @Override
    protected void doPost(SlingHttpServletRequest req, SlingHttpServletResponse resp)
            throws ServletException, IOException {
        String email = req.getParameter("email");
        receipent_email=email;

        if (email == null || email.isEmpty()) {
        	LOGGER.warn("Please provide a valid email address");
            resp.getWriter().write("Please provide a valid email address");
            return;
        }
        
        if (!isValidEmail(email)) {
        	LOGGER.warn("Invalid email address: {}", email);
        	throw new IllegalArgumentException("Invalid email address: " + email);
        }

        ResourceResolver resolver = req.getResourceResolver();

        Employee userCredentials = null;
        try {
            userCredentials = getUserCredentials(email, resolver);
        } catch (JSONException e) {
            LOGGER.error("Error reading user credentials from JSON: {}", e.getMessage());
            resp.getWriter().write("Error reading user credentials.");
            return;
        }
        
        if (userCredentials != null) {
            String password = userCredentials.getPassword();

            sendPasswordByEmail(userCredentials, password);
            
            LOGGER.info("Password sent to the email address: {}", email);
            
            resp.getWriter().write("Password sent to your email address.");
        } else {
        	
        	LOGGER.warn("Email address not found: {}", email);
        	
            resp.getWriter().write("Email address not found.");
        }
    }
    
    private boolean isValidEmail(String email) {
    	LOGGER.debug("Validating email: {}", email);
        String emailRegex = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    Employee getUserCredentials(String email, ResourceResolver resolver) throws JSONException {
        Resource resource = resolver.getResource(JSON_FILE_PATH);
        if (resource != null) {
            InputStream inputStream = resource.adaptTo(InputStream.class);
            try {
                String jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
                LOGGER.info("Successfully read JSON string from input stream: {}", jsonString);

                JSONObject jsonObject = new JSONObject(jsonString);
                JSONArray usersArray = jsonObject.getJSONArray("users");
                for (int i = 0; i < usersArray.length(); i++) {
                    JSONObject userObject = usersArray.getJSONObject(i);
                    String useremail = userObject.optString("email");
                    if (useremail != null && useremail.equals(email)) {
                        String username = userObject.optString("username");
                        String password = userObject.optString("password");
                        String role = userObject.optString("role");
                        return new Employee(username, password, role, email);
                    }
                }
                LOGGER.warn("No user found with email {}", email);
            } catch (IOException e) {
                LOGGER.error("Error reading user credentials from file: {}", e.getMessage());
            } catch (JSONException e) {
                LOGGER.error("Error parsing JSON object: {}", e.getMessage());
            }
        } else {
            LOGGER.warn("Could not find JSON file at {}", JSON_FILE_PATH);
        }
        return null;
    }


    void sendPasswordByEmail(Employee userCredentials, String password) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.debug", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");


        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_FROM, EMAIL_PASSWORD);
            }
        });

        try {
            if(receipent_email != null && !receipent_email.isEmpty() && receipent_email.equals(userCredentials.getEmail())) {
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL_FROM));
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receipent_email));
                message.setSubject("Your forgotten password");
                message.setText("Your password is: " + password);
                Transport.send(message);
                LOGGER.info("Password sent successfully to email: {}", receipent_email);
            } else {
                LOGGER.warn("Email not sent as recipient email address is either null or empty or does not match the user email address");
            }

        } catch (AddressException e) {
            LOGGER.error("Invalid email address provided: {}", e.getMessage());
        } catch (MessagingException e) {
            LOGGER.error("Failed to send email: {}", e.getMessage());
        }
    }


}