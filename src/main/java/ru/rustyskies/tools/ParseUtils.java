package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

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

    public BigDecimal parseCurrency(String str, String prefix) {
        if (str == null || str.trim().equals("")) {
            return null;
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
            }
            return result;
        } else {
            return null;
        }
    }

    public String parseCoords(String str) {
        // TODO !!! From here !!!
        // Based on https://stackoverflow.com/questions/8263959/how-to-convert-between-degrees-minutes-seconds-to-decimal-coordinates
        String regexCoords = "(\\d+)\\|(\\d+)\\|(\\d+)\\|([NS])\\|(\\d+)\\|(\\d+)\\|(\\d+)\\|([EW])";
        Matcher m = Pattern.compile(regexCoords).matcher(str);
        if (m.find()) {
            if (m.groupCount() >= 8) {
                double dd1 = Integer.parseInt(m.group(1)) + Integer.parseInt(m.group(2)) / 60.0 + Integer.parseInt(m.group(3)) / 3600.0;
                if (m.group(4).equals("S")) {
                    dd1 *= -1;
                }
                double dd2 = Integer.parseInt(m.group(5)) + Integer.parseInt(m.group(6)) / 60.0 + Integer.parseInt(m.group(7)) / 3600.0;
                if (m.group(8).equals("W")) {
                    dd2 *= -1;
                }

                return dd1 + " " + dd2;
            }
        }
        return null;
    }

    /** For debuging purpurses */
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
//        System.out.println("Result: " + parseCurrency(s, "\\$"));
//        System.out.println(parseCurrency("ddd$33,123.33gbg"));
//        System.out.println("Result: " + parseCurrency("554855"));
//        System.out.println(parseCoords("{{coord|52|31|00|N|13|23|20|E|format=dms|display=inline,title}}"));
        System.out.println(parseCoords("{{coord|55|45|N|37|37|E|type:adm1st_region:RU|display=inline,title}}"));
    }
}
