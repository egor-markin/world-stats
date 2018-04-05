package ru.rustyskies.datasource;

import com.google.gson.Gson;
import lombok.Data;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Egor Markin
 * @since 16.09.2017
 */
public enum RestCountriesEU {

    INSTANCE;

    // Based on https://restcountries.eu/

    private static final String URL = "https://restcountries.eu/rest/v2/all";
    private static final String RESOURCE_FILE = "/restcountries.eu.json";
    private static final boolean USE_CACHED_FILE = false;

    @Data
    public static class Country {
        public String name;

        public int population;

        public double area;

        public String capital;

        public String region;

        public String subregion;

        public double gini;

        public String alpha2Code;

        public String alpha3Code;
    }

//    public final Country[] countries;

    private final Map<String, Country> countriesMap;

    private final List<String> countryCodes;

    RestCountriesEU() {
        String jsonText;
        if (!USE_CACHED_FILE) {
            try {
                jsonText = IOUtils.toString(new URI(URL), Charset.forName("UTF-8"));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        } else {
            InputStream inputStream = getClass().getResourceAsStream(RESOURCE_FILE);
            if (inputStream == null) {
                throw new RuntimeException("Unable to locate resource file: " + RESOURCE_FILE);
            }
            try {
                jsonText = IOUtils.toString(inputStream, Charset.forName("UTF-8"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Country[] countries = new Gson().fromJson(jsonText, Country[].class);

        countriesMap = Arrays.stream(countries).collect(Collectors.toMap(Country::getAlpha3Code, Function.identity()));
        countryCodes = Arrays.stream(countries).map(s -> s.alpha3Code).collect(Collectors.toList());
    }

    public List<String> getCountryCodes() {
        return countryCodes;
    }

    public Country getCountry(String alpha3Code) {
        return countriesMap.get(alpha3Code);
    }

    public static void main(String args[]) {
//        System.out.println(INSTANCE.getCountryCodes());
//        System.out.println(INSTANCE.getCountry("RUS"));

        for (String code : INSTANCE.getCountryCodes()) {
            System.out.println(code + " - " + INSTANCE.getCountry(code));
        }
    }
}
