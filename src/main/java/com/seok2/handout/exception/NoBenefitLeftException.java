package com.seok2.handout.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND,  reason = "남은 금액이 없습니다.")
public class NoBenefitLeftException extends RuntimeException{

    public NoBenefitLeftException() {
        super("남은 금액이 없습니다.");
    }

}
