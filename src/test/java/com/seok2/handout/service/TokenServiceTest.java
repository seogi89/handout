package com.seok2.handout.service;

import com.seok2.handout.configuration.RedisConfig;
import com.seok2.handout.exception.TokenExpiredException;
import com.seok2.handout.utils.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class TokenServiceTest {

    @Autowired
    private TokenService tokenService;

    @Test
    public void create() {
        String roomId = "R101";
        String token = tokenService.create(roomId, 1);
        assertThat(tokenService.findByToken(TokenProvider.concat(roomId, token))).isEqualTo(1);
    }

    @Test
    public void expire() throws InterruptedException {
        String roomId = "R101";
        String token = tokenService.create(roomId, 1, 1);
        Thread.sleep(2000);
        assertThatThrownBy(() -> tokenService.findByToken(TokenProvider.concat(roomId, token)))
                .isInstanceOf(TokenExpiredException.class);
    }


}