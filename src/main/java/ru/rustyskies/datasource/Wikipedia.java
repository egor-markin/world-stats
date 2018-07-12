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

    public Map<Field, Object> getCity(City city) {
        if (!citiesFieldsMap.containsKey(city)) {
            log.warn("No fields mapping for the city found: " + city);
            return new HashMap<>();
        }
        return WikipediaWikitextParser.getCityInfobox(city, citiesFieldsMap.get(city));
    }

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
        fieldsMap.put(Field.Gini, "Gini");
        fieldsMap.put(Field.HDI, "HDI");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Hong Kong
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.HongKong, fieldsMap);
        fieldsMap.put(Field.Area, "area_km2");
        fieldsMap.put(Field.CityPopulation, "population_estimate");
        fieldsMap.put(Field.GDPPPP, "GDP_PPP");
        fieldsMap.put(Field.GDPPPPpC, "GDP_PPP_per_capita");
        fieldsMap.put(Field.GDP, "GDP_nominal");
        fieldsMap.put(Field.GDPpC, "GDP_nominal_per_capita");
        fieldsMap.put(Field.Gini, "Gini");
        fieldsMap.put(Field.HDI, "HDI");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Moscow
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Moscow, fieldsMap);
        fieldsMap.put(Field.LocalName, "ru_name");
        fieldsMap.put(Field.Area, "area_km2");
        fieldsMap.put(Field.CityPopulation, "pop_2010census");
        fieldsMap.put(Field.Coords, "coordinates");

        // Saint Petersburg
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.SaintPetersburg, fieldsMap);
        fieldsMap.put(Field.LocalName, "ru_name");
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.CityPopulation, "pop_latest");
        fieldsMap.put(Field.Area, "area_km2");

        // Penza
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Penza, fieldsMap);
        fieldsMap.put(Field.LocalName, "ru_name");
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.CityPopulation, "pop_latest");

        // New York
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.NewYork, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_metro_sq_mi");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_est");
        fieldsMap.put(Field.TimeZone, "utc_offset1");

        // Los Angeles
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.LosAngeles, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_est");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Chicago
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Chicago, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_ft");
        fieldsMap.put(Field.CityPopulation, "population_est");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Houston
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Houston, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_ft");
        fieldsMap.put(Field.CityPopulation, "population_est");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Philadelphia
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Philadelphia, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_total");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Dallas
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Dallas, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_est");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Phoenix
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Phoenix, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_est");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // Buenos Aires
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.BuenosAires, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_urban");
        fieldsMap.put(Field.TimeZone, "utc_offset1");

        // Cordoba
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Cordoba, fieldsMap);
        fieldsMap.put(Field.LocalName, "name");
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_land_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_urban");
        fieldsMap.put(Field.TimeZone, "utc_offset1");

        // Rosario
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Rosario, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_blank1_km2");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.CityPopulation, "population_urban");
        fieldsMap.put(Field.TimeZone, "utc_offset1");

        // Berlin
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Berlin, fieldsMap);
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.Elevation, "elevation");
        fieldsMap.put(Field.CityPopulation, "population");
        fieldsMap.put(Field.MetroPopulation, "pop_metro");
        fieldsMap.put(Field.GDP, "GDP");
        fieldsMap.put(Field.GDPpC, "GDP_per_capita");
        fieldsMap.put(Field.Coords, "coordinates");

        // Hamburg
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Hamburg, fieldsMap);
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "population");
        fieldsMap.put(Field.MetroPopulation, "pop_metro");
        fieldsMap.put(Field.GDP, "GDP");
        fieldsMap.put(Field.GDPpC, "GDP_per_capita");
        fieldsMap.put(Field.Coords, "coordinates");

        // Munich
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Munich, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.LocalName, "German_name");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "population");
        fieldsMap.put(Field.Elevation, "elevation");

        // Frankfurt
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Frankfurt, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.LocalName, "name");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "pop_urban");
        fieldsMap.put(Field.MetroPopulation, "pop_metro");
        fieldsMap.put(Field.Elevation, "elevation");

        // Cologne
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Cologne, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.LocalName, "German_name");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "population");
        fieldsMap.put(Field.MetroPopulation, "pop_metro");
        fieldsMap.put(Field.Elevation, "elevation");

        // Toronto
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Toronto, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.CityPopulation, "population_total");
        fieldsMap.put(Field.MetroPopulation, "population_metro");
        fieldsMap.put(Field.Elevation, "elevation_m");
        fieldsMap.put(Field.TimeZone, "utc_offset");
        fieldsMap.put(Field.GDP, "blank_info_sec2");
        fieldsMap.put(Field.GDPpC, "blank1_info_sec2");

        // Montreal
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Montreal, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.CityPopulation, "population_total");
        fieldsMap.put(Field.MetroPopulation, "population_metro");
        fieldsMap.put(Field.Elevation, "elevation_max_m");
        fieldsMap.put(Field.TimeZone, "utc_offset");
        fieldsMap.put(Field.GDP, "blank_info_sec2");
        fieldsMap.put(Field.GDPpC, "blank1_info_sec2");

        // Vancouver
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Vancouver, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_total_km2");
        fieldsMap.put(Field.CityPopulation, "population_total");
        fieldsMap.put(Field.MetroPopulation, "population_metro");
        fieldsMap.put(Field.TimeZone, "utc_offset");
        fieldsMap.put(Field.GDP, "blank_info_sec2");
        fieldsMap.put(Field.GDPpC, "blank1_info_sec2");

        // Sydney
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Sydney, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "pop");
        fieldsMap.put(Field.TimeZone, "utc");

        // Melbourne
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Melbourne, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "pop");
        fieldsMap.put(Field.TimeZone, "utc");
        fieldsMap.put(Field.Elevation, "elevation");

        // Brisbane
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Brisbane, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "pop");
        fieldsMap.put(Field.TimeZone, "utc");

        // Perth
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Perth, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "pop");
        fieldsMap.put(Field.TimeZone, "utc");

        // Adelaide
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Adelaide, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area");
        fieldsMap.put(Field.CityPopulation, "pop");
        fieldsMap.put(Field.TimeZone, "utc");

        // Auckland
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Auckland, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_urban_km2");
        fieldsMap.put(Field.TimeZone, "utc_offset");
        fieldsMap.put(Field.Elevation, "elevation_max_m");

        // Wellington
        fieldsMap = new HashMap<>();
        citiesFieldsMap.put(City.Wellington, fieldsMap);
        fieldsMap.put(Field.Coords, "coordinates");
        fieldsMap.put(Field.Area, "area_urban_km2");
        fieldsMap.put(Field.Elevation, "elevation_max_m");
        fieldsMap.put(Field.TimeZone, "utc_offset");

        // TODO !!! Add all the cities !!!
    }

    public static void main(String[] args) {
        City city = City.Wellington;
        System.out.println(getCity(city));
        WikipediaWikitextParser.printCityInfobox(city);
    }
}
