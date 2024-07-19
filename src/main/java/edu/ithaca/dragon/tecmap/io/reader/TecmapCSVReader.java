package edu.ithaca.dragon.tecmap.io.reader;

/**
 * TecmapCSVReader is the parent function that is responsible for the reading in CSV files, it is created when
 * a child class calls to it giving file specific parameters
 * Created by willsuchanek on 3/6/17.
 */

import edu.ithaca.dragon.tecmap.learningresource.AssessmentItem;
import edu.ithaca.dragon.tecmap.learningresource.AssessmentItemResponse;
import edu.ithaca.dragon.tecmap.learningresource.ManualGradedResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public abstract class TecmapCSVReader {
    static Logger logger = LogManager.getLogger(TecmapCSVReader.class);

    List<AssessmentItem> columnItemList;
    List<AssessmentItemResponse> manualGradedResponseList;
    List<String> studentNames;

    /**
     * This function is passed a filename of a gradebook directly exported from Canvas's built in gradebook.
     * (See DataCSVExample.csv in test/testresources/io for proper file format example)
     * @param rows a list of rows from the file
     * @param processors a list of csv processors
     */
    public TecmapCSVReader(List<String[]> rows, List<CsvProcessor> processors) throws IOException{
        manualGradedResponseList = new ArrayList<>();
        columnItemList = new ArrayList<>();
        studentNames = new ArrayList<>();
        readFiles(rows, processors);
    }

    public List<AssessmentItemResponse> getManualGradedResponses(){return this.manualGradedResponseList;}

    public List<AssessmentItem> getManualGradedLearningObjects(){return this.columnItemList;}

    public abstract String getIDForAStudent(String[] dataLine);

    public abstract String getNameForAStudent(String[] dataLine);

    /**
     * This function takes a list of student data and creates a manual graded response for that user
     * @param row - a row in the file
     */
    public void createManualGradedResponseList(String[] row) throws Exception {
        int i = 4;
        String stdID = getIDForAStudent(row);
        if (columnItemList.size() + 4 < row.length) {
            logger.error("More data than learning objects on line for id: {}", stdID);
        } else if (columnItemList.size() + 4 > row.length) {
            logger.warn("More learning objects than data on line for id: {}", stdID);
        }
        //need to make sure we don't go out of bounds on either list
        while (i < row.length && i < columnItemList.size() + 4) {
            AssessmentItem currentColumnItem = this.columnItemList.get(i - 4);
            String qid = currentColumnItem.getId();
            if (!("".equals(row[i]))) {
                double studentGrade;
                String number = ReaderTools.pullNumber(row[i]);
                if (!number.isEmpty()){
                    studentGrade = Double.parseDouble(number);
                }
                // raise exception if there is anything other than numbers
                else {
                    throw new Exception("Typos where grades are expected.");
                }
                ManualGradedResponse response = new ManualGradedResponse(qid, currentColumnItem.getMaxPossibleKnowledgeEstimate(), studentGrade, stdID);
                currentColumnItem.addResponse(response);
                this.manualGradedResponseList.add(response);
            }
            i++;
        }
    }

    public void readFiles(List<String[]> rows, List<CsvProcessor> processors) {
//      The first list in the list of lists is the assessments (questions) so we go through the first line and
//      pull out all the learning objects and put them into the columnItemList
        if (!processors.isEmpty()) {
            for (CsvProcessor processor : processors) {
                processor.processRows(rows);
            }
        }
        columnItemList = ReaderTools.assessmentItemsFromList(rows);
        for (int i = 3; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length > 1) {
                studentNames.add(getNameForAStudent(row));
                try {
                    //goes through and adds all the questions to their proper learning object, as well as adds them to
                    //the general list of manual graded responses
                    createManualGradedResponseList(rows.get(i));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}