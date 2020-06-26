package com.seok2.handout.data.domain;

import com.seok2.handout.exception.DuplicateUserException;
import com.seok2.handout.exception.HandoutExpiredException;
import com.seok2.handout.exception.UnauthorizedAccessException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HandoutTest {

    @Test
    void isOwner() {
        Handout handout = Handout.issue(1, "R1", 1, 10);
        assertThat(handout.isOwner(1)).isTrue();
        assertThat(handout.isOwner(2)).isFalse();
    }
    @Test
    void take() {
        Handout handout = Handout.issue(1, "R1", 1, 10);
        Benefit benefit = handout.take(2, "R1");
        assertThat(benefit.getUserId()).isEqualTo(2);
        assertThat(benefit.getAmount()).isEqualTo(10);
    }


    @Test
    void take_exception_valid_expired() {
        Handout handout = Handout.issue(1, "R1", 1, 10);
        assertThatThrownBy(()-> handout.take(2,"R1",  LocalDateTime.now().plusMinutes(11)))
                .isInstanceOf(HandoutExpiredException.class);
    }

    @Test
    void take_exception_valid_room() {
        Handout handout = Handout.issue(1, "R1", 1, 10);
        assertThatThrownBy(()-> handout.take(2,"R2"))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    void take_exception_valid_id() {
        Handout handout = Handout.issue(1, "R1", 1, 10);
        assertThatThrownBy(()-> handout.take(1,"R1"))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Test
    void take_exception_duplicate_id() {
        Handout handout = Handout.issue(1, "R1", 1, 10);
        handout.take(2,"R1");
        assertThatThrownBy(()-> handout.take(2,"R1"))
                .isInstanceOf(DuplicateUserException.class);
    }
}