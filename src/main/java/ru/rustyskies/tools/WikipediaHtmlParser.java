package ru.rustyskies.tools;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.rustyskies.beans.Field;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@UtilityClass
public class WikipediaHtmlParser {

    private String extractTableRow(String wikitext, String firstColumnText) {
        if (wikitext == null || wikitext.trim().equals("")) {
            return null;
        }

        final String rowStartText = "|- | ";
        int startPos = wikitext.indexOf(rowStartText + firstColumnText);
        if (startPos == -1) {
            log.warn("There is no matching text in the provided wikitext: " + firstColumnText);
            return null;
        }

        int endPos = wikitext.indexOf("| |-", startPos + 1);
        if (endPos == -1) {
            endPos = wikitext.length();
        }

        return wikitext.substring(startPos + rowStartText.length(), endPos);
    }

//    public Integer getCityGdp(City city) {
//        String row = extractTableRow(WikipediaUtils.getWikiPageXml("List_of_cities_by_GDP"), "[[" + city.name + "]]");
//        String cell = WikipediaUtils.getBlock(row, 0, "|", 4);
//        return ParseUtils.extractInt(cell);
//    }

    public Map<Field, Object> parseCity(String url) {
        Map<Field, Object> cityMap = new HashMap<>();

        Document d;
        try {
            d = Jsoup.connect(url).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        cityMap.put(Field.Name, JSoupUtils.getField(d, "h1#firstHeading"));
        cityMap.put(Field.Area, JSoupUtils.getDoubleField(d, "th:contains(city) + td:contains(km2)"));
        cityMap.put(Field.Elevation, JSoupUtils.getIntegerField(d, "th:contains(elevation) + td:contains(m)"));
        cityMap.put(Field.CityPopulation, JSoupUtils.getIntegerField(d, "th:contains(city) + td:contains(m)"));
        return cityMap;
    }

    public void main(String args[]) {
//        System.out.println(getCityGdp(City.Berlin));
//        System.out.println(getDoubleFromString("891.7 km2 (344.3 sq mi)"));
        System.out.println(parseCity("https://en.wikipedia.org/wiki/Berlin"));
    }

}
