package com.seok2.handout.service;

import com.seok2.handout.data.domain.Benefit;
import com.seok2.handout.data.domain.Handout;
import com.seok2.handout.exception.UnauthorizedAccessException;
import com.seok2.handout.repository.HandoutRepository;
import com.seok2.handout.web.dto.CreateIssueRequestView;
import com.seok2.handout.web.dto.HandoutResponseView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class HandoutService {

    private final HandoutRepository handoutRepository;

    @Transactional
    public Handout issue(long userId, String roomId, CreateIssueRequestView view) {
        Handout issue = handoutRepository.save(Handout.issue(userId, roomId, view.getParticipants(), view.getAmount()));
        return findById(issue.getId());
    }

    @Transactional
    public Benefit take(long id, long userId, String roomId) {
        Handout handout = findById(id);
        return handout.take(userId, roomId);
    }

    public HandoutResponseView show(long id, long userId) {
        return handoutRepository.findById(id)
                .filter(handout -> handout.isOwner(userId))
                .map(HandoutResponseView::of)
                .orElseThrow(UnauthorizedAccessException::new);
    }

    private Handout findById(long id) {
        return handoutRepository.findById(id)
                .orElseThrow(UnauthorizedAccessException::new);
    }

}
