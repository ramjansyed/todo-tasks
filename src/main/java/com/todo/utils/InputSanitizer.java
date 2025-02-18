package com.todo.utils;

import org.apache.commons.text.StringEscapeUtils;

public class InputSanitizer {
    public static String sanitize(String input) {
        return StringEscapeUtils.escapeHtml4(input);
    }
}