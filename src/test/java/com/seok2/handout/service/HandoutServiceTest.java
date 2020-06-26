package com.seok2.handout.service;

import com.seok2.handout.data.domain.Benefit;
import com.seok2.handout.data.domain.Handout;
import com.seok2.handout.exception.DuplicateUserException;
import com.seok2.handout.exception.TokenExpiredException;
import com.seok2.handout.exception.UnauthorizedAccessException;
import com.seok2.handout.utils.TokenProvider;
import com.seok2.handout.web.dto.CreateIssueRequestView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class , MockitoExtension.class})
@SpringBootTest
class HandoutServiceTest {

    @Autowired
    private HandoutService handoutService;

    private Handout sample;

    @BeforeEach
    public void setUp() {
        CreateIssueRequestView view = CreateIssueRequestView.builder()
                .participants(2)
                .amount(1000)
                .build();
        sample = handoutService.issue(1,"room1", view);
    }

    @Transactional
    @Test
    void show() {
        assertThat(handoutService.show(sample.getId(), 1).getAmount()).isEqualTo(1000);
    }

    @Transactional
    @Test
    void show_exception_permission() {
        assertThatThrownBy(() -> handoutService.show(sample.getId(), 2))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

    @Transactional
    @Test
    void show_exception_expired() {
        assertThatThrownBy(() -> handoutService.show(sample.getId(), 2))
                .isInstanceOf(UnauthorizedAccessException.class);
    }

}