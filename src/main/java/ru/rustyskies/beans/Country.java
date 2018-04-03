package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Country {
    Russia("Russia", "RUS"),
    Germany("Germany", "GER");

    public String title;
    public String code;


//    private static final String[] COUNTRIES = new String[] {
//            "Russia",
//            "USA",
//            "Argentina",
//            "Germany",
//            "Canada",
//            "Australia",
//            "New Zealand",
//            "Netherlands",
//            "Chile",
//            "South Korea",
//            "UK",
//            "Japan",
//            "Singapore",
//            "Malaysia",
//            "Hong Kong",
//            "China",
//            "France",
//            "Italy",
//            "Austria",
//            "Finland",
//            "Portugal",
//            "Spain",
//            "Greece",
//            "Sweden",
//            "Norway",
//            "Denmark",
//            "Iceland",
//            "Cyprus"
//    };
}
