package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

@UtilityClass
@Slf4j
public class JSoupUtils {

    // https://jsoup.org/cookbook/extracting-data/selector-syntax

    private final String QUERIES_SEPARATOR = "->";

    public String getField(Document document, String cssQuery) {
        String[] queries = cssQuery.split(QUERIES_SEPARATOR);
        Elements elements = document.select(queries[0].trim());
        for (int i = 1; i < queries.length; i++) {
            elements = elements.select(queries[i].trim());
        }

        if (elements.size() > 0) {
            return elements.get(0).text();
        } else {
            //log.warn("Nothing found on query: " + cssQuery);
            return null;
        }
    }

    public Double getDoubleField(Document document, String cssQuery) {
        return ParseUtils.extractDouble(getField(document, cssQuery));
    }

    public Integer getIntegerField(Document document, String cssQuery) {
        return ParseUtils.extractInt(getField(document, cssQuery));
    }
}
