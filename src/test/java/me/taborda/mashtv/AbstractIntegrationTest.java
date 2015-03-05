package me.taborda.mashtv ;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration ;
import org.springframework.boot.test.SpringApplicationConfiguration ;
import org.springframework.context.annotation.ComponentScan ;
import org.springframework.context.annotation.Configuration ;

@SpringApplicationConfiguration
public class AbstractIntegrationTest extends AbstractTest {

    @ComponentScan
    @Configuration
    @EnableAutoConfiguration
    static class TestConfig {

    }

}
