package ru.rustyskies;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Field;
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

    private Map<Field, Object> addCity(City city) {
        Map<Field, Object> map = new HashMap<>();
        map.put(Field.Name, city.getName());
        map.put(Field.Country, city.getCountry().getTitle());
        cities.add(map);
        return map;
    }

    public void main(String[] args) {
        // TODO Create a map: city.field - datasource_name+field

        Map<Field, Object> moscow = addCity(City.Moscow);

        Map<Field, Object> moscowInfobox = WikipediaInfoboxParser.parseInfobox(City.Moscow);
        moscow.put(Field.Population, moscowInfobox.get(Field.CityPopulation));
        moscow.put(Field.Area, moscowInfobox.get(Field.Area));
        moscow.put(Field.GDP, WikipediaPageParser.getCityGdp(City.Moscow));

        System.out.println(moscow);
    }


}
