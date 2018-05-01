package ru.rustyskies.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
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
        Crime("crime"),
        Pollution("pollution"),
        Traffic("traffic"),
        QoL("quality-of-life"),
        Climate("climate");

        String name;
    }

    @Value
    private static class FieldLocation {
        private Field field;
        private Page countriesRankingPage;
        private int countriesRankingColumnIndex;
        private Page countryPage;
    }

    private final FieldLocation[] fieldLocations = new FieldLocation[] {
            new FieldLocation(Field.CoL, Page.CoL, 0, Page.CoL),
            new FieldLocation(Field.Rent, Page.CoL, 1, Page.CoL),
            new FieldLocation(Field.CoLRent, Page.CoL,2, Page.CoL),
            new FieldLocation(Field.Groceries, Page.CoL,3, Page.CoL),
            new FieldLocation(Field.Restaurants, Page.CoL,4, Page.CoL),
            new FieldLocation(Field.LPP, Page.CoL, 5, Page.CoL),
            new FieldLocation(Field.Crime, Page.Crime,0, Page.Crime),
            new FieldLocation(Field.Safety, Page.Crime,1, Page.Crime),
            new FieldLocation(Field.HealthCare, Page.Health,0, Page.Health),
            new FieldLocation(Field.Pollution, Page.Pollution,0, Page.Pollution),
            new FieldLocation(Field.Traffic, Page.Traffic,0, Page.Traffic),
            new FieldLocation(Field.QoL, Page.QoL,0, Page.QoL),
            new FieldLocation(Field.Climate, Page.QoL,8, Page.Climate)
    };

    private final Map<Field, FieldLocation> fieldLocationMap = new LinkedHashMap<>();
    static {
        for (FieldLocation fl : fieldLocations) {
            fieldLocationMap.put(fl.field, fl);
        }
    }

    private Map<Page, Document> countriesRankingDocuments = new HashMap<>();

    private Document getCountriesRankingDocument(Page page) {
        Document document;
        if (!countriesRankingDocuments.containsKey(page)) {
            try {
                document = Jsoup.connect(BASE_URL + page.name + "/rankings_by_country.jsp").get();
                countriesRankingDocuments.put(page, document);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return countriesRankingDocuments.get(page);
    }

    private Document getCountryDocument(Page page, Country country) {
        try {
            return Jsoup.connect(BASE_URL + page.name + "/country_result.jsp?country=" + country.name).get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Double getCountriesRankingValue(Country country, FieldLocation pl) {
        return JSoupUtils.getDoubleField(getCountriesRankingDocument(pl.countriesRankingPage),
                "td.cityOrCountryInIndicesTable:contains(" + country.name + ")" + StringUtils.repeat(" + td", pl.countriesRankingColumnIndex + 1));
    }
    private Double getCountryValue(Country country, FieldLocation pl) {
        return JSoupUtils.getDoubleField(getCountryDocument(pl.countriesRankingPage, country), "table.table_indices -> tr + tr -> td + td");
    }

    private Object getValue(Country country, Field field) {
        FieldLocation pl = fieldLocationMap.get(field);
        Object value = getCountriesRankingValue(country, pl);
        if (value == null) {
            // Sometimes a certain country is not presented at the countries ranking page, but the information is yet available on its specific page
            //log.warn("Looking for " + field + " field value at " + country + " country's specific page: " + pl);
            value = getCountryValue(country, pl);
        }
        return value;
    }

    public Map<Field, Object> getData(Country country) {
        Map<Field, Object> map = new LinkedHashMap<>();
        for (Field field : fieldLocationMap.keySet()) {
            map.put(field, getValue(country, field));
        }
        return map;
    }

    public static void main(String[] args) {
        System.out.println(getData(Country.Latvia));
//        System.out.println(getCountryValue(Country.Latvia, fieldLocationMap.get(Field.HealthCare)));
//        System.out.println(JSoupUtils.getDoubleField(getCountryDocument(Page.Health, Country.Latvia), "table.table_indices -> tr + tr -> td + td"));
//        System.out.println(getValue(Country.Latvia, Field.Climate));
    }

}
