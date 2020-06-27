package com.seok2.handout.web.dto;

import com.seok2.handout.data.domain.Handout;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
public final class HandoutResponseView {
    private int amount;
    private int paidAmount;
    private LocalDateTime createdAt;
    private List<BenefitResponseView> benefits;

    private HandoutResponseView() {

    }

    public static HandoutResponseView of(Handout issue) {
        return HandoutResponseView.builder()
                .amount(issue.getTotalHandoutAmount())
                .paidAmount(issue.getTotalPaidAmount())
                .createdAt(issue.getCreatedAt())
                .benefits(BenefitResponseView.ofList(issue.getBenefits()))
                .build();
    }
}
