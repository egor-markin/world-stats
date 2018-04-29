package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.rustyskies.beans.Field;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public class JSoupUtils {

    // https://jsoup.org/cookbook/extracting-data/selector-syntax

    public String getField(Document document, String cssQuery) {
        Elements elements = document.select(cssQuery);
        if (elements.size() > 0) {
            return elements.get(0).text();
        } else {
            log.warn("Nothing found on query: " + cssQuery);
            return null;
        }
    }

    public Double getDoubleField(Document document, String cssQuery) {
        return ParseUtils.parseDouble(getField(document, cssQuery));
    }

    public Integer getIntegerField(Document document, String cssQuery) {
        return ParseUtils.parseInt(getField(document, cssQuery));
    }
}
