package com.mysite.core.servlets;

import java.io.IOException;

import javax.servlet.Servlet;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysite.core.service.AuthorService;
import com.mysite.core.utils.ServiceUtil;

//@Component(service = Servlet.class)
//@SlingServletResourceTypes(
//		
//        methods = {HttpConstants.METHOD_POST},
//        resourceTypes = Constants.ADDAUTHOR_RESOURCE_TYPE,
//        selectors = {Constants.ADDAUTHOR_SELECTORS},
//        extensions = {Constants.ADDAUTHOR_EXTENSION}
//)

@Component(service = { Servlet.class },
property={"sling.servlet.methods="+ HttpConstants.METHOD_GET,
		"sling.servlet.paths="+ "/bin/servlet" })




public class AuthorServlet extends SlingAllMethodsServlet {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorServlet.class);

    @Reference
    AuthorService authorService;

    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws IOException {
        try {
            String resp=authorService.createAuthorNode(ServiceUtil.getCountry(request),request);
            response.getWriter().write(resp);
        }
        catch (Exception e){
            LOG.info("\n ERROR IN REQUEST {} ",e.getMessage());
        }
    }

}