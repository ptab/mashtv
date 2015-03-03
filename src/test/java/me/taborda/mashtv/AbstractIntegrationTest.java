package me.taborda.mashtv ;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration ;
import org.springframework.boot.test.SpringApplicationConfiguration ;
import org.springframework.context.annotation.ComponentScan ;
import org.springframework.context.annotation.Configuration ;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests ;

@SpringApplicationConfiguration
public class AbstractIntegrationTest extends AbstractJUnit4SpringContextTests {

    @ComponentScan
    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {

    }

}
