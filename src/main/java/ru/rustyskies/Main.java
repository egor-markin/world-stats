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

    // TODO Possible data sources
    // https://www.quora.com/Is-there-an-API-for-the-CIA-World-Factbook-or-one-that-provides-similar-information-on-Countries
    // https://stackoverflow.com/questions/11166295/retrieving-city-statistics-any-apis-or-databases
    // https://www.citypopulation.de/
    // https://www.oxfordeconomics.com/
    // http://citie.org/
    // http://ec.europa.eu/eurostat/web/cities/data/database
    // http://www.citymayors.com/statistics/
    // https://www.expatistan.com/
    // https://nomadlist.com/
    // https://www.cia.gov/library/publications/the-world-factbook/geos/rs.html
    // https://github.com/factbook
    // https://github.com/factbook/factbook.json
    // https://old.datahub.io/dataset/cia-world-factbook
    // https://datahub.io/search
    // http://data.un.org/
    // http://wiki.dbpedia.org/develop/datasets
    // https://developers.google.com/freebase/
    // https://en.wikipedia.org/wiki/Open_data
    // http://calculatenetsalary.com/

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

        // TODO !!! Add cities
        // Population - https://datahub.io/core/population-city#resource-unsd-citypopulation-year-both
        // https://www.citypopulation.de/php/russia-penza.php?cityid=56701000000
        // Datahub.getCityPopulation

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
        GoogleSheetsReport.updateGoogleSheets(countries);

        // Cities
        Map<Field, Object> moscow = addCity(City.Moscow);
//        moscow.put(Field.Population, moscowInfobox.get(Field.CityPopulation));
//        moscow.put(Field.Area, moscowInfobox.get(Field.Area));
//        moscow.put(Field.GDP, WikipediaPageParser.getCityGdp(City.Moscow));
        GoogleSheetsReport.updateGoogleSheets(cities);

//        for (City city : City.values()) {
//        Map<Field, Object> moscow = addCity(City.Moscow);
//        Map<Field, Object> moscowInfobox = WikipediaInfoboxParser.parseInfobox(City.Moscow);
//        System.out.println(moscow);
//        }

    }


}
