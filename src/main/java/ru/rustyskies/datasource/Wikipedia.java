package ru.rustyskies.datasource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Field;
import ru.rustyskies.tools.WikipediaWikitextParser;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Egor Markin
 * @since 30.05.2018
 */
@UtilityClass
@Slf4j
public class Wikipedia {

    private final Map<City, Map<Field, String>> citiesFieldsMap = new HashMap<>();

    static {
        Map<Field, String> fieldsMap;

        // Singapore
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Singapore, fieldsMap);
        fieldsMap.put(Field.Area, "area_km2");
        fieldsMap.put(Field.CityPopulation, "population_estimate");
        fieldsMap.put(Field.GDPPPP, "GDP_PPP");
        fieldsMap.put(Field.GDPPPPpC, "GDP_PPP_per_capita");
        fieldsMap.put(Field.GDP, "GDP_nominal");
        fieldsMap.put(Field.GDPpC, "GDP_nominal_per_capita");

        // Moscow
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Moscow, fieldsMap);
        fieldsMap.put(Field.LocalName, "ru_name");
        fieldsMap.put(Field.Area, "area_km2");
        fieldsMap.put(Field.CityPopulation, "pop_2010census");

        // Berlin
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Berlin, fieldsMap);
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.Elevation, "elevation");
        fieldsMap.put(Field.CityPopulation, "population");
        fieldsMap.put(Field.MetroPopulation, "pop_metro");
        fieldsMap.put(Field.GDP, "GDP");
        fieldsMap.put(Field.GDPpC, "GDP_per_capita");

        // TODO !!! Add all the cities !!!
    }

    public Map<Field, Object> getCity(City city) {
        if (!citiesFieldsMap.containsKey(city)) {
            log.warn("No fields mapping for the city found: " + city);
            return new HashMap<>();
        }
        return WikipediaWikitextParser.getCityInfobox(city, citiesFieldsMap.get(city));
    }

    public static void main(String[] args) {
        System.out.println(getCity(City.Singapore));
        WikipediaWikitextParser.printCityInfobox(City.Singapore);
    }
}
