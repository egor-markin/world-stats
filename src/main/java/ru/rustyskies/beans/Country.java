package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {
    Russia("Russia", "RU", "RUS"),
    USA("United States", "US", "USA"),
    Argentina("Argentina", "AR", "ARG"),
    Germany("Germany", "DE", "DEU"),
    Canada("Canada", "CA", "CAN"),
    Australia("Australia", "AU", "AUS"),
    NewZealand("New Zealand", "NZ", "NZL"),
    Netherlands("Netherlands", "NL", "NLD"),
    Chile("Chile", "CL", "CHL"),
    SouthKorea("South Korea", "KR", "KOR"),
    UK("United Kingdom", "GB", "GBR"),
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

    public String name;
    public String iso2Code;
    public String alpha3Code;
}
