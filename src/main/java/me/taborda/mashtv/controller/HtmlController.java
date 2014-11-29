package me.taborda.mashtv.controller ;

import java.io.IOException ;

import javax.servlet.http.HttpServletResponse ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.stereotype.Controller ;
import org.springframework.web.bind.annotation.ExceptionHandler ;
import org.springframework.web.bind.annotation.RequestMapping ;

@Controller
public class HtmlController {

    private static final Logger LOG = LoggerFactory.getLogger(HtmlController.class) ;

    @RequestMapping("/feeds")
    public String feeds() {
        return "feeds" ;
    }

    @RequestMapping("/shows")
    public String shows() {
        return "shows" ;
    }

    @ExceptionHandler(Exception.class)
    public String handleException(final Exception e, final HttpServletResponse response) throws IOException {
        LOG.error(e.getMessage(), e) ;
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()) ;
        return "error" ;
    }
}
