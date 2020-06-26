package com.seok2.handout.repository;

import com.seok2.handout.data.domain.Handout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandoutRepository extends JpaRepository<Handout, Long> {
}
