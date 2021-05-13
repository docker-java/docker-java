package com.github.dockerjava.jsch;

import org.slf4j.LoggerFactory;

public class JschLogger implements com.jcraft.jsch.Logger {

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JschLogger.class);

    @Override
    public boolean isEnabled(int level) {
        switch (level) {
            case DEBUG:
                return LOGGER.isDebugEnabled() || LOGGER.isTraceEnabled();
            case INFO:
                return LOGGER.isDebugEnabled();
            case WARN:
                return LOGGER.isWarnEnabled();
            case ERROR:
            case FATAL:
                return LOGGER.isErrorEnabled();
            default:
                throw new IllegalArgumentException("Unknown log level: " + level);
        }
    }

    @Override
    public void log(int level, String message) {
        switch (level) {
            case DEBUG:
                LOGGER.debug(message);
                break;
            case INFO:
                LOGGER.info(message);
                break;
            case WARN:
                LOGGER.warn(message);
                break;
            case ERROR:
                LOGGER.error(message);
                break;
            case FATAL:
                LOGGER.error("FATAL: {}", message);
                break;
            default:
                throw new IllegalArgumentException("Unknown log level: " + level);
        }
    }


}
