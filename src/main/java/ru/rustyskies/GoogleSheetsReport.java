package ru.rustyskies;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.Country;
import ru.rustyskies.beans.Field;
import services.GoogleSheetsApi;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

    private static final Field[] COLUMNS = new Field[] {
            Field.Population,
            Field.Area,
            Field.PopulationDensity
    };

    public void updateGoogleSheetsPage(List<Map<Field, Object>> countries) {
        GoogleSheetsApi api = GoogleSheetsApi.INSTANCE;

        // Clearing the target page
        api.clearPage(SPREADSHEET_ID, PAGE_ID);

        // Header
        List<Object> columns = new ArrayList<>();
        columns.add("Country");
        columns.add("City");
        columns.add("Type");
        for (Field f : COLUMNS) {
            columns.add(f.title + " (" + f.unit + ")");
        }
        api.writeRow(SPREADSHEET_ID, PAGE_ID, "A1:1", columns);

        // Countries
        int rowIndex = 2;
        for (Map<Field, Object> c : countries) {
            List<Object> data = new ArrayList<>();
            data.add(c.get(Field.Name)); // Country
            data.add("Whole Country"); // City
            data.add("Country"); // Type
            for (Field f : COLUMNS) {
                data.add(c.get(f));
            }
            api.writeRow(SPREADSHEET_ID, PAGE_ID, "A" + rowIndex + ":" + rowIndex, data);
            rowIndex++;
        }
    }
}
