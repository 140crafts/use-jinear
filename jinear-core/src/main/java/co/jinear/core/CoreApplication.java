package co.jinear.core;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import co.jinear.core.config.logback.appender.AxiomAppender;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableRetry
@EnableScheduling
@EnableFeignClients
public class CoreApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(CoreApplication.class, args);
        context.start();
        addAxiomAppender(context, (LoggerContext) LoggerFactory.getILoggerFactory());
    }

    private static void addAxiomAppender(ConfigurableApplicationContext context, LoggerContext loggerContext) {
        AxiomAppender axiomAppender = context.getBean(AxiomAppender.class);
        Logger rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(axiomAppender);
    }
}
