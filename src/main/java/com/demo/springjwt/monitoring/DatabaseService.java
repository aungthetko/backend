package com.demo.springjwt.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService implements HealthIndicator {
    private static final String DATABASE_SERVICE = "Database Service";

    @Override
    public Health health() {
        if (isDatabaseServiceUp()){
            return Health.up()
                    .withDetail(DATABASE_SERVICE, "Service is running")
                    .build();
        }
        return Health.down()
                .withDetail(DATABASE_SERVICE, "Service is not available")
                .build();
    }

    private Boolean isDatabaseServiceUp(){
        return true;
    }
}
