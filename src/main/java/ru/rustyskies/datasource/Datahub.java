package ru.rustyskies.datasource;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.IOUtils;
import ru.rustyskies.beans.City;
import ru.rustyskies.tools.HttpUtils;
import ru.rustyskies.tools.ParseUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @since 17.05.2018
 * @author Egor Markin
 */
@Slf4j
@UtilityClass
public class Datahub {

    private static final String BASE_URL = "https://pkgstore.datahub.io/core/";

    public Integer getCityPopulation(City city) {
        AtomicReference<Integer> result = new AtomicReference<>();
        HttpUtils.getPageByUrl(BASE_URL + "population-city/unsd-citypopulation-year-both_csv/data/72bffcc522e04029d93dd8c236c44fad/unsd-citypopulation-year-both_csv.csv", inputStream -> {
            CSVParser parser = null;
            try {
                parser = CSVParser.parse(inputStream, Charset.forName("UTF-8"), CSVFormat.RFC4180);
                for (CSVRecord record : parser) {
                    String countryName = record.get(0);
                    String cityName = record.get(4);

                    if (city.nameMatch(cityName) && city.getCountry().nameMatch(countryName)) {
                        result.set(ParseUtils.parseInt(record.get(9)));
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                IOUtils.closeQuietly(parser);
            }
        });
        return result.get();
    }

    public static void main(String[] args) {
        // TODO Add pages cache
        log.debug(getCityPopulation(City.Berlin) + "");
    }

}
