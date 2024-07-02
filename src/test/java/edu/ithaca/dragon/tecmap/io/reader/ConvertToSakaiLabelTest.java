package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import edu.ithaca.dragon.tecmap.Settings;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConvertToSakaiLabelTest {

    @Test
    void addPointTotalsToQuestionLabelsTest()throws IOException, CsvException {
        List<String[]> rowsToConvert = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/pointsOffExample.csv");
        ConvertToSakaiLabel.addPointTotalsToQuestionLabels(rowsToConvert.get(0), rowsToConvert.get(1), 2);

        List<String[]> correctRowsExample = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/convertedPointsOffExample.csv");
        String[] convertedRow = rowsToConvert.get(0);
        String[] correctRowExample = correctRowsExample.get(0);
        assertEquals(correctRowExample.length, convertedRow.length);
        for (int colIdx=0; colIdx<convertedRow.length; colIdx++){
            assertEquals(correctRowExample[colIdx], convertedRow[colIdx]);
        }
    }


    @Test
    void addPointTotalsToQuestionLabelsCanvasTest() throws IOException, CsvException {
        List<String[]> rowsToConvert = CsvRepresentation.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH+"CanvasFiles/CanvasGradeExample.csv");
        ConvertToSakaiLabel.addPointTotalsToQuestionLabels(rowsToConvert.get(0), rowsToConvert.get(2), 4);

        List<String[]> correctRowsExample = CsvRepresentation.parseRowsFromFile(Settings.DEFAULT_TEST_DATASTORE_PATH+"CanvasFiles/convertedCanvasGradeExample.csv");
        String[] convertedRow = rowsToConvert.get(0);
        String[] correctRowExample = correctRowsExample.get(0);
        assertEquals(correctRowExample.length, convertedRow.length);
        for (int colIdx=0; colIdx<convertedRow.length; colIdx++){
            assertEquals(correctRowExample[colIdx], convertedRow[colIdx]);
        }
    }
}
