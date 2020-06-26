package com.seok2.handout.data.domain;

import com.seok2.handout.exception.HandoutExpiredException;
import com.seok2.handout.exception.UnauthorizedAccessException;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class Handout extends AbstractEntity{

    public static final int HANDOUT_EXPIRED_MINUTE = 10;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String roomId;
    @Embedded
    private Benefits benefits = new Benefits();

    public static Handout issue(long userId, String roomId, int participants, int amount) {
        Handout handout = new Handout();
        handout.userId = userId;
        handout.roomId = roomId;
        handout.benefits = Benefits.create(handout, participants, amount);
        return handout;
    }

    protected Benefit take(final long userId, final String roomId, LocalDateTime dateTime) {
        if(!this.roomId.equals(roomId) || isOwner(userId)){
            throw new UnauthorizedAccessException();
        }
        if(isExpired(dateTime)) {
            throw new HandoutExpiredException();
        }
        return benefits.take(userId);
    }

    public Benefit take(final long userId, final String roomId) {
        return take(userId,roomId,LocalDateTime.now());
    }

    private boolean isExpired(LocalDateTime dateTime) {
        return getCreatedAt().plusMinutes(HANDOUT_EXPIRED_MINUTE).isBefore(dateTime);
    }

    public int getTotalHandoutAmount() {
        return benefits.getTotalHandoutAmount();
    }

    public int getTotalPaidAmount() {
        return benefits.getTotalPaidAmount();
    }

    public boolean isOwner(long userId) {
        return this.userId == userId;
    }
}
