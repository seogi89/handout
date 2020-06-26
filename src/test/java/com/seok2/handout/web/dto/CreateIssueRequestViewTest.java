package com.seok2.handout.web.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class CreateIssueRequestViewTest {

    @DisplayName("참여자보다 뿌릴 금액이 적을 경우")
    @Test
    void validate() {
        assertThatThrownBy(() -> CreateIssueRequestView.builder()
                .amount(1)
                .participants(2)
                .build()
                .validate())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("참여자가 1명보다 작은 경우")
    @Test
    void validate2() {
        assertThatThrownBy(() -> CreateIssueRequestView.builder()
                .amount(1)
                .participants(0)
                .build()
                .validate())
                .isInstanceOf(IllegalArgumentException.class);
    }
}