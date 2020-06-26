package com.seok2.handout.web;

import com.seok2.handout.web.dto.CreateIssueRequestView;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;


//@TODO 커버리지 측정시 테스트 실패함 - 원인 분석 해야함.
// 포트 충돌 이슈로 추측됨. 포트 변경 하였으나, 어플리케이션 동작중 여전히 테스트 케이스 실패함
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient(timeout = "100000")
class HandoutAcceptanceTest {

    @Autowired
    public WebTestClient webTestClient;

    public String issueAndGetToken() {
        CreateIssueRequestView view  = CreateIssueRequestView.builder()
                .participants(1)
                .amount(10)
                .build();
        return webTestClient.post().uri("/api/handouts")
                .header("X-ROOM-ID", "R101")
                .header("X-USER-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(view), CreateIssueRequestView.class)
                .exchange()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get("X-TOKEN-ID").get(0);
    }

    @Test
    public void issue() {
        CreateIssueRequestView view  = CreateIssueRequestView.builder()
                .participants(1)
                .amount(10)
                .build();
        webTestClient.post().uri("/api/handouts")
                .header("X-ROOM-ID", "R101")
                .header("X-USER-ID", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(view), CreateIssueRequestView.class)
                .exchange()
                .expectHeader().exists("X-TOKEN-ID");
    }

    @Test
    public void show() {
        String token = issueAndGetToken();
        webTestClient.get().uri("/api/handouts")
                .header("X-ROOM-ID", "R101")
                .header("X-USER-ID", "1")
                .header("X-TOKEN-ID", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.amount").isEqualTo(10);
    }

    @Test
    public void take() {
        String token = issueAndGetToken();
        webTestClient.post().uri("/api/handouts/take")
                .header("X-ROOM-ID", "R101")
                .header("X-USER-ID", "2")
                .header("X-TOKEN-ID", token)
                .exchange()
                .expectStatus().isOk()
                .expectBody(String.class)
                .isEqualTo("10");
    }
}