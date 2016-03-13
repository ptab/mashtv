package me.taborda.mashtv.controller.api;

import java.io.IOException ;

import javax.servlet.http.HttpServletResponse ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.web.bind.annotation.ControllerAdvice ;
import org.springframework.web.bind.annotation.ExceptionHandler ;
import org.springframework.web.bind.annotation.ResponseBody ;

@ControllerAdvice(assignableTypes = { RestBaseController.class })
public class RestBaseController {

    private static final Logger LOG = LoggerFactory.getLogger(RestBaseController.class) ;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Exception handleException(final Exception e, final HttpServletResponse response) throws IOException {
        LOG.warn(e.getMessage()) ;
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage()) ;
        return e ;
    }
}
