package com.infra.easyinfra.Helpers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class StringUtils {

    public static List<String> splitByComma(String input) {
        if (input == null || input.isBlank()) {
            return List.of();
        }
        return Arrays.stream(input.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }
}
