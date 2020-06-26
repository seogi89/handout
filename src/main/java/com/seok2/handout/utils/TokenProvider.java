package com.seok2.handout.utils;

import net.bytebuddy.utility.RandomString;

import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public final class TokenProvider {

    private static final Random RANDOM = new Random();

    private static final char [] TOKEN_CHARACTERS_POOL =
            "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
    private static final int KEY_SIZE = 3;
    private static final String KEY_PREFIX = "handout:{0}:{1}";


    private TokenProvider() {

    }

    public static String create(String roomId) {
        return MessageFormat.format(KEY_PREFIX, roomId, generate());
    }

    private static String generate() {
        return Stream.generate(() -> TOKEN_CHARACTERS_POOL[RANDOM.nextInt(TOKEN_CHARACTERS_POOL.length)])
                .limit(3)
                .map(String::valueOf)
                .collect(Collectors.joining());
    }

    public static String parse(String key) {
        return key.substring(key.lastIndexOf(":") + 1);
    }

    public static String concat(String roomId, String token) {
        return MessageFormat.format(KEY_PREFIX, roomId, token);
    }
}