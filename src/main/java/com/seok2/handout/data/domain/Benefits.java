package com.seok2.handout.data.domain;

import com.seok2.handout.exception.DuplicateUserException;
import com.seok2.handout.exception.NoBenefitLeftException;
import lombok.Getter;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Embeddable
public class Benefits {

    private static final Random RANDOM = new Random();
    private static final int MINIMUM_PAID_AMOUNT = 1;

    @OneToMany(mappedBy = "handout", cascade = CascadeType.ALL)
    private final List<Benefit> benefits = new ArrayList<>();

    protected static Benefits create(Handout parent, int participants, int amount) {
        Benefits benefits = new Benefits();
        Arrays.stream(breakUp(participants, amount))
                .mapToObj(broken -> Benefit.of(parent, broken))
                .collect(Collectors.collectingAndThen(Collectors.toList(), benefits.benefits::addAll));
        return benefits;
    }

    protected void addAll(Benefit... benefits) {
        this.benefits.addAll(Arrays.asList(benefits));
    }

    private static int[] breakUp(final int participants, int amount) {
        int[] amounts = new int[participants];
        for (int i = 0; i < participants - 1; i += 1) {
            amounts[i] = nextInt(1, amount - (participants - i - 1));
            amount -= amounts[i];
        }
        amounts[participants - 1] = amount;
        return amounts;
    }

    private static int nextInt(final int startInclusive, final int endExclusive) {
        if (startInclusive == endExclusive) {
            return startInclusive;
        }
        return startInclusive + RANDOM.nextInt(endExclusive - startInclusive);
    }

    protected Benefit take(long userId) {
        checkDuplicated(userId);
        return benefits.stream()
                .filter(Benefit::isNotUse)
                .findFirst()
                .map(benefit -> benefit.take(userId))
                .orElseThrow(NoBenefitLeftException::new);
    }

    private void checkDuplicated(long userId) {
        if (benefits.stream()
                .anyMatch(benefit -> benefit.isSameReceiver(userId))) {
            throw new DuplicateUserException();
        }
    }

    protected int getTotalHandoutAmount() {
        return sumIfCondition(benefit -> true);
    }

    protected int getTotalPaidAmount() {
        return sumIfCondition(Benefit::isUse);
    }

    private int sumIfCondition(Predicate<Benefit> predicate) {
        return benefits.stream()
                .filter(predicate)
                .mapToInt(Benefit::getAmount)
                .sum();
    }

    public Stream<Benefit> stream() {
        return benefits.stream();
    }


}
