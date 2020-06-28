package com.seok2.handout.web.dto;

import com.seok2.handout.data.domain.Benefit;
import com.seok2.handout.data.domain.Benefits;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public final class BenefitResponseView {

    private Long receiverId;
    private int amount;
    private LocalDateTime receiveTime;
    private long version;

    public static List<BenefitResponseView> ofList(Benefits benefits) {
        return benefits.stream()
                .map(BenefitResponseView::of)
                .collect(Collectors.toList());
    }

    private static BenefitResponseView of(Benefit benefit) {
        return BenefitResponseView.builder()
                .receiverId(benefit.getUserId())
                .amount(benefit.getAmount())
                .receiveTime(benefit.getReceivedAt())
                .build();
    }
}
