package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {

    Name("Name", "", FieldType.String, null, null, "", ""),
    Country("Country", "", FieldType.String, null, null, "", ""),
    City("City", "", FieldType.String, null, null, "", ""),
    Type("Type", "", FieldType.String, null, null, "", ""),
    LocalName("Local Name", "", FieldType.String, null, null, "", ""),
    Population("Population", "", FieldType.Integer, null, null, "", ""),
    CityPopulation("City population", "", FieldType.Integer, null, null, "", ""),
    MetroPopulation("Metro area population", "", FieldType.Integer, null, null, "", ""),
    PopulationDensity("Density", "/km²", FieldType.Double, null, null, "", ""),
    Area("Area", "km²", FieldType.Double, null, null, "", ""),
    Elevation("Elevation", "m", FieldType.Double, null, null, "", ""),
    GDP("GDP", "$USD", FieldType.Currency, "billion", null, "Nominal Gross Domestic Product", ""),
    GDPPPP("GDPPPP", "$USD", FieldType.Currency, null, null, "Gross Domestic Product at Purchasing Power Parity", ""),
    GDPpC("GDPpC", "$USD", FieldType.Currency, null, null, "Nominal Gross Domestic Product per Capita", ""),
    GDPPPPpC("GDPPPPpC", "$USD", FieldType.Currency, null, null, "Gross Domestic Product at Purchasing Power Parity per Capita", ""),
    CoL("CoL", "", FieldType.Double, null, "#,##0.00", "Cost of Living (excluding Rent) Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Rent("Rent", "", FieldType.Double, null, "#,##0.00", "Rent Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    CoLRent("CoL+Rent", "", FieldType.Double, null, "#,##0.00", "Cost of Living Plus Rent Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Groceries("Groceries", "", FieldType.Double, null, "#,##0.00", "Groceries Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Restaurants("Restaurants", "", FieldType.Double, null, "#,##0.00", "Restaurants Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    LPP("LPP", "", FieldType.Double, null, "#,##0.00", "Local Purchasing Power Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Crime("Crime", "", FieldType.Double, null, "#,##0.00", "Crime Index", "https://www.numbeo.com/crime/indices_explained.jsp"),
    Safety("Safety", "", FieldType.Double, null, "#,##0.00", "Safety index", "https://www.numbeo.com/crime/indices_explained.jsp"),
    HealthCare("HealthCare", "", FieldType.Double, null, "#,##0.00", "Health Care index", "https://www.numbeo.com/health-care/indices_explained.jsp"),
    Pollution("Pollution", "", FieldType.Double, null, "#,##0.00", "Pollution Index", "https://www.numbeo.com/pollution/indices_explained.jsp"),
    Traffic("Traffic", "", FieldType.Double, null, "#,##0.00", "Traffic Index", "https://www.numbeo.com/traffic/indices_explained.jsp"),
    Climate("Climate", "", FieldType.Double, null, "#,##0.00", "Climate Index", "https://www.numbeo.com/climate/indices_explained.jsp"),
    QoL("QoL", "", FieldType.Double, null, "#,##0.00", "Quality of Life Index", "https://www.numbeo.com/quality-of-life/indices_explained.jsp"),
    Gini("Gini", "", FieldType.Double, null, null, "Gini coefficient - measurement of nation's residents inequality", "https://en.wikipedia.org/wiki/Gini_coefficient"),
    HDI("HDI", "", FieldType.Double, null, null, "Human Development Index - composite statistic index of life expectancy, education, and per capita income indicators", "https://en.wikipedia.org/wiki/Human_Development_Index"),
    TimeZone("TimeZone", "", FieldType.String, null, null, "", "https://en.wikipedia.org/wiki/Time_zone"),
    Coords("Coordinates", "", FieldType.Coordinates, null, null, "GPS Coordinates", "https://en.wikipedia.org/wiki/Geographic_coordinate_system");


    public final String title;
    public final String unit;
    public final FieldType fieldType;
    public final String defaultSuffix;
    public final String fieldNumberFormatPattern;
    public final String description;
    public final String url;
}
