package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public enum Country {
    Russia("Russia", "RU", "RUS", new String[] { "Russian Federation" }),
    USA("United States", "US", "USA", new String[] { "United States of America" }),
    Argentina("Argentina", "AR", "ARG"),
    Germany("Germany", "DE", "DEU"),
    Canada("Canada", "CA", "CAN"),
    Australia("Australia", "AU", "AUS"),
    NewZealand("New Zealand", "NZ", "NZL"),
    Netherlands("Netherlands", "NL", "NLD"),
    Chile("Chile", "CL", "CHL"),
    SouthKorea("South Korea", "KR", "KOR", new String[] { "Republic of Korea" }),
    UK("United Kingdom", "GB", "GBR", new String[] { "United Kingdom of Great Britain and Northern Ireland" }),
    Japan("Japan", "JP", "JPN"),
    Singapore("Singapore", "SG", "SGP"),
    Malaysia("Malaysia", "MY", "MYS"),
    HongKong("Hong Kong", "HK", "HKG"),
    China("China", "CN", "CHN"),
    France("France", "FR", "FRA"),
    Italy("Italy", "IT", "ITA"),
    Austria("Austria", "AU", "AUT"),
    Finland("Finland", "FI", "FIN"),
    Portugal("Portugal", "PT", "PRT"),
    Spain("Spain", "ES", "ESP"),
    Greece("Greece", "GR", "GRC"),
    Sweden("Sweden", "SE", "SWE"),
    Norway("Norway", "NO", "NOR"),
    Denmark("Denmark", "DK", "DNK"),
    Iceland("Iceland", "IS", "ISL"),
    Cyprus("Cyprus", "CY", "CYP"),
    Estonia("Estonia", "EE", "EST"),
    Latvia("Latvia", "LV", "LVA"),
    Lithuania("Lithuania", "LT", "LTU"),
    Switzerland("Switzerland", "CH", "CHE");

    public final String name;
    public final String iso2Code;
    public final String alpha3Code;
    public final Set<String> nameVariations;

    Country(String name, String iso2Code, String alpha3Code) {
        this.name = name;
        this.iso2Code = iso2Code;
        this.alpha3Code = alpha3Code;
        nameVariations = Collections.singleton(name.toLowerCase());
    }

    Country(String name, String iso2Code, String alpha3Code, String[] nameVariations) {
        this.name = name;
        this.iso2Code = iso2Code;
        this.alpha3Code = alpha3Code;

        Set<String> s = new HashSet<>(Arrays.asList(nameVariations));
        s.add(name);
        s = s.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.nameVariations = Collections.unmodifiableSet(s);
    }

    /** Checks if provided name matches any of current city name variations */
    public boolean nameMatch(String name) {
        return name != null && !name.trim().equals("") && nameVariations.contains(name.toLowerCase());
    }
}
