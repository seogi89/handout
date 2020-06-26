package com.seok2.handout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,  reason = "여러번 시도 할 수 없습니다.")
public class DuplicateUserException extends IllegalArgumentException {

    public DuplicateUserException() {
        super("여러번 시도 할 수 없습니다.");
    }
}
