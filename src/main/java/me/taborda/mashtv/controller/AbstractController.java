package me.taborda.mashtv.controller ;

import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.web.bind.annotation.ExceptionHandler ;
import org.springframework.web.bind.annotation.ResponseBody ;

public abstract class AbstractController {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractController.class) ;

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Exception handleException(final Exception e, final HttpServletRequest request, final HttpServletResponse response) {
        LOG.error(e.getMessage(), e) ;
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR) ;
        return new Exception(e.getMessage()) ;
    }
}
