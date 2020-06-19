package ru.rustyskies.beans;

import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum City {

    Singapore("Singapore", Country.Singapore),
    HongKong("Hong Kong", Country.HongKong),
    Moscow("Moscow", Country.Russia, new String[] { "Moskva" }),
    SaintPetersburg("Saint Petersburg", Country.Russia, new String[] { "St. Petersburg" }),
//    Penza("Penza", Country.Russia),
    NewYork("New York", Country.USA, "New_York_City", new String[] { "New York (NY)" }),
    LosAngeles("Los Angeles", Country.USA, new String[] { "Los Angeles (CA)" }),
    Chicago("Chicago", Country.USA, new String[] { "Chicago (IL)" }),
    Houston("Houston", Country.USA, new String[] { "Houston (TX)" }),
    Philadelphia("Philadelphia", Country.USA, new String[] { "Philadelphia (PA)" }),
    Dallas("Dallas", Country.USA, new String[] { "Dallas (TX)" }),
    Phoenix("Phoenix", Country.USA, "Phoenix,_Arizona", new String[] { "Phoenix (AZ)" }),
    BuenosAires("Buenos Aires", Country.Argentina),
//    Cordoba("Córdoba", Country.Argentina, "Córdoba,_Argentina"),
    Rosario("Rosario", Country.Argentina, "Rosario,_Santa_Fe"),
    Berlin("Berlin", Country.Germany),
    Hamburg("Hamburg", Country.Germany),
    Munich("Munich", Country.Germany, new String[] { "München" }),
    Frankfurt("Frankfurt", Country.Germany, new String[] { "Frankfurt am Main" }),
    Cologne("Cologne", Country.Germany, new String[] { "Köln" }),
    Toronto("Toronto", Country.Canada),
    Montreal("Montreal", Country.Canada, new String[] { "Montréal" }),
    Vancouver("Vancouver", Country.Canada),
    Sydney("Sydney", Country.Australia),
    Melbourne("Melbourne", Country.Australia),
    Brisbane("Brisbane", Country.Australia),
    Perth("Perth", Country.Australia),
    Adelaide("Adelaide", Country.Australia),
    Auckland("Auckland", Country.NewZealand),
    Wellington("Wellington", Country.NewZealand),
    Amsterdam("Amsterdam", Country.Netherlands),
    Rotterdam("Rotterdam", Country.Netherlands),
    Santiago("Santiago", Country.Chile),
    Seoul("Seoul", Country.SouthKorea),
    Busan("Busan", Country.SouthKorea, new String[] { "Busan (Pusan)" }),
    Incheon("Incheon", Country.SouthKorea),
    London("London", Country.UK),
    Manchester("Manchester", Country.UK),
    Tokyo("Tokyo", Country.Japan),
    Yokohama("Yokohama", Country.Japan),
    Osaka("Osaka", Country.Japan),
    KualaLumpur("Kuala Lumpur", Country.Malaysia, "Kuala_Lumpur"),
    Shanghai("Shanghai", Country.China),
    Beijing("Beijing", Country.China, new String[] { "Beijing (Peking)" }),
    Guangzhou("Guangzhou", Country.China),
    Chongqing("Chongqing", Country.China),
    Paris("Paris", Country.France),
    Marseille("Marseille", Country.France),
    Lyon("Lyon", Country.France),
    Rome("Rome", Country.Italy, new String[] { "Roma" }),
    Milan("Milan", Country.Italy, new String[] { "Milano" }),
    Naples("Naples", Country.Italy, new String[] { "Napoli" }),
    Vienna("Vienna", Country.Austria, new String[] { "Wien" }),
    Helsinki("Helsinki", Country.Finland),
    Lisbon("Lisbon", Country.Portugal, new String[] { "Lisboa" }),
    Madrid("Madrid", Country.Spain),
    Barcelona("Barcelona", Country.Spain),
    Bilbao("Bilbao", Country.Spain),
    Valencia("Valencia", Country.Spain),
    Athens("Athens", Country.Greece, new String[] { "Athinai" }),
    Stockholm("Stockholm", Country.Sweden),
    Gothenburg("Gothenburg", Country.Sweden, new String[] { "Göteborg" }),
    Oslo("Oslo", Country.Norway),
    Copenhagen("Copenhagen", Country.Denmark, new String[] { "Kobenhavn", "København" }),
    Reykjavik("Reykjavik", Country.Iceland);

    public final String name;
    public final Country country;
    public final String wikipediaArticleName;
    public final Set<String> nameVariations;

    City(String name, Country country, String wikipediaArticleName, String[] nameVariations) {
        this.name = name;
        this.country = country;
        this.wikipediaArticleName = wikipediaArticleName;

        Set<String> s = new HashSet<>(Arrays.asList(nameVariations));
        s.add(name);
        s = s.stream().map(String::toLowerCase).collect(Collectors.toSet());
        this.nameVariations = Collections.unmodifiableSet(s);
    }

    City(String name, Country country) {
        this(name, country, null, new String[] { name.toLowerCase() });
    }

    City(String name, Country country, String wikipediaArticleName) {
        this(name, country, wikipediaArticleName, new String[] { name.toLowerCase() });
    }

    City(String name, Country country, String[] nameVariations) {
        this(name, country, null, nameVariations);
    }

    /** Checks if provided name matches any of current city name variations */
    public boolean nameMatch(String name) {
        return name != null && !name.trim().equals("") && nameVariations.contains(name.toLowerCase());
    }
}
