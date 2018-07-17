package ru.rustyskies;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.City;
import ru.rustyskies.beans.Country;
import ru.rustyskies.beans.Field;
import ru.rustyskies.datasource.*;
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

    // Wiki parser for tables like:
    // https://en.wikipedia.org/wiki/List_of_municipalities_of_the_Netherlands
    // https://en.wikipedia.org/wiki/List_of_English_districts_by_area

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

    private final List<Map<Field, Object>> dataList = new ArrayList<>();

    private Map<Field, Object> addCity(City city) {
        Map<Field, Object> map = new LinkedHashMap<>();
        map.put(Field.Country, city.getCountry().getName());
        map.put(Field.City, city.getName());
        map.put(Field.Type, "City");
        dataList.add(map);
        return map;
    }

    private Map<Field, Object> addCountry(Country country) {
        Map<Field, Object> map = new LinkedHashMap<>();
        map.put(Field.Country, country.getName());
        map.put(Field.City, "Whole Country");
        map.put(Field.Type, "Country");
        dataList.add(map);
        return map;
    }

    public void main(String[] args) {
        ProxyUtils.enableSocksProxy();

        // TODO !!! Wikipedia datasource !!!

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
        for (City city : City.values()) {
            Map<Field, Object> map = addCity(city);
            map.put(Field.Population, Datahub.getCityPopulation(city));
            map.putAll(Wikipedia.getCity(city));
        }

        GoogleSheetsReport.updateGoogleSheets(dataList);
    }


}
