package com.seok2.handout.data.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BenefitTest {

    @Test
    void isNotUse() {
        Benefit benefit = Benefit.of(null, 100);
        assertThat(benefit.isNotUse()).isTrue();
    }

    @Test
    void take() {
        Benefit benefit = Benefit.of(null, 100);
        benefit.take(1);
        assertThat(benefit.isNotUse()).isFalse();
        assertThat(benefit.getUserId()).isEqualTo(1);
    }
}