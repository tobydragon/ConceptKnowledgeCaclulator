package edu.ithaca.dragon.tecmap.io.reader;

import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointsOffConverterTest {


    @Test
    void convertFromPointsOffToTotalPointsTest()throws IOException, CsvException  {
        List<String[]> rowsToConvert = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/pointsOffExample.csv");
        ConvertToSakaiLabel.addPointTotalsToQuestionLabels(rowsToConvert.get(0), rowsToConvert.get(1), 2);
        PointsOffConverter.convertFromPointsOffToTotalPoints(rowsToConvert);

        List<String[]> correctRowsExample = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/convertedPointsOffExample.csv");
        assertEquals(correctRowsExample.size(), rowsToConvert.size());
        for (int rowIdx=0; rowIdx < rowsToConvert.size(); rowIdx++){
            String[] convertedRow = rowsToConvert.get(rowIdx);
            String[] correctRowExample = correctRowsExample.get(rowIdx);
            assertEquals(correctRowExample.length, convertedRow.length);
            for (int colIdx=0; colIdx<convertedRow.length; colIdx++){
                assertEquals(correctRowExample[colIdx], convertedRow[colIdx]);
            }
        }
    }

}