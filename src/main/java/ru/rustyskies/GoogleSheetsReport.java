package ru.rustyskies;

import com.google.api.services.sheets.v4.model.*;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.beans.Field;
import ru.rustyskies.tools.GoogleSheetsApi;

import java.util.ArrayList;
import java.util.Arrays;
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

    private static final List<Field> PREDEFINED_HEADER_COLUMNS = Arrays.asList(
            Field.Country,
            Field.City,
            Field.Type
    );

    private static final Float[] HEADER_COLOR_RGB = new Float[] { 70f, 70f, 70f };

    public void updateGoogleSheets(List<Map<Field, Object>> countries) {
        GoogleSheetsApi api = GoogleSheetsApi.INSTANCE;

        List<Request> requests = new ArrayList<>();

        Sheet sheet = api.getSheetById(SPREADSHEET_ID, SHEET_ID);
        if (sheet == null) {
            throw new RuntimeException("No sheet \"" + SHEET_ID + "\" found at " + SPREADSHEET_ID);
        }

        // Columns list
        List<Field> columns = new ArrayList<>(PREDEFINED_HEADER_COLUMNS);
        if (!countries.isEmpty()) {
            columns.addAll(countries.get(0).keySet());
        } else {
            log.warn("No countries were provided");
            return;
        }

        // Header
        Color headerColor = new Color().setRed(HEADER_COLOR_RGB[0]).setGreen(HEADER_COLOR_RGB[1]).setBlue(HEADER_COLOR_RGB[2]);
        CellFormat headerFormat = new CellFormat().setBackgroundColor(headerColor).setTextFormat(new TextFormat().setBold(true)).setHorizontalAlignment("center");
        CellFormat firstColumnHeaderFormat = new CellFormat().setBackgroundColor(headerColor).setTextFormat(new TextFormat().setBold(true)).setHorizontalAlignment("left");

        int columnIndex2 = 0; // Index for the sheet - it might be different from columnIndex because of predefined columns
        for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
            Field column = columns.get(columnIndex);

            // Skipping all the predefined columns
            if (columnIndex >= PREDEFINED_HEADER_COLUMNS.size()) {
                if (PREDEFINED_HEADER_COLUMNS.contains(column)) {
                    continue;
                }
            }

            // Format
            CellData headerCellData = new CellData();
            if (columnIndex == 0) {
                headerCellData.setUserEnteredFormat(firstColumnHeaderFormat);
            } else {
                headerCellData.setUserEnteredFormat(headerFormat);
            }

            // Value & hyperlink
            String value;
            if (column.unit != null && !column.unit.trim().equals("")) {
                value = column.title + " (" + column.unit + ")";
            } else {
                value = column.title;
            }
            if (column.url != null && !column.url.trim().equals("")) {
                headerCellData.setUserEnteredValue(new ExtendedValue().setFormulaValue("=HYPERLINK(\"" + column.url + "\";\"" + value + "\")"));
            } else {
                headerCellData.setUserEnteredValue(new ExtendedValue().setStringValue(value));
            }

            // Description
            if (column.description != null && !column.description.trim().equals("")) {
                headerCellData.setNote(column.description);
            }

            GridRange gr = new GridRange();
            gr.setSheetId(sheet.getProperties().getSheetId());
            gr.setStartRowIndex(0);
            gr.setEndRowIndex(1);
            gr.setStartColumnIndex(columnIndex2);
            gr.setEndColumnIndex(columnIndex2 + 1);
            columnIndex2++;

            requests.add(new Request().setRepeatCell(new RepeatCellRequest()
                    .setCell(headerCellData)
                    .setRange(gr)
                    .setFields("*")));
        }

        // Data
        CellFormat firstColumnDataFormat = new CellFormat().setHorizontalAlignment("left");
        for (int countryIndex = 0; countryIndex < countries.size(); countryIndex++) {
            Map<Field, Object> country = countries.get(countryIndex);

            columnIndex2 = 0; // Index for the sheet - it might be different from columnIndex because of predefined columns
            for (int columnIndex = 0; columnIndex < columns.size(); columnIndex++) {
                Field column = columns.get(columnIndex);

                // Skipping all the predefined columns
                if (columnIndex >= PREDEFINED_HEADER_COLUMNS.size()) {
                    if (PREDEFINED_HEADER_COLUMNS.contains(column)) {
                        continue;
                    }
                }

                Object value = country.get(column);

                CellData headerCellData = new CellData();
                CellFormat cellFormat;
                if (value instanceof Integer || value instanceof Double) {
                    NumberFormat nf = new NumberFormat().setType("NUMBER");
                    if (column.getFieldNumberFormatPattern() != null && !column.getFieldNumberFormatPattern().trim().equals("")) {
                        nf.setPattern(column.getFieldNumberFormatPattern());
                    } else {
                        nf.setPattern("#,##0");
                    }
                    cellFormat = new CellFormat().setNumberFormat(nf).setHorizontalAlignment("center");
                } else {
                    cellFormat = new CellFormat().setHorizontalAlignment("center");
                }

                ExtendedValue cellData;
                switch (columnIndex) {
                    case 0:
                        // Country
                        cellFormat = firstColumnDataFormat;
                        cellData = new ExtendedValue().setStringValue(String.valueOf(country.get(Field.Country)));
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
                        if (value == null) {
                            cellData = new ExtendedValue().setStringValue("");
                        } else if (value instanceof String) {
                            cellData = new ExtendedValue().setStringValue((String) country.get(column));
                        } else if (value instanceof Integer) {
                            cellData = new ExtendedValue().setNumberValue(((Integer) country.get(column)).doubleValue());
                        } else if (value instanceof Double) {
                            cellData = new ExtendedValue().setNumberValue((Double) country.get(column));
                        } else {
                            throw new RuntimeException("Unexpected field type: " + value.getClass() + " for column " + column);
                        }
                }
                headerCellData.setUserEnteredFormat(cellFormat);
                headerCellData.setUserEnteredValue(cellData);

                GridRange gr = new GridRange();
                gr.setSheetId(sheet.getProperties().getSheetId());
                gr.setStartRowIndex(countryIndex + 1);
                gr.setEndRowIndex(countryIndex + 2);
                gr.setStartColumnIndex(columnIndex2);
                gr.setEndColumnIndex(columnIndex2 + 1);
                columnIndex2++;

                requests.add(new Request().setRepeatCell(new RepeatCellRequest()
                        .setCell(headerCellData)
                        .setRange(gr)
                        .setFields("*")));
            }
        }

        // Clearing the target sheet
        api.clearSheet(SPREADSHEET_ID, SHEET_ID);

        // Applying new data
        api.executeBatchUpdate(SPREADSHEET_ID, requests);
    }
}
