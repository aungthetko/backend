package com.demo.springjwt.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

@Service
public class LoggerService  implements HealthIndicator {
    private static final String LOGGER_SERVICE = "Logger Service";

    @Override
    public Health health() {
        if (isLoggerServiceUp()){
            return Health.up()
                    .withDetail(LOGGER_SERVICE, "Service is running")
                    .build();
        }
        return Health.down()
                .withDetail(LOGGER_SERVICE, "Service is not available")
                .build();
    }
    private Boolean isLoggerServiceUp(){
        return false;
    }
}
