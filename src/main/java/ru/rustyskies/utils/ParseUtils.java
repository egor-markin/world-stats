package ru.rustyskies.utils;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public class ParseUtils {

    public Integer parseInt(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = Pattern.compile("\\d+").matcher(str);
        if (m.find()) {
            return Integer.parseInt(m.group());
        } else {
            return null;
        }
    }

    public Double parseDouble(String str) {
        if (str == null) {
            return null;
        }
        Matcher m = Pattern.compile("(\\d+(?:\\.\\d+))").matcher(str);
        if (m.find()) {
            return Double.parseDouble(m.group(1));
        } else {
            return null;
        }
    }

}
