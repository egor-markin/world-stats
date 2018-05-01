package ru.rustyskies;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Country;
import ru.rustyskies.beans.Field;
import ru.rustyskies.datasource.Numbeo;
import ru.rustyskies.datasource.RestCountriesEU;
import ru.rustyskies.datasource.Worldbank;
import ru.rustyskies.tools.ProxyUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Egor Markin
 * @since 05.08.2017
 */
@Slf4j
@UtilityClass
public class Main {

    // TODO Add HTML output using FreeMarker or React
    // TODO Run project on a public server
    // TODO Add XLSX output

    private final List<Map<Field, Object>> cities = new ArrayList<>();
    private final List<Map<Field, Object>> countries = new ArrayList<>();

    private Map<Field, Object> addCity(City city) {
        Map<Field, Object> map = new LinkedHashMap<>();
        map.put(Field.City, city.getName());
        map.put(Field.Country, city.getCountry().getName());
        cities.add(map);
        return map;
    }

    private Map<Field, Object> addCountry(Country country) {
        Map<Field, Object> map = new LinkedHashMap<>();
        map.put(Field.Country, country.getName());
        countries.add(map);
        return map;
    }

    public void main(String[] args) {
        ProxyUtils.enableSocksProxy();

        // TODO !!! Format all index columns to 00.00 format
        // TODO !!! Round GDP column' value to $mln
        // TODO !!! Add other Numbeo indexes

        // Countries
        for (Country country : Country.values()) {
            Map<Field, Object> map = addCountry(country);

            RestCountriesEU.Country c2 = RestCountriesEU.INSTANCE.getCountry(country.getAlpha3Code());
            int population = c2.getPopulation();
            map.put(Field.Population, population);
            map.put(Field.Area, c2.getArea());
            map.put(Field.PopulationDensity, c2.getArea() != 0 ? c2.getPopulation() / c2.getArea() : 0);

            double gdp = Worldbank.getGdp(country);
            map.put(Field.GDP, Worldbank.getGdp(country));
            map.put(Field.GDPpC, population != 0 ? gdp / population : 0);

            map.putAll(Numbeo.getData(country));
        }

        // Cities
//        for (City city : City.values()) {
//        Map<Field, Object> moscow = addCity(City.Moscow);
//        Map<Field, Object> moscowInfobox = WikipediaInfoboxParser.parseInfobox(City.Moscow);
//        moscow.put(Field.Population, moscowInfobox.get(Field.CityPopulation));
//        moscow.put(Field.Area, moscowInfobox.get(Field.Area));
//        moscow.put(Field.GDP, WikipediaPageParser.getCityGdp(City.Moscow));
//        System.out.println(moscow);
//        }

        GoogleSheetsReport.updateGoogleSheets(countries);
    }


}
