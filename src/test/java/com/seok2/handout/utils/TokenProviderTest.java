package com.seok2.handout.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class TokenProviderTest {

    @Test
    void create() {
        assertThat(TokenProvider.create("R101")).startsWith("handout:R101");
    }

    @Test
    void parse() {
        String key = TokenProvider.create("R101");
        String token = key.substring(key.lastIndexOf(":") + 1);
        assertThat(TokenProvider.parse(key)).isEqualTo(token);
    }
}