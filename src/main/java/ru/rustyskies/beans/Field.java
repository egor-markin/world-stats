package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {

    Name("Name", "", FieldType.String, null, "", ""),
    Country("Country", "", FieldType.String, null, "", ""),
    City("City", "", FieldType.String, null, "", ""),
    Type("Type", "", FieldType.String, null, "", ""),
    LocalName("Local Name", "", FieldType.String, null, "", ""),
    Population("Population", "", FieldType.Integer, null, "", ""),
    CityPopulation("City population", "", FieldType.Integer, null, "", ""),
    MetroPopulation("Metro area population", "", FieldType.Integer, null, "", ""),
    PopulationDensity("Density", "/km²", FieldType.Double, null, "", ""),
    Area("Area", "km²", FieldType.Double, null, "", ""),
    Elevation("Elevation", "m", FieldType.Integer, null, "", ""),
    GDP("GDP", "$USD", FieldType.Double, null, "Nominal Gross Domestic Product", ""),
    GDPpC("GDPpC", "$USD", FieldType.Double, null, "Nominal Gross Domestic Product per Capita", ""),
    CoL("CoL", "", FieldType.Double, "#,##0.00", "Cost of Living (excluding Rent) Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Rent("Rent", "", FieldType.Double, "#,##0.00", "Rent Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    CoLRent("CoL+Rent", "", FieldType.Double, "#,##0.00", "Cost of Living Plus Rent Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Groceries("Groceries", "", FieldType.Double, "#,##0.00", "Groceries Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Restaurants("Restaurants", "", FieldType.Double, "#,##0.00", "Restaurants Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    LPP("LPP", "", FieldType.Double, "#,##0.00", "Local Purchasing Power Index - Compared to New York City", "https://www.numbeo.com/cost-of-living/cpi_explained.jsp"),
    Crime("Crime", "", FieldType.Double, "#,##0.00", "Crime Index", "https://www.numbeo.com/crime/indices_explained.jsp"),
    Safety("Safety", "", FieldType.Double, "#,##0.00", "Safety index", "https://www.numbeo.com/crime/indices_explained.jsp"),
    HealthCare("HealthCare", "", FieldType.Double, "#,##0.00", "Health Care index", "https://www.numbeo.com/health-care/indices_explained.jsp"),
    Pollution("Pollution", "", FieldType.Double, "#,##0.00", "Pollution Index", "https://www.numbeo.com/pollution/indices_explained.jsp"),
    Traffic("Traffic", "", FieldType.Double, "#,##0.00", "Traffic Index", "https://www.numbeo.com/traffic/indices_explained.jsp"),
    Climate("Climate", "", FieldType.Double, "#,##0.00", "Climate Index", "https://www.numbeo.com/climate/indices_explained.jsp"),
    QoL("QoL", "", FieldType.Double, "#,##0.00", "Quality of Life Index", "https://www.numbeo.com/quality-of-life/indices_explained.jsp");

    public final String title;
    public final String unit;
    public final FieldType fieldType;
    public final String fieldNumberFormatPattern;
    public final String description;
    public final String url;
}
