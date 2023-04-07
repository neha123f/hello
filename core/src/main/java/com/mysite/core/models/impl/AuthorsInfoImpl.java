package com.mysite.core.models.impl;

import com.day.cq.wcm.api.Page;
import com.mysite.core.models.AddAuthor;
import com.mysite.core.models.AuthorsInfo;
import com.mysite.core.service.AuthorService;
import com.mysite.core.service.UtilService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = AuthorsInfo.class,
        resourceType = AuthorsInfoImpl.RESOURCE_TYPE,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class AuthorsInfoImpl implements AuthorsInfo {
    private static final Logger LOG = LoggerFactory.getLogger(AuthorsInfoImpl.class);
    final protected static String RESOURCE_TYPE="mysite/components/custom/authorsinfo";

    @SlingObject
    Resource resource;

    @ScriptVariable
    Page currentPage;

    @OSGiService
    AuthorService authorService;


    private String country;

    @Override
    public List<Map<String, String>> getAuthorsList() {
        List<Map<String, String>> authos=authorService.getAuthors(country);
        return authos;
    }

    @PostConstruct
    protected void init() throws LoginException {
       country=currentPage.getPath().split("/")[3];
    }

}
