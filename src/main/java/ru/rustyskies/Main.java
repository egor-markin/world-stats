package ru.rustyskies;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Country;
import ru.rustyskies.beans.Field;
import ru.rustyskies.datasource.RestCountriesEU;
import ru.rustyskies.datasource.WikipediaInfoboxParser;
import ru.rustyskies.datasource.WikipediaPageParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Egor Markin
 * @since 05.08.2017
 */
@Slf4j
@UtilityClass
public class Main {

    private final List<Map<Field, Object>> cities = new ArrayList<>();
    private final List<Map<Field, Object>> countries = new ArrayList<>();

    private Map<Field, Object> addCity(City city) {
        Map<Field, Object> map = new HashMap<>();
        map.put(Field.Name, city.getName());
        map.put(Field.Country, city.getCountry().getName());
        cities.add(map);
        return map;
    }

    private Map<Field, Object> addCountry(Country country) {
        Map<Field, Object> map = new HashMap<>();
        map.put(Field.Name, country.getName());
        countries.add(map);
        return map;
    }

    public void main(String[] args) {
        // TODO Create a map: city.field - datasource_name+field

//        Map<Field, Object> moscow = addCity(City.Moscow);
//        Map<Field, Object> moscowInfobox = WikipediaInfoboxParser.parseInfobox(City.Moscow);
//        moscow.put(Field.Population, moscowInfobox.get(Field.CityPopulation));
//        moscow.put(Field.Area, moscowInfobox.get(Field.Area));
//        moscow.put(Field.GDP, WikipediaPageParser.getCityGdp(City.Moscow));
//        System.out.println(moscow);

        for (Country c : Country.values()) {
            Map<Field, Object> map = addCountry(c);

            RestCountriesEU.Country c2 = RestCountriesEU.INSTANCE.getCountry(c.getAlpha3Code());
            map.put(Field.Population, c2.getPopulation());
            map.put(Field.Area, c2.getArea());
            map.put(Field.PopulationDensity, c2.getArea() != 0 ? c2.getPopulation() / c2.getArea() : 0);

            // TODO GDP - https://data.worldbank.org/indicator/NY.GDP.MKTP.CD?year_high_desc=true
            // https://datahelpdesk.worldbank.org/knowledgebase/articles/898590-api-country-queries
            // https://datahelpdesk.worldbank.org/knowledgebase/articles/898599-api-indicator-queries
            // http://api.worldbank.org/v2/countries/rus/indicators/NY.GDP.MKTP.CD?date=2017:2017
            // http://api.worldbank.org/v2/countries/rus/indicators/NY.GDP.MKTP.CD
        }
        GoogleSheetsReport.updateGoogleSheetsPage(countries);
    }


}
