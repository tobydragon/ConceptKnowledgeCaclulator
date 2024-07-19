package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoveGradeStartColumnIndexTest {

    @Test
    void moveGradeStartColumnIndexTest() throws IOException, CsvException {
        List<String[]> newRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/WrongGradeStartColumnIndex.csv");
        MoveGradeStartColumnIndex.moveGradeStartColumnIndex(newRows);

        List<String[]> orgRows = CsvFileLibrary.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH + "ConvertToCanvasFiles/CorrectGradeStartColumnIndex.csv");

        for (int row = 0; row < orgRows.size(); row++) {
            for (int column = 0; column < orgRows.get(row).length; column++) {
                assertEquals(orgRows.get(row)[column], newRows.get(row)[column]);
            }
        }
    }
}
