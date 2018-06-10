package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
public class ParseUtils {

    // Based on http://moderntone.blogspot.com/2013/02/validate-double-values-with-java.html

    // Add ^ at the beginning and $ at the end of the patterns for exact matching

    private final String regexDecimal = "-?[0-9,]*\\.\\d+";
    private final String regexInteger = "-?[0-9,]+";
    private final String regexDouble = regexDecimal + "|" + regexInteger;

    private final Map<String, Integer> suffixes = new HashMap<>();
    static {
        suffixes.put("mln", 1000000);
        suffixes.put("million", 1000000);
        suffixes.put("bln", 1000000000);
        suffixes.put("billion", 1000000000);
    }

    public Integer parseInt(String str) {
        if (str == null || str.trim().equals("")) {
            return null;
        }
        Matcher m = Pattern.compile(regexInteger).matcher(str);
        if (m.find()) {
            return Integer.parseInt(m.group().replaceAll(",", ""));
        } else {
            return null;
        }
    }

    public Double parseDouble(String str) {
        if (str == null || str.trim().equals("")) {
            return null;
        }
        Matcher m = Pattern.compile(regexDouble).matcher(str);
        if (m.find()) {
            return Double.parseDouble(m.group().replaceAll(",", ""));
        } else {
            return null;
        }
    }

    public Double parseUsd(String str) {
        if (str == null || str.trim().equals("")) {
            return null;
        }
        // <possible $ symbol>(<number>123,456.789)<possible space>(<possible suffix>billion)
        Matcher m = Pattern.compile("\\$?([0-9,.]+)\\s*([a-zA-Z]*)").matcher(str);
        if (m.find()) {
            double result = 0;
            if (m.groupCount() > 0) {
                result = Double.parseDouble(m.group(1).replaceAll(",", ""));
            }
            if (m.groupCount() > 1 && m.group(2) != null && !m.group(2).trim().equals("")) {
                String suffix = m.group(2).trim().toLowerCase();
                if (suffixes.containsKey(suffix)) {
                    result *= suffixes.get(suffix);
                } else {
                    log.warn("Unknown suffix: " + suffix);
                }
            }
            return result;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // TODO !!!! It doesn't work correctly!!!! It should match $554.855 first!!!
        System.out.println("Result: " + parseUsd("1234 dfdf ($554.855 billion)&lt;ref name=imf&gt;"));
//        System.out.println(parseUsd("ddd$33,123.33gbg"));
//        System.out.println("Result: " + parseUsd("554855"));
    }
}
