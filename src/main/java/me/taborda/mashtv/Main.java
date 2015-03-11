package me.taborda.mashtv ;

import org.springframework.boot.SpringApplication ;
import org.springframework.boot.autoconfigure.SpringBootApplication ;
import org.springframework.scheduling.annotation.EnableScheduling ;

@SpringBootApplication
@EnableScheduling
public class Main {

    public static void main(final String[] args) throws Exception {
        SpringApplication.run(Main.class, args) ;
    }
}
