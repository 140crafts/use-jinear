package co.jinear.core;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import co.jinear.core.config.logback.appender.AxiomAppender;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.resilience.annotation.EnableResilientMethods;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Map;

@SpringBootApplication
@EnableResilientMethods
@EnableScheduling
public class CoreApplication {

    private static final Map<String, Object> DEFAULT_PROPERTIES = Map.of("spring.jackson.use-jackson2-defaults", "true");

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CoreApplication.class);
        application.setDefaultProperties(DEFAULT_PROPERTIES);
        ConfigurableApplicationContext context = application.run(args);
        context.start();
        addAxiomAppender(context, (LoggerContext) LoggerFactory.getILoggerFactory());
    }

    private static void addAxiomAppender(ConfigurableApplicationContext context, LoggerContext loggerContext) {
        AxiomAppender axiomAppender = context.getBean(AxiomAppender.class);
        Logger rootLogger = loggerContext.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(axiomAppender);
    }
}
