package ru.rustyskies.datasource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import ru.rustyskies.beans.City;
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

    private final String BASE_URL = "https://www.numbeo.com/";

    @Getter
    @AllArgsConstructor
    public enum Page {
        CoL("cost-of-living"),
        HealthCare("health-care"),
        Crime("crime"),
        Pollution("pollution"),
        Traffic("traffic"),
        Climate("climate"),
        QoL("quality-of-life");

        String name;
    }

    private enum LocationType {
        City,
        Country
    }

    @Value
    private static class FieldLocation {
        Field field;
        Page countriesRankingPage;
        int countriesRankingColumnIndex;
        Page page;
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
        new FieldLocation(Field.HealthCare, Page.HealthCare,0, Page.HealthCare),
        new FieldLocation(Field.Pollution, Page.Pollution,0, Page.Pollution),
        new FieldLocation(Field.Traffic, Page.Traffic,0, Page.Traffic),
        new FieldLocation(Field.Climate, Page.QoL,8, Page.Climate),
        new FieldLocation(Field.QoL, Page.QoL,0, Page.QoL),
    };

    private final Map<Field, FieldLocation> fieldLocationMap = new LinkedHashMap<>();
    static {
        for (FieldLocation fl : fieldLocations) {
            fieldLocationMap.put(fl.field, fl);
        }
    }

    private final Map<Page, Document> countryRankingDocuments = new HashMap<>();
    private final Map<Page, Document> cityRankingDocuments = new HashMap<>();

    private Document getRankingDocument(Page page, LocationType locationType) {
        if (locationType.equals(LocationType.Country)) {
            if (!countryRankingDocuments.containsKey(page)) {
                try {
                    countryRankingDocuments.put(page, Jsoup.connect(BASE_URL + page.name + "/rankings_by_country.jsp").get());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return countryRankingDocuments.get(page);
        } else if (locationType.equals(LocationType.City)) {
            if (!cityRankingDocuments.containsKey(page)) {
                try {
                    cityRankingDocuments.put(page, Jsoup.connect(BASE_URL + page.name + "/rankings_current.jsp").get());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return cityRankingDocuments.get(page);
        } else {
            throw new RuntimeException("Unexpected locationType: " + locationType);
        }
    }

    private Document getDocument(Page page, String locationName, LocationType locationType) {
        try {
            if (locationType == LocationType.City) {
                return Jsoup.connect(BASE_URL + page.name + "/in/" + locationName).get();
            } else if (locationType == LocationType.Country) {
                return Jsoup.connect(BASE_URL + page.name + "/country_result.jsp?country=" + locationName).get();
            } else {
                throw new RuntimeException("Unexpected locationType: " + locationType);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Double getRankingValue(String locationName, LocationType locationType, FieldLocation pl) {
        return JSoupUtils.getDoubleField(getRankingDocument(pl.countriesRankingPage, locationType),
                "td.cityOrCountryInIndicesTable:contains(" + locationName + ")" + StringUtils.repeat(" + td", pl.countriesRankingColumnIndex + 1));
    }

    private Double getValue(String locationName, LocationType locationType, FieldLocation pl) {
        String cssQuery;
        if (locationType == LocationType.City) {
            // TODO
            // possible cssQuery for cities: "div.summary -> ul -> li:contains(cost of living index of) -> span.emp_number"
            cssQuery = "table.table_indices -> tr + tr -> td + td";
        } else if (locationType == LocationType.Country) {
            cssQuery = "table.table_indices -> tr + tr -> td + td";
        } else {
            throw new RuntimeException("Unexpected locationType: " + locationType);
        }
        return JSoupUtils.getDoubleField(getDocument(pl.page, locationName, locationType), cssQuery);
    }

    private Object getValue(String locationName, LocationType locationType, Field field) {
        FieldLocation pl = fieldLocationMap.get(field);
        // At first looking for the value at the Ranking page
        Object value = getRankingValue(locationName, locationType, pl);
        if (value == null) {
            // If no value was found at the ranking page -> looking for it at the individual page of the city/country
            value = getValue(locationName, locationType, pl);
        }
        return value;
    }

    private Map<Field, Object> getData(String locationName, LocationType locationType) {
        Map<Field, Object> map = new LinkedHashMap<>();
        for (Field field : fieldLocationMap.keySet()) {
            map.put(field, getValue(locationName, locationType, field));
        }
        return map;
    }

    public Map<Field, Object> getCountryData(Country country) {
        return getData(country.name, LocationType.Country);
    }

    public Map<Field, Object> getCityData(City city) {
        return getData(city.name, LocationType.City);
    }

    public void main(String[] args) {
        //System.out.println(getCountryData(Country.Latvia));
//        System.out.println(getCountryValue(Country.Latvia, fieldLocationMap.get(Field.HealthCare)));
//        System.out.println(JSoupUtils.getDoubleField(getCountryDocument(Page.HealthCare, Country.Latvia), "table.table_indices -> tr + tr -> td + td"));
//        System.out.println(getValue(Country.Latvia, Field.Climate));

//        System.out.println(getCityData(City.Moscow));
        //System.out.println(getValue(City.Chicago.name, LocationType.City, Field.CoL));
        System.out.println(getCountryData(Country.USA));
        System.out.println(getCityData(City.Chicago));
    }
}
