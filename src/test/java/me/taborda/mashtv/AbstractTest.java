package me.taborda.mashtv ;

import java.net.URL;

import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

public abstract class AbstractTest extends AbstractJUnit4SpringContextTests {

    protected URL getResource(final String name) {
        return Thread.currentThread().getContextClassLoader().getResource(name) ;
    }

}
