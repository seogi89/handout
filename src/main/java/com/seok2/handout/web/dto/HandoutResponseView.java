package com.seok2.handout.web.dto;

import com.seok2.handout.data.domain.Handout;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public final class HandoutResponseView {
    private int amount;
    private int paidAmount;
    private LocalDateTime createdAt;
    private List<BenefitResponseView> benefits;

    private HandoutResponseView() {

    }

    public static HandoutResponseView of(Handout issue) {
        HandoutResponseView hrv = new HandoutResponseView();
        hrv.amount = issue.getTotalHandoutAmount();
        hrv.paidAmount = issue.getTotalPaidAmount();
        hrv.createdAt = issue.getCreatedAt();
        hrv.benefits = BenefitResponseView.ofList(issue.getBenefits());
        return hrv;
    }
}
