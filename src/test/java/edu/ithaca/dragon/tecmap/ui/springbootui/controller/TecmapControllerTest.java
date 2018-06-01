package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.Tecmap;
import edu.ithaca.dragon.tecmap.io.writer.Json;
import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(TecmapController.class)
public class TecmapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TecmapService tecmapServiceMock;

    private TecmapService onlyStructureTecmapService;
    private TecmapService twoAssessmentsAddedTecmapService;
    private TecmapService twoAssessmentsConnectedTecmapService;

    @Before
    public void setup() throws IOException {
        Tecmap onlyStructureTecmap = new Tecmap(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json");
        Tecmap twoAssessmentsAddedTecmap = new Tecmap(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
                )
        );

        Tecmap twoAssessmentsConnectedTecmap = new Tecmap(
                Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleGraph.json",
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleResources.json")),
                new ArrayList<>(Arrays.asList(Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment1.csv",
                        Settings.TEST_RESOURCE_DIR + "tecmapExamples/Cs1ExampleAssessment2.csv")
                )
        );
        onlyStructureTecmapService = new TecmapService(onlyStructureTecmap);
        twoAssessmentsAddedTecmapService = new TecmapService(twoAssessmentsAddedTecmap);
        twoAssessmentsConnectedTecmapService = new TecmapService(twoAssessmentsConnectedTecmap);
    }

    @Test
    public void getStructureTree() throws Exception {
        Mockito.when(tecmapServiceMock.retrieveStructureTree())
                .thenReturn(onlyStructureTecmapService.retrieveStructureTree());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/map/structureTree").accept(
                 MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.structureAsTreeString;

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getConceptIdList() throws Exception {
        Mockito.when(tecmapServiceMock.retrieveConceptIdList()).
                thenReturn(onlyStructureTecmapService.retrieveConceptIdList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/map/conceptList").accept(
                 MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.allConceptsStringAsJson;

        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    public void getBlankLearningResourceRecordsFromAssessment() throws Exception {
        Mockito.when(tecmapServiceMock.retrieveBlankLearningResourceRecordsFromAssessment()).
                thenReturn(twoAssessmentsAddedTecmapService.retrieveBlankLearningResourceRecordsFromAssessment());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/map/blankLRRecords").accept(
                 MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.assessment1And2Str;

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getCohortTree() throws Exception {
        Mockito.when(tecmapServiceMock.retrieveCohortTree()).
                thenReturn(twoAssessmentsConnectedTecmapService.retrieveCohortTree());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/map/cohortTree").accept(
                 MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Json.toJsonString(twoAssessmentsConnectedTecmapService.retrieveCohortTree());

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }
}
