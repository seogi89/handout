package com.seok2.handout.data.domain;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@ToString
@Getter
@Table(
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"handout_id", "user_id"})
)
@Entity
public class Benefit extends AbstractEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "handout_id", foreignKey = @ForeignKey(name = "fk_benefit_to_handout"))
    private Handout handout;
    @Column(name = "user_id")
    private Long userId;
    private int amount;
    private LocalDateTime receivedAt;
    @Version
    private Long version;

    protected static Benefit of(Handout parent, int amount) {
        Benefit benefit = new Benefit();
        benefit.handout = parent;
        benefit.amount = amount;
        return benefit;
    }

    protected boolean isUse() {
        return this.userId != null;
    }

    protected boolean isNotUse() {
        return !isUse();
    }

    protected Benefit take(long userId) {
        this.userId = userId;
        this.receivedAt = LocalDateTime.now();
        return this;
    }

    protected boolean isSameReceiver(long userId) {
        return this.userId != null && this.userId == userId;
    }
}

