package ru.rustyskies;

import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.Field;
import services.GoogleSheetsApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Egor Markin
 * @since 16.09.2017
 */
@Slf4j
@UtilityClass
public class GoogleSheetsReport {

    public static final String SPREADSHEET_ID = "1ZZ2Jd8ART6gqHbX_j4S8ZBI5DRvRodmxTfEJCZ_lZ8Q";

    public static final String SHEET_ID = "Sheet2";

    private static final Field[] COLUMNS = new Field[] {
            Field.Population,
            Field.Area,
            Field.PopulationDensity
    };

    public void updateGoogleSheets(List<Map<Field, Object>> countries) {
        GoogleSheetsApi api = GoogleSheetsApi.INSTANCE;

        // Clearing the target sheet
        api.clearSheet(SPREADSHEET_ID, SHEET_ID);

        // Header
        List<Object> columns = new ArrayList<>();
        columns.add("Country");
        columns.add("City");
        columns.add("Type");
        for (Field f : COLUMNS) {
            if (f.unit != null && !f.unit.trim().equals("")) {
                columns.add(f.title + " (" + f.unit + ")");
            } else {
                columns.add(f.title);
            }
        }
        api.writeRow(SPREADSHEET_ID, SHEET_ID, "A1:1", columns);
        api.updateCellFormat(SPREADSHEET_ID, SHEET_ID, "A1:1", new CellFormat().setBackgroundColor(new Color().setBlue((float) 137).setGreen((float) 137).setRed((float) 137)));

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
            api.writeRow(SPREADSHEET_ID, SHEET_ID, "A" + rowIndex + ":" + rowIndex, data);
            rowIndex++;
        }
    }
}
