package me.taborda.mashtv ;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration ;
import org.springframework.boot.test.SpringApplicationConfiguration ;
import org.springframework.context.annotation.ComponentScan ;
import org.springframework.context.annotation.Configuration ;

@SpringApplicationConfiguration
public abstract class AbstractIntegrationTest extends AbstractTest {

    @Configuration
    @ComponentScan({ "me.taborda.mashtv.repository", "me.taborda.mashtv.service" })
    @EnableAutoConfiguration
    static class TestConfig {

    }

}
