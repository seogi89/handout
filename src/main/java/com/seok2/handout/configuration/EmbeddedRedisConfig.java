package com.seok2.handout.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.ServerSocket;

@Configuration
public class EmbeddedRedisConfig {

    @Value("${spring.redis.port}")
    private int redisPort;
    private RedisServer redisServer;

    @PostConstruct
    public void redisServer() throws IOException {
        int port = findUnusedPort(redisPort);
        redisServer = new RedisServer(port);
        redisServer.start();
    }

    private int findUnusedPort(int port) {
        if(port == 65535) {
            throw new IllegalArgumentException("Not Found Available Port: 6379 ~ 65535");
        }
        try (ServerSocket socket = new ServerSocket(port)){
        }
        catch (IOException e) {
            return findUnusedPort(port+1);
        }
        return port;
    }

    @PreDestroy
    public void stopRedis() {
        if (redisServer != null) {
            redisServer.stop();
        }
    }

}
