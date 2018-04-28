package ru.rustyskies;

import com.google.api.services.sheets.v4.model.*;
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

    private static final Field[] HEADER_COLUMNS = new Field[] {
            Field.Country,
            Field.City,
            Field.Type,
            Field.Population,
            Field.Area,
            Field.PopulationDensity
    };

    public void updateGoogleSheets(List<Map<Field, Object>> countries) {
        GoogleSheetsApi api = GoogleSheetsApi.INSTANCE;

        // Clearing the target sheet
        api.clearSheet(SPREADSHEET_ID, SHEET_ID);

        List<Request> requests = new ArrayList<>();

        Sheet sheet = api.getSheetById(SPREADSHEET_ID, SHEET_ID);
        if (sheet == null) {
            throw new RuntimeException("No sheet \"" + SHEET_ID + "\" found at " + SPREADSHEET_ID);
        }

        // Header
        Color headerColor = new Color().setBlue((float) 70).setGreen((float) 70).setRed((float) 70);
        CellFormat headerFormat = new CellFormat().setBackgroundColor(headerColor).setTextFormat(new TextFormat().setBold(true)).setHorizontalAlignment("center");
        CellFormat firstColumnHeaderFormat = new CellFormat().setBackgroundColor(headerColor).setTextFormat(new TextFormat().setBold(true)).setHorizontalAlignment("left");
        for (int i = 0; i < HEADER_COLUMNS.length; i++) {
            Field column = HEADER_COLUMNS[i];

            CellData headerCellData = new CellData();
            if (i == 0) {
                headerCellData.setUserEnteredFormat(firstColumnHeaderFormat);
            } else {
                headerCellData.setUserEnteredFormat(headerFormat);
            }
            if (column.unit != null && !column.unit.trim().equals("")) {
                headerCellData.setUserEnteredValue(new ExtendedValue().setStringValue(column.title + " (" + column.unit + ")"));
            } else {
                headerCellData.setUserEnteredValue(new ExtendedValue().setStringValue(column.title));
            }

            GridRange gr = new GridRange();
            gr.setSheetId(sheet.getProperties().getSheetId());
            gr.setStartRowIndex(0);
            gr.setEndRowIndex(1);
            gr.setStartColumnIndex(i);
            gr.setEndColumnIndex(i + 1);

            requests.add(new Request().setRepeatCell(new RepeatCellRequest()
                    .setCell(headerCellData)
                    .setRange(gr)
                    .setFields("*")));
        }

        // Data
        CellFormat dataFormat = new CellFormat().setHorizontalAlignment("center");
        CellFormat numberFormat = new CellFormat().setNumberFormat(new NumberFormat().setType("NUMBER").setPattern("#,##0")).setHorizontalAlignment("center");
        CellFormat firstColumnDataFormat = new CellFormat().setHorizontalAlignment("left");
        for (int countryIndex = 0; countryIndex < countries.size(); countryIndex++) {
            Map<Field, Object> country = countries.get(countryIndex);

            for (int columnIndex = 0; columnIndex < HEADER_COLUMNS.length; columnIndex++) {
                Field column = HEADER_COLUMNS[columnIndex];

                CellData headerCellData = new CellData();
                CellFormat cellFormat = dataFormat;
                ExtendedValue cellData;
                switch (columnIndex) {
                    case 0:
                        // Country
                        cellFormat = firstColumnDataFormat;
                        cellData = new ExtendedValue().setStringValue(String.valueOf(country.get(Field.Name)));
                        break;
                    case 1:
                        // City
                        cellData = new ExtendedValue().setStringValue("Whole Country");
                        break;
                    case 2:
                        // Type
                        cellData = new ExtendedValue().setStringValue("Country");
                        break;
                    default:
                        switch (column.fieldType) {
                            case Double:
                                cellFormat = numberFormat;
                                cellData = new ExtendedValue().setNumberValue((Double) country.get(column));
                                break;
                            case Integer:
                                cellFormat = numberFormat;
                                cellData = new ExtendedValue().setNumberValue(((Integer) country.get(column)).doubleValue());
                                break;
                            case String:
                                cellData = new ExtendedValue().setStringValue(String.valueOf(country.get(column)));
                                break;
                            default:
                                throw new RuntimeException("Unexpected field type: " + column.fieldType);
                        }
                }
                headerCellData.setUserEnteredFormat(cellFormat);
                headerCellData.setUserEnteredValue(cellData);

                GridRange gr = new GridRange();
                gr.setSheetId(sheet.getProperties().getSheetId());
                gr.setStartRowIndex(countryIndex + 1);
                gr.setEndRowIndex(countryIndex + 2);
                gr.setStartColumnIndex(columnIndex);
                gr.setEndColumnIndex(columnIndex + 1);

                requests.add(new Request().setRepeatCell(new RepeatCellRequest()
                        .setCell(headerCellData)
                        .setRange(gr)
                        .setFields("*")));
            }
        }
        api.executeBatchUpdate(SPREADSHEET_ID, requests);
    }
}
