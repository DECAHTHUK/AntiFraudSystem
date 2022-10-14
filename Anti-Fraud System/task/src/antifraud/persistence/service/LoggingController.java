package antifraud.persistence.service;

import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class LoggingController {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingController.class);

    public void info(String message) {
        LOGGER.info(message);
    }

    public void warn(String message) {
        LOGGER.warn(message);
    }

    public void error(String message) {
        LOGGER.error(message);
    }

    public void debug(String message) {
        LOGGER.debug(message);
    }
}