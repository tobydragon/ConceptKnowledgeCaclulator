package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CanvasConverterTest {


    @Test
    void canvasConverterTest1() throws IOException, CsvException {
        // test combination of swap name and id and create max score (order not matter)
        List<String[]> newRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/canvasConverterTest1.csv");
        SwapNameAndIDColumn.swapNameAndIDColumn(newRows);
        CreateMaxScoreRow.createMaxScoreRow(newRows);

        List<String[]> orgRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/CanvasGradeExample.csv");

        for (int row = 0; row < orgRows.size(); row++) {
            for (int column = 0; column < orgRows.get(row).length; column++) {
                assertEquals(orgRows.get(row)[column], newRows.get(row)[column]);
            }
        }
    }

    @Test
    void canvasConverterTest2() throws IOException, CsvException {
        // test combination of move gradeStartColumnIndex and create max score (order not matter)
        List<String[]> newRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/canvasConverterTest2.csv");
        MoveGradeStartColumnIndex.moveGradeStartColumnIndex(newRows);
        CreateMaxScoreRow.createMaxScoreRow(newRows);

        List<String[]> orgRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/CorrectGradeStartColumnIndex.csv");

        for (int row = 0; row < orgRows.size(); row++) {
            for (int column = 0; column < orgRows.get(row).length; column++) {
                assertEquals(orgRows.get(row)[column], newRows.get(row)[column]);
            }
        }
    }

    @Test
    void canvasConverterTest3() throws IOException, CsvException {
        // test combination of swap name and id and move gradeStartColumnIndex (order not matter)
        List<String[]> newRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/canvasConverterTest3.csv");
        SwapNameAndIDColumn.swapNameAndIDColumn(newRows);
        MoveGradeStartColumnIndex.moveGradeStartColumnIndex(newRows);

        List<String[]> orgRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/CorrectGradeStartColumnIndex.csv");

        for (int row = 0; row < orgRows.size(); row++) {
            for (int column = 0; column < orgRows.get(row).length; column++) {
                assertEquals(orgRows.get(row)[column], newRows.get(row)[column]);
            }
        }
    }

    @Test
    void canvasConverterTest() throws IOException, CsvException {
        // test all 3
        List<String[]> newRows = CsvFileLibrary.parseRowsFromFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/assessmentGrades.csv");
        CanvasConverter.canvasConverter(newRows);

        List<String[]> orgRows = CsvFileLibrary.parseRowsFromFile(Settings.TEST_RESOURCE_DIR + "comp220_Summer2024/gradesCanvasFormat.csv");

        for (int row = 0; row < orgRows.size(); row++) {
            for (int column = 0; column < orgRows.get(row).length; column++) {
                assertEquals(orgRows.get(row)[column], newRows.get(row)[column]);
            }
        }
    }
}
