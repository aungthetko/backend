package com.demo.springjwt.kafka;

import com.demo.springjwt.modal.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProducer {

    private final KafkaTemplate<String, User> kafkaTemplate;
    public static final Logger LOGGER = LoggerFactory.getLogger(UserProducer.class);

    public void sendMessage(User user){
        Message<User> message = MessageBuilder.withPayload(user)
                        .setHeader(KafkaHeaders.TOPIC, "backend-kafka")
                .build();
        kafkaTemplate.send(message);
    }
}
