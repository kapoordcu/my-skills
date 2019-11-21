package com.performance.sql;

import com.performance.sql.service.TwitterProducerService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        AbstractApplicationContext context = new AnnotationConfigApplicationContext(Application.class);
        context.getBean(TwitterProducerService.class).produce();
    }
}

