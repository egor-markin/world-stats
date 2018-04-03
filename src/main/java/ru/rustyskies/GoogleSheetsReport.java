package ru.rustyskies;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.Country;
import services.GoogleSheetsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Egor Markin
 * @since 16.09.2017
 */
@Slf4j
@UtilityClass
public class GoogleSheetsReport {

    private static final String SPREADSHEET_ID = "1ZZ2Jd8ART6gqHbX_j4S8ZBI5DRvRodmxTfEJCZ_lZ8Q";

    private static final String PAGE_ID = "Sheet2";

    private static final String[] COLUMNS = new String[] {
            "Name",
            "City",
            "Type",
            "Population"
    };

    public void updateGoogleSheetsPage() {
        GoogleSheetsApi api = GoogleSheetsApi.INSTANCE;

        // Clearing the target page
        api.clearPage(SPREADSHEET_ID, PAGE_ID);

        // Header row
        api.writeRow(SPREADSHEET_ID, PAGE_ID, "A1:1", new ArrayList<>(Arrays.asList(COLUMNS)));

        // Countries column
        List<String> countries = Arrays.stream(Country.values()).map(Country::getTitle).collect(Collectors.toList());
        api.writeColumn(SPREADSHEET_ID, PAGE_ID, "A2:A", new ArrayList<>(countries));

        // Population
//        RestCountriesEU.INSTANCE.countries
        api.writeColumn(SPREADSHEET_ID, PAGE_ID, "D2:D", new ArrayList<>());
    }
}
