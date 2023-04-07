package com.mysite.core.servlets;



import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(service = Servlet.class, property = { "sling.servlet.methods=GET",
		"sling.servlet.paths=/bin/username", "sling.servlet.extensions=json",
		"sling.servlet.selectors=myservlet" })
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
			throws ServletException, IOException {
		ResourceResolver resourceResolver = null;
		InputStream inputStream = null;
		try {
			//resourceResolver = resourceResolverFactory.getServiceResourceResolver(null);
			resourceResolver = resourceResolverFactory.getServiceResourceResolver(null);
			String jsonFilePath = "/content/dam/mysite/data.json"; // path to your JSON file
			inputStream = resourceResolver.getResource(jsonFilePath).adaptTo(InputStream.class);
			String jsonString = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
			JSONArray jsonArray = new JSONArray(jsonString);
			List<String> usernames = new ArrayList<String>();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String username = jsonObject.getString("username");
				usernames.add(username);
			}
			response.setContentType("application/json");
			response.getWriter().write(usernames.toString());
		} catch (JSONException | LoginException e) {
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
		} finally {
			if (resourceResolver != null) {
				resourceResolver.close();
			}
			IOUtils.closeQuietly(inputStream);
		}
	}
}

