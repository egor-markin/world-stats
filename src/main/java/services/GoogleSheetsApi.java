package services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import lombok.extern.slf4j.Slf4j;
import ru.rustyskies.GoogleSheetsReport;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Egor Markin
 * @since 16.09.2017
 */
@Slf4j
public enum GoogleSheetsApi {

    INSTANCE;

    // Based on https://developers.google.com/sheets/api/quickstart/java

    private static final String APPLICATION_NAME = "world-stats";
    private static final String OAUTH_SECRET_FILE = "/client_secret.json";

    private final FileDataStoreFactory DATA_STORE_FACTORY;

    private final HttpTransport HTTP_TRANSPORT;

    private final JsonFactory JSON_FACTORY;

    private final Sheets SHEETS;

    private final Sheets.Spreadsheets.Values VALUES;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/SHEETS.googleapis.com-java-quickstart
     */
    private final List<String> SCOPES;

    GoogleSheetsApi() {
        try {
            SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
            JSON_FACTORY = JacksonFactory.getDefaultInstance();
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            /* Directory to store user credentials for this application. */
            File dataStoreDir = new File(System.getProperty("user.home"),
                    ".credentials/sheets.googleapis.com-java-" + APPLICATION_NAME);
            DATA_STORE_FACTORY = new FileDataStoreFactory(dataStoreDir);
            SHEETS = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize()).setApplicationName(APPLICATION_NAME).build();
            VALUES = SHEETS.spreadsheets().values();
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
    }

    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream inputStream = getClass().getResourceAsStream(OAUTH_SECRET_FILE);
        if (inputStream == null) {
            throw new RuntimeException("Unable to locate OAuth Secret file (" + OAUTH_SECRET_FILE + ") in project's resources. Fix the path or create a new file: https://developers.google.com/SHEETS/api/quickstart/java");
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(inputStream));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        // System.out.println("Credentials saved to " + DATA_STORE_FACTORY.getDataDirectory().getAbsolutePath());
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public ClearValuesResponse clearSheet(String spreadsheetId, String sheetId) {
        try {
            return VALUES.clear(spreadsheetId, sheetId, new ClearValuesRequest()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Object>> readRange(String spreadsheetId, String sheetId, String range) {
        try {
            return VALUES.get(spreadsheetId, sheetId + "!" + range).execute().getValues();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object readCell(String spreadsheetId, String sheetId, String cell) {
        List<List<Object>> data = readRange(spreadsheetId, sheetId, cell);
        if (data != null && !data.isEmpty()) {
            List<Object> subData = data.get(0);
            if (subData != null && !subData.isEmpty()) {
                return subData.get(0);
            }
        }
        return null;
    }

    public UpdateValuesResponse writeRange(String spreadsheetId, String sheetId, String range, List<List<Object>> data) {
        try {
            return VALUES.update(spreadsheetId, sheetId + "!" + range, new ValueRange().setValues(data)).setValueInputOption("USER_ENTERED").execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateValuesResponse writeColumn(String spreadsheetId, String sheetId, String columnRange, List<Object> rows) {
        List<List<Object>> data = new ArrayList<>(rows.size());
        for (Object o : rows) {
            data.add(Collections.singletonList(o));
        }
        return writeRange(spreadsheetId, sheetId, columnRange, data);
    }

    public UpdateValuesResponse writeRow(String spreadsheetId, String sheetId, String rowRange, List<Object> columns) {
        return writeRange(spreadsheetId, sheetId, rowRange, Collections.singletonList(columns));
    }

    public UpdateValuesResponse writeCell(String spreadsheetId, String sheetId, String cell, Object data) {
        return writeRange(spreadsheetId, sheetId, cell, Collections.singletonList(Collections.singletonList(data)));
    }

    public BatchUpdateSpreadsheetResponse updateCellFormat(String spreadsheetId, String sheetId, String range, CellFormat cellFormat) {
        // https://stackoverflow.com/questions/40263978/how-to-use-google-java-sheet-api-v4-to-format-cells-as-number-and-date
        // https://stackoverflow.com/questions/37986171/google-sheet-api-v4java-append-date-in-cells
        List<CellData> cellData = new ArrayList<>();
        cellData.add(new CellData().setUserEnteredFormat(cellFormat));

        // TODO Join writeRange & this method since this method also writes data

        List<RowData> rowData = new ArrayList<>();
        rowData.add(new RowData().setValues(cellData));

        UpdateCellsRequest requestBody = new UpdateCellsRequest();
        requestBody.setRows(rowData);
        requestBody.setRange(convertRange(spreadsheetId, sheetId, range));
        requestBody.setFields("*");

        BatchUpdateSpreadsheetRequest batchRequests = new BatchUpdateSpreadsheetRequest();
        batchRequests.setRequests(Collections.singletonList(new Request().setUpdateCells(requestBody)));

        try {
            return SHEETS.spreadsheets().batchUpdate(spreadsheetId, batchRequests).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Sheet getSheetById(String spreadsheetId, String sheetId) {
        try {
            Spreadsheet spreadsheet = SHEETS.spreadsheets().get(spreadsheetId).setFields("sheets.properties").execute();
            for (Sheet sheet : spreadsheet.getSheets()) {
                if (sheet.getProperties().getTitle().equals(sheetId)) {
                    return sheet;
                }
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private GridRange convertRange(String spreadsheetId, String sheetId, String range) {
        // https://developers.google.com/resources/api-libraries/documentation/sheets/v4/java/latest/com/google/api/services/sheets/v4/model/GridRange.html
        // https://tanaikech.github.io/2017/07/31/converting-a1notation-to-gridrange-for-google-sheets-api/
        // https://github.com/Tsar/Spreadsheet/blob/master/Spreadsheet.py#L131

        Sheet sheet = getSheetById(spreadsheetId, sheetId);
        if (sheet == null) {
            throw new RuntimeException("No sheet \"" + sheetId + "\" found at " + spreadsheetId);
        }

        Matcher matcher = Pattern.compile("([a-zA-Z]+)(\\d+):([a-zA-Z]*)(\\d*)").matcher(range);
        if (!matcher.find()) {
            throw new RuntimeException("Incorrect range formatted string: " + range);
        }

        GridRange gr = new GridRange();

        gr.setSheetId(sheet.getProperties().getSheetId());

        // Start column
        int sc = stringToNumber(matcher.group(1));
        gr.setStartColumnIndex(sc == 0 ? 0 : sc - 1);

        // End column
        int ec = stringToNumber(matcher.group(3));
        gr.setEndColumnIndex(ec == 0 ? 10 : ec);

        // Start row
        int sr = parseInt(matcher.group(2));
        gr.setStartRowIndex(sr == 0 ? 0 : sr - 1);

        // End row
        int er = parseInt(matcher.group(4));
        gr.setEndRowIndex(er == 0 ? 10 : er);

        // DEBUG
//        System.out.println(range + " -> " + gr);
        // DEBUG

        return gr;
    }

    private int parseInt(String intStr) {
        if (intStr != null && !intStr.trim().equals("")) {
            return Integer.parseInt(intStr);
        }
        return 0;
    }

    /** Converts a string to a decimal number considering it as a 26-base number (eg. A -> 1, AA -> 27, AAA -> 703 etc). Case insensitive.*/
    private int stringToNumber(String str) {
        if (str == null || str.trim().equals("")) {
            return 0;
        }
        int sum = 0;
        for (int i = 0; i < str.length(); i++) {
            sum += ((int) Character.toUpperCase(str.charAt(i)) - 64) * (int) Math.pow(26, str.length() - 1 - i);
        }
        return sum;
    }

    public static void main(String[] args) {
        GridRange gr = INSTANCE.convertRange(GoogleSheetsReport.SPREADSHEET_ID, GoogleSheetsReport.SHEET_ID, "A1:A1");
        System.out.println(gr);
    }

    public static void main2(String[] args) {
        String range = "A1:A1";
        Matcher matcher = Pattern.compile("([a-zA-Z]+)(\\d+):([a-zA-Z]*)(\\d*)").matcher(range);
        System.out.println("Group count: " + matcher.groupCount());
        if (matcher.find()) {
            for (int i = 0; i <= matcher.groupCount(); i++) {
                if (i % 2 == 0) {
                    System.out.println(matcher.group(i));
                } else {
                    System.out.println(matcher.group(i).toUpperCase() + " -> " + GoogleSheetsApi.INSTANCE.stringToNumber(matcher.group(i)));
                }
            }
        } else {
            System.out.println("nothing found");
        }
    }

    public static void main1(String[] args) throws IOException {
        final String spreadsheetId = "1ZZ2Jd8ART6gqHbX_j4S8ZBI5DRvRodmxTfEJCZ_lZ8Q";

        // Writing
        List<List<Object>> values = Collections.singletonList(Collections.singletonList("ccc"));
        UpdateValuesResponse response = GoogleSheetsApi.INSTANCE.writeRange(spreadsheetId, "TestSheet", "A1", values);
        log.info(response.toPrettyString());

        // Reading
        List<List<Object>> data = GoogleSheetsApi.INSTANCE.readRange(spreadsheetId, "Sheet1", "A:D");
        if (data == null || data.size() == 0) {
            log.info("No data found.");
        } else {
            for (List rowValues : data) {
                for (Object cell : rowValues) {
                    log.info(cell + " - " + cell.getClass());
                }
            }
        }
    }


}
