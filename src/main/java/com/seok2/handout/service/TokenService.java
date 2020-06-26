package com.seok2.handout.service;

import com.seok2.handout.exception.TokenExpiredException;
import com.seok2.handout.utils.TokenProvider;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    private static final int TOKEN_EXPIRE_DATE = 7 * 60 * 60 * 24;

    private final RedisTemplate<String, String> redisTemplate;

    public TokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String create(String roomId, long handoutId, int expire) {
        String key = TokenProvider.create(roomId);
        while(redisTemplate.hasKey(key)) {
            key = TokenProvider.create(roomId);
        }
        redisTemplate.opsForValue().set(
                key,
                String.valueOf(handoutId),
                expire,
                TimeUnit.SECONDS
        );
        return TokenProvider.parse(key);
    }
    public String create(String roomId, long handoutId) {
        return create(roomId, handoutId, TOKEN_EXPIRE_DATE);
    }

    public Long findByToken(String token) {
        String id = Optional.ofNullable(redisTemplate.opsForValue().get(token))
                .orElseThrow(TokenExpiredException::new);
        return Long.valueOf(id);
    }
}
