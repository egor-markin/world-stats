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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Egor Markin
 * @since 16.09.2017
 */
@Slf4j
public enum GoogleSheetsApi {

    INSTANCE;

    // Based on https://developers.google.com/sheets/api/quickstart/java

    private static final String APPLICATION_NAME = "EmigrationResearch";
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

    public ClearValuesResponse clearPage(String sheetId, String page) {
        try {
            return VALUES.clear(sheetId, page, new ClearValuesRequest()).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<List<Object>> readRange(String sheetId, String page, String range) {
        try {
            return VALUES.get(sheetId, page + "!" + range).execute().getValues();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Object readCell(String sheetId, String page, String cell) {
        List<List<Object>> data = readRange(sheetId, page, cell);
        if (data != null && !data.isEmpty()) {
            List<Object> subData = data.get(0);
            if (subData != null && !subData.isEmpty()) {
                return subData.get(0);
            }
        }
        return null;
    }

    public UpdateValuesResponse writeRange(String sheetId, String page, String range, List<List<Object>> data) {
        try {
            return VALUES.update(sheetId, page + "!" + range,   new ValueRange().setValues(data)).setValueInputOption("USER_ENTERED").execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public UpdateValuesResponse writeColumn(String sheetId, String page, String columnRange, List<Object> rows) {
        List<List<Object>> data = new ArrayList<>(rows.size());
        for (Object o : rows) {
            data.add(Collections.singletonList(o));
        }
        return writeRange(sheetId, page, columnRange, data);
    }

    public UpdateValuesResponse writeRow(String sheetId, String page, String rowRange, List<Object> columns) {
        return writeRange(sheetId, page, rowRange, Collections.singletonList(columns));
    }

    public UpdateValuesResponse writeCell(String sheetId, String page, String cell, Object data) {
        return writeRange(sheetId, page, cell, Collections.singletonList(Collections.singletonList(data)));
    }

    public BatchUpdateSpreadsheetResponse updateCellFormat(String sheetId, String page, String range, CellFormat cellFormat) {
        // https://stackoverflow.com/questions/40263978/how-to-use-google-java-sheet-api-v4-to-format-cells-as-number-and-date
        // https://stackoverflow.com/questions/37986171/google-sheet-api-v4java-append-date-in-cells
        List<CellData> cellData = new ArrayList<>();
        cellData.add(new CellData().setUserEnteredFormat(cellFormat));

        List<RowData> rowData = new ArrayList<>();
        rowData.add(new RowData().setValues(cellData));

        UpdateCellsRequest requestBody = new UpdateCellsRequest();
        requestBody.setRows(rowData);
//        requestBody.setRange(new GridRange().setSheetId(1).s);
        // TODO How to convert page+range to indexes?

        BatchUpdateSpreadsheetRequest batchRequests = new BatchUpdateSpreadsheetRequest();
        batchRequests.setRequests(Collections.singletonList(new Request().setUpdateCells(requestBody)));

        try {
            return SHEETS.spreadsheets().batchUpdate(sheetId, batchRequests).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {
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
