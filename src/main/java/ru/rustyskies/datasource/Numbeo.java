package ru.rustyskies.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.rustyskies.beans.Country;
import ru.rustyskies.beans.Field;
import ru.rustyskies.tools.JSoupUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @since 29.04.2018
 * @author Egor Markin
 */
@Slf4j
@UtilityClass
public class Numbeo {

    // Since Numbeo doesn't provide free API access to their data, this class uses HTML parser to retrieve necessary data

    private static final String BASE_URL = "https://www.numbeo.com/";

    @Getter
    @AllArgsConstructor
    private enum Page {
        CoL("cost-of-living"),
        Health("health-care"),
        Crime("crime");

        String url;
    }

    private Map<Page, Document> documents = new HashMap<>();

    private Document getDocument(Page page) {
        Document document;
        if (!documents.containsKey(page)) {
            try {
                document = Jsoup.connect(BASE_URL + page.url + "/rankings_by_country.jsp").get();
                documents.put(page, document);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return documents.get(page);
    }

    private Double getValue(Page page, Country country, int columnIndex) {
        return JSoupUtils.getDoubleField(getDocument(page),
                "td.cityOrCountryInIndicesTable:contains(" + country.name + ")" + StringUtils.repeat(" + td", columnIndex + 1));
    }

    public Map<Field, Object> getData(Country country) {
        Map<Field, Object> map = new LinkedHashMap<>();
        map.put(Field.CoL, getValue(Page.CoL, country, 0));
        map.put(Field.Rent, getValue(Page.CoL, country, 1));
        map.put(Field.CoLRent, getValue(Page.CoL, country, 2));
        map.put(Field.Groceries, getValue(Page.CoL, country, 3));
        map.put(Field.Restaurants, getValue(Page.CoL, country, 4));
        map.put(Field.LPP, getValue(Page.CoL, country, 5));
        map.put(Field.Crime, getValue(Page.Crime, country, 0));
        map.put(Field.Safety, getValue(Page.Crime, country, 1));
        map.put(Field.HealthCare, getValue(Page.Health, country, 0));
        return map;
    }

    public static void main(String[] args) {
        System.out.println(getData(Country.Russia));
    }

}
