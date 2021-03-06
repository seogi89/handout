package com.seok2.handout.data.domain;

import com.seok2.handout.exception.NoBenefitLeftException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


class BenefitsTest {

    @Test
    public void create() {
        Benefits benefits = Benefits.create(null, 2, 1000);
        assertThat(benefits.getBenefits().size()).isEqualTo(2);
        assertThat(benefits.getTotalHandoutAmount()).isEqualTo(1000);
    }

    @Test
    public void take_exception_no_benefit_left() {
        Benefits benefits = new Benefits();
        benefits.addAll(Benefit.of(null, 1000).take(1));
        assertThatThrownBy(() -> benefits.take(2))
                .isInstanceOf(NoBenefitLeftException.class);
    }

    @Test
    public void getTotalHandoutAmount() {
        Benefits benefits = new Benefits();
        benefits.addAll(Benefit.of(null, 1000), Benefit.of(null, 2000));
        assertThat(benefits.getTotalHandoutAmount()).isEqualTo(3000);
    }

    @Test
    public void getTotalPaidAmount() {
        Benefits benefits = new Benefits();
        benefits.addAll(Benefit.of(null, 1000).take(1), Benefit.of(null, 2000));
        assertThat(benefits.getTotalPaidAmount()).isEqualTo(1000);
    }

}