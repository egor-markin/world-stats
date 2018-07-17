package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
@Slf4j
public class ParseUtils {

    // Based on http://moderntone.blogspot.com/2013/02/validate-double-values-with-java.html

    // https://docs.oracle.com/javase/7/docs/api/java/util/regex/Pattern.html

    // Add ^ at the beginning and $ at the end of the patterns for exact matching
    private final String regexDecimal = "-?[0-9,]*\\.\\d+"; // <possible '-' symbol><zero or more digits including ',' symbol><obligatory '.' symbol><one or more digits>
    private final String regexInteger = "-?[0-9,]+"; // <possible '-' symbol><one or more digits including ',' symbol>
    private final String regexDouble = regexDecimal + "|" + regexInteger;
    private final String regexCurrency = "([0-9,.]+)\\s*([a-zA-Z]*)"; // (Group 1: zero or more digits including ',' symbol)<possible zero or more space characters>(Group 2: <possible suffix>billion)

    private final Map<String, BigDecimal> suffixes = new HashMap<>();
    static {
        suffixes.put("mln", BigDecimal.valueOf(1000000));
        suffixes.put("million", BigDecimal.valueOf(1000000));
        suffixes.put("bln", BigDecimal.valueOf(1000000000));
        suffixes.put("billion", BigDecimal.valueOf(1000000000));
        suffixes.put("trillion", BigDecimal.valueOf(1000000000000L));
    }

    public String extractString(String str) {
        str = Jsoup.parse(Jsoup.parse(str).text()).text(); // For some reason double HTML decoding is needed
//        str = str.replaceAll("^\"|\"$", ""); // Removing leading & trailing double quote characters (if present)
        str = str.replaceAll("^''|''$", ""); // Removing leading & trailing '' characters (if present)
//        str = str.replaceAll("^''|''$", ""); // Stripping the string from all the wikitext links
        return str;
    }

    public Integer extractInt(String str) {
        if (str == null || str.trim().equals("")) {
            return null;
        } else {
            str = extractString(str);
        }
        Matcher m = Pattern.compile(regexInteger).matcher(str);
        if (m.find()) {
            return Integer.parseInt(m.group().replaceAll(",", ""));
        } else {
            return null;
        }
    }

    public int parseInt(String str) {
        if (str == null || str.trim().equals("")) {
            return 0;
        }
        return Integer.parseInt(str);
    }

    public double parseDouble(String str) {
        if (str == null || str.trim().equals("")) {
            return 0;
        }
        return Double.parseDouble(str);
    }

    public Double extractDouble(String str) {
        if (str == null || str.trim().equals("")) {
            return null;
        } else {
            str = extractString(str);
        }
        Matcher m = Pattern.compile(regexDouble).matcher(str);
        if (m.find()) {
            return Double.parseDouble(m.group().replaceAll(",", ""));
        } else {
            return null;
        }
    }

    public BigDecimal extractCurrency(String str, String prefix, String defaultSuffix) {
        if (str == null || str.trim().equals("")) {
            return null;
        } else {
            str = extractString(str);
        }

        Matcher m = null;
        boolean match = false;

        if (prefix != null && !prefix.trim().equals("")) {
            // At first trying to look for a string like (prefix + number)
            m = Pattern.compile(prefix + regexCurrency).matcher(str);
            match = m.find();
        }

        if (!match) {
            // If there isn't any -> looking again for just a number like 100.3
            m = Pattern.compile(regexCurrency).matcher(str);
            match = m.find();
        }

        if (match) {
            BigDecimal result = BigDecimal.ZERO;
            if (m.groupCount() > 0) {
                result = new BigDecimal(m.group(1).replaceAll(",", ""));
            }
            if (m.groupCount() > 1 && m.group(2) != null && !m.group(2).trim().equals("")) {
                String suffix = m.group(2).trim().toLowerCase();
                if (suffixes.containsKey(suffix)) {
                    result = result.multiply(suffixes.get(suffix));
                } else {
                    log.warn("Unknown suffix: " + suffix);
                }
            } else {
                if (defaultSuffix != null) {
                    result = result.multiply(suffixes.get(defaultSuffix));
                }
            }
            return result;
        } else {
            return null;
        }
    }

    public String extractCoords(String str) {
        // Based on https://stackoverflow.com/questions/8263959/how-to-convert-between-degrees-minutes-seconds-to-decimal-coordinates
        String regexCoords = "(-?[0-9.]+)[|]?(\\d*)[|]?(\\d*)[|]?([NS]?)\\|(-?[0-9.]+)[|]?(\\d*)[|]?(\\d*)[|]?([EW]?)";
        Matcher m = Pattern.compile(regexCoords).matcher(str);
        if (m.find()) {
            if (m.groupCount() >= 8) {
                double dd1 = parseDouble(m.group(1)) + parseInt(m.group(2)) / 60.0 + parseInt(m.group(3)) / 3600.0;
                if (m.group(4).equals("S")) {
                    dd1 *= -1;
                }
                double dd2 = parseDouble(m.group(5)) + parseInt(m.group(6)) / 60.0 + parseInt(m.group(7)) / 3600.0;
                if (m.group(8).equals("W")) {
                    dd2 *= -1;
                }

                return dd1 + " " + dd2;
            }
        }
        return null;
    }

    /** For debuging purpurse */
    private void printGroups(Matcher m) {
        StringBuilder b = new StringBuilder();
        for (int i = 0; i <= m.groupCount(); i++) {
            b.append(m.group(i)).append(", ");
        }
        System.out.println("Groups[" + m.groupCount() + "]: " + b.toString());
    }

    public static void main(String[] args) {
//        String s = "1234 dfdf ($554.855 billion)&lt;ref name=imf&gt;";
//        String s = "1234 dfdf (554.855 billion)&lt;ref name=imf&gt;";
//        String s = "ddd";
//        System.out.println("Result: " + extractCurrency(s, "\\$"));
//        System.out.println(extractCurrency("ddd$33,123.33gbg"));
//        System.out.println("Result: " + extractCurrency("554855"));
//        System.out.println(extractCoords("{{coord|52|31|00|N|13|23|20|E|format=dms|display=inline,title}}"));
//        System.out.println(extractCoords("{{coord|55|45|N|37|37|E|type:adm1st_region:RU|display=inline,title}}"));
//        System.out.println(extractCoords("{{coord|55|N|37|E|type:adm1st_region:RU|display=inline,title}}"));
//        System.out.println(extractCoords("{{coord|40.7127|N|74.0059|W|region:US-NY|format=dms|display=inline,title}}"));
//        System.out.println(extractCoords("{{coord|40.009376|-75.133346|format=dms|region:US-PA|display=inline,title}}"));
//        System.out.println(extractDouble("&lt;!--CENSUS 2016 DATA ONLY, DO NOT USE ESTIMATES --&gt;631,486 [[List of the 100 largest municipalities in Canada by population|(8th]])"));
        //System.out.println(extractCurrency("[[CNY|¥]] 3.01 trillion&lt;br/&gt;[[USD|$]] 446.31 billion([[List of Chinese administrative divisions by GDP|11th]])", "\\$", ""));

        // TODO FROM HERE !!!
        // "[[CNY|¥]]" -> ¥
        System.out.println("[[CNY|fff]]".replaceAll("\\[\\[\\w+\\|\\w+", ""));
    }
}
