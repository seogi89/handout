package com.seok2.handout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "받을 수 있는 기간이 만료 되었습니다.")
public class HandoutExpiredException extends RuntimeException {

    public HandoutExpiredException() {
        super("받을 수 있는 기간이 만료 되었습니다.");
    }
}
