package com.demo.springjwt.monitoring;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Endpoint(id = "custom") @Service
public class CustomActuatorEndpoint {

    @ReadOperation
    private Map<String, String> sayHello(){
        Map<String, String> map = new HashMap<>();
        map.put("Hello", "World");
        return map;
    }

}
