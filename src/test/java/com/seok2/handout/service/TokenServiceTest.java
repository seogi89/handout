package com.seok2.handout.service;

import com.seok2.handout.exception.TokenExpiredException;
import com.seok2.handout.utils.TokenProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
        assertThat(tokenService.getAndParseByToken(TokenProvider.concat(roomId, token))).isEqualTo(1);
    }

    @Test
    public void expire() throws InterruptedException {
        String roomId = "R101";
        String token = tokenService.create(roomId, 1, 1);
        Thread.sleep(2000);
        assertThatThrownBy(() -> tokenService.getAndParseByToken(TokenProvider.concat(roomId, token)))
                .isInstanceOf(TokenExpiredException.class);
    }

}