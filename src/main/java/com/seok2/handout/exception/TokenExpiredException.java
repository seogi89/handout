package com.seok2.handout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "유효하지 않은 토큰입니다.")
public class TokenExpiredException extends RuntimeException {

    public TokenExpiredException() {
        super("유효 하지 않은 토큰 입니다.");
    }
}
