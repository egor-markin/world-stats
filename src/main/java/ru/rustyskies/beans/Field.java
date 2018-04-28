package ru.rustyskies.beans;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Field {

    Name("Name", "", FieldType.String),
    Country("Country", "", FieldType.String),
    City("City", "", FieldType.String),
    Type("Type", "", FieldType.String),
    LocalName("Local Name", "", FieldType.String),
    Population("Population", "", FieldType.Integer),
    CityPopulation("City population", "", FieldType.Integer),
    MetroPopulation("Metro area population", "", FieldType.Integer),
    PopulationDensity("Density", "/km²", FieldType.Double),
    Area("Area", "km²", FieldType.Double),
    Elevation("Elevation", "m", FieldType.Integer),
    GDP("Nominal Gross Domestic Product (GDP)", "billion $USD", FieldType.Integer),
    GDPpC("Nominal GDP per Capita", "$USD", FieldType.Integer),
    CoLI("Cost of Living Index - Compared to New York City", "", FieldType.Double);

    public final String title;
    public final String unit;
    public final FieldType fieldType;
}
