package com.mysite.core.servlets;
import com.day.cq.commons.jcr.JcrConstants;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletResourceTypes;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;

import org.apache.sling.api.request.RequestParameter;


/**
 * Servlet that writes some sample content into the response. It is mounted for
 * all resources of a specific Sling resource type. The
 * {@link SlingSafeMethodsServlet} shall be used for HTTP methods that are
 * idempotent. For write operations use the {@link SlingAllMethodsServlet}.
 */
@Component(service = { Servlet.class },
property={"sling.servlet.methods="+ HttpConstants.METHOD_GET,
		"sling.servlet.paths="+ "/bin/servlet" }

)

@ServiceDescription("Simple Demo Servlet")
public class TestServlet extends SlingAllMethodsServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG=LoggerFactory.getLogger(TestServlet.class);

    @Override
    protected void doGet(final SlingHttpServletRequest req,
            final SlingHttpServletResponse resp) throws ServletException, IOException {
        final Resource resource = req.getResource();
        resp.setContentType("text/plain");
        resp.getWriter().write("Title = " + resource.getValueMap().get(JcrConstants.JCR_TITLE));
    }
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
//    	SlingHttpSession session = request.getSession();
//    	String username = request.getParameter("username");
//    	String password = request.getParameter("password");
    	 try {
    		 
             LOG.info("\n ------------------------STARTED POST-------------------------");
             List<RequestParameter> requestParameterList=request.getRequestParameterList();
             for(RequestParameter requestParameter : requestParameterList){
                 LOG.info("\n ==PARAMETERS===>  {} : {} ",requestParameter.getName(),requestParameter.getString());
             }
         }catch (Exception e){
             LOG.info("\n ERROR IN REQUEST {} ",e.getMessage());
         }
  //response.sendRedirect("/content/admin.html");       }
     response.getWriter().write("======FORM SUBMITTED========");


}
}

