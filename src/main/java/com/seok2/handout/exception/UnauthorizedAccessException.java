package com.seok2.handout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "유효하지 않은 접근입니다.")
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException() {
        super("유효 하지 않은 접근 입니다.");
    }
}
