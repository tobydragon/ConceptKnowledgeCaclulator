/**package edu.ithaca.dragonlab.ckc.io;

import edu.ithaca.dragonlab.ckc.learningobject.LearningObjectResponse;
import org.junit.Test;
import java.io.IOException;

import java.util.List;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by willsuchanek on 2/28/17.
 *
public class CSVOutputterTest {

    @Test
    public void questionsToSortedSetTest(){
        String inputXML = "test/testdata/CSVOutputterTest.xml";

        //Get behaviors from runsetone xml
        XmlFragment runestoneFrag = XmlFragment.getFragmentFromLocalFile(inputXML);
        CfInteractionData testCf = CfInteractionDataParser.fromRunestoneXml(runestoneFrag);

        List<CfAction> allActions = testCf.getCfActions();

        //Creates problem summaries from user actions
        ObjectSummaryIdentifier myIdentifier = new ObjectSummaryIdentifier();
        List<String> involvedUsers = AnalysisActions.getOriginatingUsernames(allActions);
        List<LearningObjectResponse> summaries = myIdentifier.buildPerUserPerProblemSummaries(allActions, involvedUsers);

        CSVOutputter sumSet = new CSVOutputter(summaries);

        //Creates a SortedSet with the unique question names in the XML files
        SortedSet<String> testCase = new TreeSet<String>();
        testCase.add("test_question6_4_3");
        testCase.add("test_question6_4_2");
        testCase.add("test_question6_4_1");
        testCase.add("test_question6_3_1");
        testCase.add("test_question6_2_1");
        testCase.add("test_question6_1_1");
        Assert.assertEquals(testCase,CSVOutputter.questionsToSortedSet(sumSet.studentsToQuestions));

    }

    @Test
    public void makeCSVTest(){
        String inputXML = "test/testdata/CSVOutputterTest.xml";

        //Get behaviors from runsetone xml
        XmlFragment runestoneFrag = XmlFragment.getFragmentFromLocalFile(inputXML);
        CfInteractionData testCf = CfInteractionDataParser.fromRunestoneXml(runestoneFrag);

        List<CfAction> allActions = testCf.getCfActions();

        //Creates problem summaries from user actions
        ObjectSummaryIdentifier myIdentifier = new ObjectSummaryIdentifier();
        List<String> involvedUsers = AnalysisActions.getOriginatingUsernames(allActions);
        List<LearningObjectResponse> summaries = myIdentifier.buildPerUserPerProblemSummaries(allActions, involvedUsers);

        CSVOutputter sumSet = new CSVOutputter(summaries);

        String testString = ",test_question6_1_1,test_question6_2_1,test_question6_3_1,test_question6_4_1,test_question6_4_2,test_question6_4_3,\n"
                +"CLTestStudent2,,,1,1,1,1,\n"
                +"CLTestStudent1,0,1,,0,,,\n";
        System.out.println(sumSet.makeCSV());
        System.out.println(testString);
        Assert.assertEquals(testString, sumSet.makeCSV());
    }

    public static void main(String[] args){
        String inputXML = "test/testdata/CSVOutputterTest.xml";

        //Get behaviors from runsetone xml
        XmlFragment runestoneFrag = XmlFragment.getFragmentFromLocalFile(inputXML);
        CfInteractionData testCf = CfInteractionDataParser.fromRunestoneXml(runestoneFrag);

        List<CfAction> allActions = testCf.getCfActions();

        //Creates problem summaries from user actions
        ObjectSummaryIdentifier myIdentifier = new ObjectSummaryIdentifier();
        List<String> involvedUsers = AnalysisActions.getOriginatingUsernames(allActions);
        List<LearningObjectResponse> summaries = myIdentifier.buildPerUserPerProblemSummaries(allActions, involvedUsers);

        try {
            CSVOutputter sumSet = new CSVOutputter("testCSVfile",summaries);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
*/