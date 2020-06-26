package com.seok2.handout.web.dto;

import com.seok2.handout.data.domain.Benefit;
import com.seok2.handout.data.domain.Benefits;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public final class BenefitResponseView {

    private Long receiverId;
    private int amount;
    private LocalDateTime receiveTime;
    private long version;

    private BenefitResponseView () {

    }

    public static List<BenefitResponseView> ofList(Benefits benefits) {
        return benefits.stream()
                .map(BenefitResponseView::of)
                .collect(Collectors.toList());
    }

    private static BenefitResponseView of(Benefit benefit) {
        BenefitResponseView brv = new BenefitResponseView();
        brv.receiverId = benefit.getUserId();
        brv.amount = benefit.getAmount();
        brv.receiveTime = benefit.getReceivedAt();
        brv.version = benefit.getVersion();
        return brv;
    }
}
