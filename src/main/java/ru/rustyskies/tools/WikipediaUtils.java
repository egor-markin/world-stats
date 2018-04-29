package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;

import java.io.IOException;

@Slf4j
@UtilityClass
public class WikipediaUtils {

    public String getWikiPageXml(String pageName) {
        try {
            return Jsoup.connect("https://en.wikipedia.org/w/api.php?action=parse&page=" + pageName + "&prop=wikitext&format=xml").get().outerHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getGroup(String wikitext, int fromPos, String groupSeparator, int groupIndex) {
        int pos = fromPos;
        int groupStartPos = fromPos;
        int group = -1;
        int depth = 0;
        while (pos < wikitext.length() && group < groupIndex) {
            groupStartPos = pos;
            while (pos < wikitext.length() && (depth > 0 || !wikitext.startsWith(groupSeparator, pos))) {
                if (wikitext.startsWith("{{", pos)) {
                    pos++;
                    depth++;
                } else if (wikitext.startsWith("}}", pos)) {
                    pos++;
                    depth--;
                    if (depth < 0) {
                        log.warn("There is uneven number of {{ and }} symbols in the string: " + wikitext);
                        depth = 0;
                    }
                }
                pos++;
            }
            pos++;
            group++;
        }
        if (depth > 0) {
            log.warn("There is uneven number of {{ and }} symbols in the string: " + wikitext);
        }
        return pos == wikitext.length() ? wikitext.substring(groupStartPos) : wikitext.substring(groupStartPos, pos - 1);
    }

}
