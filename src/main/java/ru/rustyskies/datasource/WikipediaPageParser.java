package ru.rustyskies.datasource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.utils.ParseUtils;
import ru.rustyskies.utils.WikipediaUtils;

@Slf4j
@UtilityClass
public class WikipediaPageParser {

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

    public Integer getCityGdp(City city) {
        String row = extractTableRow(WikipediaUtils.getWikiPageXml("List_of_cities_by_GDP"), "[[" + city.name + "]]");
        String cell = WikipediaUtils.getGroup(row, 0, "|", 4);
        return ParseUtils.parseInt(cell);
    }

    public void main(String args[]) {
        System.out.println(getCityGdp(City.Berlin));
    }

}
