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
    SaintPetersburg("Saint Petersburg", Country.Russia),
    Penza("Penza", Country.Russia),
    NewYork("New York", Country.USA),
    LosAngeles("Los Angeles", Country.USA),
    Chicago("Chicago", Country.USA),
    Houston("Houston", Country.USA),
    Philadelphia("Philadelphia", Country.USA),
    Dallas("Dallas", Country.USA),
    Phoenix("Phoenix", Country.USA),
    BuenosAires("Buenos Aires", Country.Argentina),
    Cordoba("Córdoba", Country.Argentina),
    Rosario("Rosario", Country.Argentina),
    Berlin("Berlin", Country.Germany),
    Hamburg("Hamburg", Country.Germany),
    Munich("Munich", Country.Germany),
    Frankfurt("Frankfurt", Country.Germany),
    Cologne("Cologne", Country.Germany),
    Toronto("Toronto", Country.Canada),
    Montreal("Montreal", Country.Canada),
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
    Busan("Busan", Country.SouthKorea),
    Incheon("Incheon", Country.SouthKorea),
    London("London", Country.UK),
    Manchester("Manchester", Country.UK),
    Birmingham("Birmingham", Country.UK),
    Tokyo("Tokyo", Country.Japan),
    Yokohama("Yokohama", Country.Japan),
    Osaka("Osaka", Country.Japan),
    KualaLumpur("Kuala Lumpur", Country.Malaysia),
    Shanghai("Shanghai", Country.China),
    Beijing("Beijing", Country.China),
    Guangzhou("Guangzhou", Country.China),
    Chongqing("Chongqing", Country.China),
    Paris("Paris", Country.France),
    Marseille("Marseille", Country.France),
    Lyon("Lyon", Country.France),
    Rome("Rome", Country.Italy),
    Milan("Milan", Country.Italy),
    Naples("Naples", Country.Italy),
    Vienna("Vienna", Country.Austria),
    Helsinki("Helsinki", Country.Finland),
    Lisbon("Lisbon", Country.Portugal),
    Madrid("Madrid", Country.Spain),
    Barselona("Barselona", Country.Spain),
    Bilbao("Bilbao", Country.Spain),
    Valencia("Valencia", Country.Spain),
    Athenes("Athenes", Country.Greece),
    Stockholm("Stockholm", Country.Sweden),
    Gothenburg("Gothenburg", Country.Sweden),
    Oslo("Oslo", Country.Norway),
    Copenhagen("Copenhagen", Country.Denmark),
    Reykjavik("Reykjavik", Country.Iceland);

    public final String name;
    public final Country country;
    public final Set<String> nameVariations;

    City(String name, Country country) {
        this.name = name;
        this.country = country;
        nameVariations = Collections.singleton(name.toLowerCase());
    }

    City(String name, Country country, String[] nameVariations) {
        this.name = name;
        this.country = country;

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
