package com.demo.springjwt.kafka;

import com.demo.springjwt.modal.User;
import com.demo.springjwt.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserConsumer {

    public static final Logger LOGGER = LoggerFactory.getLogger(UserConsumer.class);
    private final UserRepo userRepo;

    @KafkaListener(topics = "backend-kafka",
            groupId = "${spring.kafka.consumer.group-id}")
    public void consumeUser(User user){
        LOGGER.info(String.format("Consuming User : " + user.toString()));
        userRepo.save(user);
    }
}
