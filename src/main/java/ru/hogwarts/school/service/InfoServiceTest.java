package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("test")
public class InfoServiceTest implements InfoService{
    private static final Logger logger = LoggerFactory.getLogger(InfoServiceProd.class);

    @Value("${server.port}")
    private Integer port;

    public InfoServiceTest() {
    }

    @Override
    public Integer getPort() {
        logger.info("Was invoked method for get port {} - TEST", port);
        return port;
    }
}
