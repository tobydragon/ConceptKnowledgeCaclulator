package edu.ithaca.dragon.tecmap.io.reader;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PointsOffConverterTest {


    @Test
    void addPointTotalsToQuestionLabelsTest()throws IOException{
        List<String[]> rowsToConvert = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/pointsOffExample.csv");
        PointsOffConverter.addPointTotalsToQuestionLabels(rowsToConvert.get(0), rowsToConvert.get(1));

        List<String[]> correctRowsExample = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/convertedPointsOffExample.csv");
        String[] convertedRow = rowsToConvert.get(0);
        String[] correctRowExample = correctRowsExample.get(0);
        assertEquals(correctRowExample.length, convertedRow.length);
        for (int colIdx=0; colIdx<convertedRow.length; colIdx++){
            assertEquals(correctRowExample[colIdx], convertedRow[colIdx]);
        }

    }

    @Test
    void convertFromPointsOffToTotalPointsTest()throws IOException {
        List<String[]> rowsToConvert = CsvRepresentation.parseRowsFromFile("src/test/resources/singleUseFiles/pointsOffExample.csv");
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