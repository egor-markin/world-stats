package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Field;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public class WikipediaWikitextParser {

    private final String BLOCK_START = "{{";
    private final String BLOCK_END = "}}";
    private final String LINK_START = "[[";
    private final String LINK_END = "]]";
    private final String GROUP_SEPARATOR = "|";
    private final String INFOBOX_PREFIX = BLOCK_START + "Infobox";

    private String getWikiPageXml(String pageName) {
        try {
            return Jsoup.connect("https://en.wikipedia.org/w/api.php?action=parse&page=" + pageName + "&prop=wikitext&format=xml").get().outerHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printPos(String wikitext, int pos, int depth) {
        final int offset = 60;
        int lOffset = (pos - offset) < 0 ? 0 : (pos - offset);
        int rOffset = wikitext.length() < (pos + offset) ? wikitext.length() : (pos + offset);
        System.out.println("Current pos: " + wikitext.substring(lOffset, pos) + " (" + depth + ") " + wikitext.substring(pos, rOffset));
    }

    /** Extracts all the groups separated by | symbol from current block as a list */
    private List<String> getGroups(String wikitext, int startPos) {
        List<String> list = new ArrayList<>();

        // If wikitext starts with {{ -> skipping them
        if (wikitext.trim().startsWith(BLOCK_START, startPos)) {
            startPos += BLOCK_START.length();
        }

        int pos = startPos;
        int groupStartPos = startPos;
        int depth = 0;
        while (pos < wikitext.length() && depth >= 0) {
//            printPos(wikitext, pos, depth);
            if (wikitext.startsWith(BLOCK_START, pos)) {
                depth++;
                pos += BLOCK_START.length() - 1;
            } else if (wikitext.startsWith(BLOCK_END, pos)) {
                depth--;
                pos += BLOCK_END.length() - 1;
            } else if (wikitext.startsWith(LINK_START, pos)) {
                depth++;
                pos += LINK_START.length() - 1;
            } else if (wikitext.startsWith(LINK_END, pos)) {
                depth--;
                pos += LINK_END.length() - 1;
            } else if (depth == 0 && wikitext.startsWith(GROUP_SEPARATOR, pos)) {
                if (wikitext.startsWith(GROUP_SEPARATOR, groupStartPos)) {
                    groupStartPos += GROUP_SEPARATOR.length();
                }
                list.add(wikitext.substring(groupStartPos, pos));
                groupStartPos = pos;
            }
            pos++;
        }

        // Adding the last group
        if (pos > groupStartPos) {
            list.add(wikitext.substring(groupStartPos + GROUP_SEPARATOR.length(), pos - BLOCK_END.length()));
        }

        return list;
    }

    private Map<String, String> infoboxTextToMap(String infoboxWikitext, int startPos) {
        Map<String, String> map = new LinkedHashMap<>();

        if (infoboxWikitext == null || infoboxWikitext.trim().equals("")) {
            return map;
        }

        if (infoboxWikitext.startsWith(INFOBOX_PREFIX, startPos)) {
            startPos += INFOBOX_PREFIX.length();
        }

        List<String> groups = getGroups(infoboxWikitext, startPos);
        for (String group : groups) {
            int p = group.indexOf("=");
            if (p == -1) {
                map.put(group.trim(), "");
            } else {
                map.put(group.substring(0, p).trim(), group.substring(p + 1).trim());
            }
        }
        return map;
    }

    private Map<String, String> getCityInfobox(City city) {
        String xml = getWikiPageXml(city.name);
        int startPos = xml.indexOf(INFOBOX_PREFIX);
        if (startPos == -1) {
            log.warn("There is no InfoBox in the provided wikitext: " + xml);
            return null;
        } else {
            return infoboxTextToMap(xml, startPos);
        }
    }

    public Map<Field, Object> getCityInfobox(City city, Map<Field, String> fieldsMap) {
        Map<Field, Object> resultMap = new HashMap<>();

        Map<String, String> cityMap = getCityInfobox(city);
        if (cityMap == null || cityMap.isEmpty()) {
            log.warn("No infobox data has been obtained for " + city);
            return resultMap;
        }

        for (Field field : fieldsMap.keySet()) {
            String cityMapFieldName = fieldsMap.get(field);
            if (!cityMap.containsKey(cityMapFieldName)) {
                log.warn("No city infobox field was found: " + cityMapFieldName + " for field " + field);
                continue;
            }
            String valueStr = cityMap.get(cityMapFieldName);

            Object value;
            switch (field.fieldType) {
                case String:
                    value = valueStr;
                    break;
                case Integer:
                    value = ParseUtils.parseInt(valueStr);
                    break;
                case Double:
                    value = ParseUtils.parseDouble(valueStr);
                    break;
                case Currency:
                    value = ParseUtils.parseUsd(valueStr);
                    break;
                default:
                    throw new RuntimeException("Unexpected field type: " + field.fieldType);
            }
            resultMap.put(field, value);
        }
        return resultMap;
    }

    public void printCityInfobox(City city) {
        Map<String, String> map = getCityInfobox(city);
        for (String param : map.keySet()) {
            System.out.println(param + " -> " + map.get(param));
        }
    }
}
