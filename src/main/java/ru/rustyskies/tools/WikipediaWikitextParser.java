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

    /** Extracts all the groups separated by | symbol from current block as a list */
    private List<String> getGroups(String wikitext, int startPos) {
        List<String> list = new ArrayList<>();

        // If wikitext starts with {{ -> skipping them
        if (wikitext.trim().startsWith(BLOCK_START)) {
            startPos += BLOCK_START.length();
        }

        int pos = startPos;
        int groupStartPos = startPos;
        int blockDepth = 0;
        int linkDepth = 0;
        while (pos < wikitext.length() && blockDepth >= 0) {
            if (wikitext.startsWith(BLOCK_START, pos)) {
                blockDepth++;
            } else if (wikitext.startsWith(BLOCK_END, pos)) {
                blockDepth--;
            } else if (wikitext.startsWith(LINK_START, pos)) {
                linkDepth++;
            } else if (wikitext.startsWith(LINK_END, pos)) {
                linkDepth--;
            } else if (blockDepth == 0 && linkDepth == 0 && wikitext.startsWith(GROUP_SEPARATOR, pos)) {
                if (wikitext.startsWith(GROUP_SEPARATOR, groupStartPos)) {
                    groupStartPos += GROUP_SEPARATOR.length();
                }
                list.add(wikitext.substring(groupStartPos, pos));
                groupStartPos = pos;
            }
            pos++;
        }
        return list;
    }

    /** Extracts specified block confined in {{ and }} from a list of such a blocks from the provided wikitext */
    private String getBlock(String wikitext, int startPos, int blockNum) {
        // TODO !!! REWITE THIS METHOD !!!

        int pos = startPos;
        int blockStartPos = startPos;

        int block = -1;
        int depth = 0;
        while (pos < wikitext.length() && block < blockNum) {
            while (pos < wikitext.length() && depth > 0) {
                if (wikitext.startsWith(BLOCK_START, pos)) {
                    depth++;
                } else if (wikitext.startsWith(BLOCK_END, pos)) {
                    depth--;
                    if (depth < 0) {
                        log.warn("There is uneven number of " + BLOCK_START + " and " + BLOCK_END + " symbols in the string: " + wikitext);
                        depth = 0;
                    }
                }
                pos++;
            }
        }
        if (depth > 0) {
            log.warn("There is uneven number of " + BLOCK_START + " and " + BLOCK_END + " symbols in the string: " + wikitext);
        }
        return pos == wikitext.length() ? wikitext.substring(blockStartPos) : wikitext.substring(blockStartPos, pos - 1);
    }

    private String extractInfoboxText(String wikitext) {
        if (wikitext == null || wikitext.trim().equals("")) {
            return null;
        }
        int startPos = wikitext.indexOf(INFOBOX_PREFIX);
        if (startPos == -1) {
            log.warn("There is no InfoBox in the provided wikitext: " + wikitext);
            return null;
        }
        return getBlock(wikitext, startPos, 0);
    }

    private Map<String, String> infoboxTextToMap(String infoboxWikitext) {
        Map<String, String> map = new LinkedHashMap<>();

        if (infoboxWikitext == null || infoboxWikitext.trim().equals("")) {
            return map;
        }

        int startPos = 0;
        if (infoboxWikitext.trim().startsWith(INFOBOX_PREFIX)) {
            startPos = INFOBOX_PREFIX.length() - 1;
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

    private String getParam(String infoboxWikitext, String paramName) {
        String s1 = "|" + paramName + "=";

        int p = infoboxWikitext.indexOf(s1);
        if (p == -1) {
            log.warn("There is no '" + paramName + "' param in provided wikitext");
            return null;
        } else {
            Scanner s = new Scanner(infoboxWikitext.substring(p + s1.length()));
            s.useDelimiter("[}|]");
            if (s.hasNext()) {
                return s.next();
            }
            s.close();
        }
        return null;
    }

    private Double getDoubleParam(String infoboxWikitext, String paramName) {
        String str = getParam(infoboxWikitext, paramName);
        if (str != null && !str.trim().equals("")) {
            return Double.valueOf(str.trim());
        } else {
            return null;
        }
    }

    private Integer getIntParam(String infoboxWikitext, String paramName) {
        String str = getParam(infoboxWikitext, paramName);
        if (str != null && !str.trim().equals("")) {
            return Integer.valueOf(str.trim());
        } else {
            return null;
        }
    }

    private Integer getInfoboxGdp(String gdpStr) {
        if (gdpStr != null && !gdpStr.trim().equals("")) {
            Matcher m = Pattern.compile("\\$[0-9,]+").matcher(gdpStr);
            if (m.find()) {
                return Integer.valueOf(m.group().substring(1).replaceAll(",", ""));
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public Map<String, String> parseInfobox(City city) {
        return infoboxTextToMap(extractInfoboxText(getWikiPageXml(city.name)));
    }

    public Map<Field, Object> parseInfobox(City city, Map<Field, String> fieldsMap) {
        Map<Field, Object> resultMap = new HashMap<>();

        // TODO !!! Rewrite this method using infoboxTextToMap() + cache !!!

        String infoBox = extractInfoboxText(getWikiPageXml(city.name));
        for (Field f : fieldsMap.keySet()) {
            Object value;
            if (Field.GDP.equals(f) || Field.GDPpC.equals(f)) {
                value = getInfoboxGdp(getParam(infoBox, "GDP"));
            } else {
                switch (f.fieldType) {
                    case String:
                        value = getParam(infoBox, fieldsMap.get(f));
                        break;
                    case Integer:
                        value = getIntParam(infoBox, fieldsMap.get(f));
                        break;
                    case Double:
                        value = getDoubleParam(infoBox, fieldsMap.get(f));
                        break;
                    default:
                        throw new RuntimeException("Unexpected field type: " + f.fieldType);
                }
            }
            resultMap.put(f, value);
        }
        return resultMap;
    }

    public void main(String args[]) {
//        System.out.println(getGdp("130/ $152 "));
//        System.out.println(getGdp("35,600/ $41,900 "));
//        System.out.println(parseInfobox(City.Berlin));
//        System.out.println(parseInfobox(Main.City.Moscow));

//        String wikitext = "[[Moscow]] | Russia | Eastern Europe | style=\"background:#abd5f5;\"| {{nts|225.0}} (2015)&lt;ref name=\"autogenerated1\"&gt;{{cite web|url=http://mrd.gks.ru/wps/wcm/connect/rosstat_ts/mrd/ru/statistics/grp/ |title=Валовой региональный продукт::Мордовиястат |website= Mrd.gks.ru |date= |accessdate= 2017-04-01}}&lt;/ref&gt; | style=\"background:#50f231;\"| {{nts|553.3}} | style=\"background:#79ff76;\"| {{nts|321}} | style=\"background:#FEE7F0;\"| {{nts|325.8}} | style=\"background:khaki;\"\n";
//        System.out.println(wikitext);
//        System.out.println(getBlock(wikitext, 0, "|", 4));

//        System.out.println(WikipediaUtils.getWikiPageXml("Moscow"));

        City city = City.Singapore;

        System.out.println(extractInfoboxText(getWikiPageXml(city.name)));

        Map<String, String> map = parseInfobox(city);
        for (String param : map.keySet()) {
            System.out.println(param + " -> " + map.get(param));
        }
    }
}
