package no.api.web;

import org.apache.commons.lang3.StringUtils;
import org.jsondoc.core.pojo.JSONDoc;
import org.jsondoc.core.pojo.JSONDoc.MethodDisplay;
import org.jsondoc.core.scanner.DefaultJSONDocScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/api/b2b/v1", produces = APPLICATION_JSON_VALUE)
public class JSONDocController {
    private static final Logger LOGGER = LoggerFactory.getLogger(JSONDocController.class);

    private String version;
    private String basePath;
    private List<String> packages;
    private MethodDisplay displayMethodAs;
    private boolean playgroundEnabled;

    public void setVersion(String version) {
        this.version = version;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

    public void setDisplayMethodAs(MethodDisplay displayMethodAs) {
        this.displayMethodAs = displayMethodAs;
    }

    public void setPlaygroundEnabled(boolean playgroundEnabled) {
        this.playgroundEnabled = playgroundEnabled;
    }

    /**
     * Get the documentation for the available REST services which is used to population the existing html docs page.
     */
    @RequestMapping(value = "/doc/api-docs", method = RequestMethod.GET,
            produces= MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public JSONDoc getApiDocs(HttpServletRequest request) {
        String baseUrl = StringUtils.substringBefore(request.getRequestURL().toString(), "/v1");
        setBasePath(baseUrl);
        setVersion("1.0");
        setPackages(Arrays.asList("no.api.web.v1"));
        setDisplayMethodAs(MethodDisplay.URI);
        setPlaygroundEnabled(true);
        return new DefaultJSONDocScanner().getJSONDoc(this.version, this.basePath, this.packages,
                this.playgroundEnabled, this.displayMethodAs);
    }

    /**
     * Bootstrap the documentation page.
     */
    @RequestMapping(value = "/docs", method = RequestMethod.GET)
    public String getDocs(Model model, HttpServletRequest request) {LOGGER.debug("Get request for document API");
        String baseUrl = StringUtils.substringBefore(request.getRequestURL().toString(), "/v1");
        model.addAttribute("url", baseUrl + "/v1/doc/api-docs");
        return "jsondoc";
    }
}
