package ru.rustyskies.datasource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Field;
import ru.rustyskies.tools.WikipediaInfoboxParser;

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

        // Moscow
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Moscow, fieldsMap);
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
        return WikipediaInfoboxParser.parseInfobox(city, citiesFieldsMap.get(city));
    }

    public static void main(String[] args) {
        System.out.println(getCity(City.Oslo));
    }
}
