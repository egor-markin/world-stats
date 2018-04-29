package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {

    Name("Name", "", FieldType.String, "", ""),
    Country("Country", "", FieldType.String, "", ""),
    City("City", "", FieldType.String, "", ""),
    Type("Type", "", FieldType.String, "", ""),
    LocalName("Local Name", "", FieldType.String, "", ""),
    Population("Population", "", FieldType.Integer, "", ""),
    CityPopulation("City population", "", FieldType.Integer, "", ""),
    MetroPopulation("Metro area population", "", FieldType.Integer, "", ""),
    PopulationDensity("Density", "/km²", FieldType.Double, "", ""),
    Area("Area", "km²", FieldType.Double, "", ""),
    Elevation("Elevation", "m", FieldType.Integer, "", ""),
    GDP("GDP", "$USD", FieldType.Double, "Nominal Gross Domestic Product", ""),
    GDPpC("GDPpC", "$USD", FieldType.Double, "Nominal Gross Domestic Product per Capita", ""),
    CoL("CoL", "", FieldType.Double, "Cost of Living (excluding Rent) Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Rent("Rent", "", FieldType.Double, "Rent Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    CoLRent("CoL+Rent", "", FieldType.Double, "Cost of Living Plus Rent Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Groceries("Groceries", "", FieldType.Double, "Groceries Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Restaurants("Restaurants", "", FieldType.Double, "Restaurants Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    LPP("LPP", "", FieldType.Double, "Local Purchasing Power Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Crime("Crime", "", FieldType.Double, "Crime Index", "https://www.numbeo.com/crime/indices_explained.jsp"),
    Safety("Safety", "", FieldType.Double, "Safety index", "https://www.numbeo.com/crime/indices_explained.jsp"),
    HealthCare("HealthCare", "", FieldType.Double, "Health Care index", "https://www.numbeo.com/health-care/indices_explained.jsp");

    public final String title;
    public final String unit;
    public final FieldType fieldType;
    public final String description;
    public final String url;
}
