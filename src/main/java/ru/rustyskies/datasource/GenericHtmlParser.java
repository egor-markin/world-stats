package ru.rustyskies.datasource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import ru.rustyskies.beans.Field;
import ru.rustyskies.utils.ParseUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@UtilityClass
@Slf4j
public class GenericHtmlParser {

    public Map<Field, Object> parseCity(String url) {
        Map<Field, Object> cityMap = new HashMap<>();

        Document d;
        try {
            d = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cityMap.put(Field.Name, getField(d, "h1#firstHeading"));
        cityMap.put(Field.Area, getDoubleField(d, "th:contains(city) + td:contains(km2)"));
        cityMap.put(Field.Elevation, getIntegerField(d, "th:contains(elevation) + td:contains(m)"));
        cityMap.put(Field.CityPopulation, getIntegerField(d, "th:contains(city) + td:contains(m)"));
        return cityMap;
    }

    private String getField(Document document, String cssQuery) {
        Elements elements = document.select(cssQuery);
        if (elements.size() > 0) {
            return elements.get(0).text();
        } else {
            log.warn("Nothing found on query: " + cssQuery);
            return null;
        }
    }

    private Double getDoubleField(Document document, String cssQuery) {
        return ParseUtils.parseDouble(getField(document, cssQuery));
    }

    private Integer getIntegerField(Document document, String cssQuery) {
        return ParseUtils.parseInt(getField(document, cssQuery));
    }

    public static void main(String[] args) {
//        System.out.println(getDoubleFromString("891.7 km2 (344.3 sq mi)"));
        System.out.println(GenericHtmlParser.parseCity("https://en.wikipedia.org/wiki/Berlin"));
    }
}
