package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {
    Russia("Russia", "RUS"),
    USA("USA", "USA"),
    Argentina("Argentina", "ARG"),
    Germany("Germany", "DEU"),
    Canada("Canada", "CAN"),
    Australia("Australia", "AUS"),
    NewZealand("New Zealand", "NZL"),
    Netherlands("Netherlands", "NLD"),
    Chile("Chile", "CHL"),
    SouthKorea("South Korea (Republic of Korea)", "KOR"),
    UK("UK", "GBR"),
    Japan("Japan", "JPN"),
    Singapore("Singapore", "SGP"),
    Malaysia("Malaysia", "MYS"),
    HongKong("Hong Kong", "HKG"),
    China("China", "CHN"),
    France("France", "FRA"),
    Italy("Italy", "ITA"),
    Austria("Austria", "AUT"),
    Finland("Finland", "FIN"),
    Portugal("Portugal", "PRT"),
    Spain("Spain", "ESP"),
    Greece("Greece", "GRC"),
    Sweden("Sweden", "SWE"),
    Norway("Norway", "NOR"),
    Denmark("Denmark", "DNK"),
    Iceland("Iceland", "ISL"),
    Cyprus("Cyprus", "CYP");

    public String name;
    public String alpha3Code;
}
