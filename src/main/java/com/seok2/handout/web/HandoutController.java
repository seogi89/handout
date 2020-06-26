package com.seok2.handout.web;

import com.seok2.handout.configuration.resolver.HandoutId;
import com.seok2.handout.data.domain.Benefit;
import com.seok2.handout.data.domain.Handout;
import com.seok2.handout.exception.DuplicateUserException;
import com.seok2.handout.service.HandoutService;
import com.seok2.handout.service.TokenService;
import com.seok2.handout.utils.TokenProvider;
import com.seok2.handout.web.dto.CreateIssueRequestView;
import com.seok2.handout.web.dto.HandoutResponseView;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;

@RequiredArgsConstructor
@RestController
@RequestMapping(HandoutController.HANDOUT_BASE)
public class HandoutController {

    protected static final String HANDOUT_BASE = "api/handouts";

    private final HandoutService handoutService;
    private final TokenService tokenService;

    @PostMapping
    public ResponseEntity<Void> issue(
            @RequestHeader("X-USER-ID") long userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @RequestBody CreateIssueRequestView view
    ) {
        view.validate();
        Handout handout = handoutService.issue(userId, roomId, view);
        String key = tokenService.create(roomId, handout.getId());
        return ResponseEntity.noContent()
                .header("X-TOKEN-ID", TokenProvider.parse(key))
                .build();
    }

    @PostMapping("/take")
    public ResponseEntity<Integer> take(
            @RequestHeader("X-USER-ID") long userId,
            @RequestHeader("X-ROOM-ID") String roomId,
            @HandoutId long id) {
        return ResponseEntity.ok(doTake(userId, roomId, id).getAmount());
    }

    private Benefit doTake(long userId, String roomId, long id) {
        try {
            return handoutService.take(id, userId, roomId);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateUserException();
        } catch (OptimisticLockException e) {
            return doTake(userId, roomId, id);
        }
    }

    @GetMapping
    public ResponseEntity<HandoutResponseView> show(
            @RequestHeader("X-USER-ID") long userId,
            @HandoutId long id) {
        return ResponseEntity.ok(handoutService.show(id, userId));
    }


}
