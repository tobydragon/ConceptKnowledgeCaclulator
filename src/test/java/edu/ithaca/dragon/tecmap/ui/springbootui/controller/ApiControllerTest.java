package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.Settings;
import edu.ithaca.dragon.tecmap.data.TecmapDatastore;
import edu.ithaca.dragon.tecmap.data.TecmapFileDatastore;
import edu.ithaca.dragon.tecmap.io.Json;
import edu.ithaca.dragon.tecmap.io.record.LearningResourceRecord;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyObject;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TecmapService tecmapServiceMock;

    private TecmapService tecmapService;

    @Before
    public void setup() throws IOException {
        TecmapDatastore tecmapDatastore = TecmapFileDatastore.buildFromJsonFile(Settings.DEFAULT_TEST_DATASTORE_FILE, Settings.TEST_ROOT_PATH);

        tecmapService = new TecmapService(tecmapDatastore);
    }

    @Test
    public void getStructureTree() throws Exception {
        String courseId = "Cs1ExampleStructure";
        Mockito.when(tecmapServiceMock.retrieveStructureTree(anyString()))
                .thenReturn(tecmapService.retrieveStructureTree(courseId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/structureTree/Cs1Example").accept(
                 MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.structureAsTreeString;

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveStructureTree(anyString()))
                .thenReturn(tecmapService.retrieveStructureTree(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        expected = Cs1ExampleJsonStrings.structureAsTreeString;

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        //Test Assessment Connected
        courseId = "Cs1Example";
        Mockito.when(tecmapServiceMock.retrieveStructureTree(anyString()))
                .thenReturn(tecmapService.retrieveStructureTree(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();

        expected = Cs1ExampleJsonStrings.structureWithResourceConnectionsAsTree;
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        courseId = "NoPath";
        Mockito.when(tecmapServiceMock.retrieveStructureTree(anyString()))
                .thenReturn(tecmapService.retrieveStructureTree(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void getConceptIdList() throws Exception {
        String courseId = "Cs1ExampleStructure";
        Mockito.when(tecmapServiceMock.retrieveConceptIdList(anyString())).
                thenReturn(tecmapService.retrieveConceptIdList(courseId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/conceptList/thisIsACourse").accept(
                 MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.allConceptsStringAsJson;

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveConceptIdList(anyString())).
                thenReturn(tecmapService.retrieveConceptIdList(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Connected
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveConceptIdList(anyString())).
                thenReturn(tecmapService.retrieveConceptIdList(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Null
        courseId = "NoPath";
        Mockito.when(tecmapServiceMock.retrieveConceptIdList(anyString())).
                thenReturn(tecmapService.retrieveConceptIdList(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void getBlankLearningResourceRecordsFromAssessment() throws Exception {
        String courseId = "Cs1ExampleStructure";
        Mockito.when(tecmapServiceMock.retrieveBlankLearningResourceRecordsFromAssessment(anyString())).
                thenReturn(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/blankLRRecords/thisIsACourse").accept(
                 MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals("[]", result.getResponse().getContentAsString());


        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveBlankLearningResourceRecordsFromAssessment(anyString())).
                thenReturn(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        String expected = Cs1ExampleJsonStrings.assessment1And2Str;

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        //Test Assessment Connected
        courseId = "Cs1Example";
        Mockito.when(tecmapServiceMock.retrieveBlankLearningResourceRecordsFromAssessment(anyString())).
                thenReturn(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        //Test Null
        courseId = "NoPath";
        Mockito.when(tecmapServiceMock.retrieveBlankLearningResourceRecordsFromAssessment(anyString())).
                thenReturn(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals("", result.getResponse().getContentAsString());
    }

    @Test
    public void postConnectedResources() throws Exception{
        String courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.postConnectedResources(anyString(), anyObject()))
                .thenReturn(tecmapService.postConnectedResources(courseId, tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId)));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "/api/connectResources/courseId")
                .accept(MediaType.APPLICATION_JSON)
                .content(Json.toJsonString(tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId)))
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder).andExpect(status().isOk());

        Mockito.when(tecmapServiceMock.postConnectedResources(anyString(), anyObject()))
                .thenReturn(tecmapService.postConnectedResources(courseId, null));

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());

        Mockito.when(tecmapServiceMock.postConnectedResources(anyString(), anyObject()))
                .thenReturn(tecmapService.postConnectedResources(courseId, new ArrayList<LearningResourceRecord>()));

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());

        courseId = "NotAValidID";
        Mockito.when(tecmapServiceMock.postConnectedResources(anyString(), anyObject()))
                .thenReturn(tecmapService.postConnectedResources(courseId, tecmapService.retrieveBlankLearningResourceRecordsFromAssessment(courseId)));

        mockMvc.perform(requestBuilder).andExpect(status().isNotFound());
    }

    @Test
    public void getCohortTree() throws Exception {
        String courseId = "Cs1ExampleStructure";
        Mockito.when(tecmapServiceMock.retrieveCohortTree(anyString())).
                thenReturn(tecmapService.retrieveCohortTree(courseId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/cohortTree/thisIsACourse").accept(
                 MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "";

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveCohortTree(anyString())).
                thenReturn(tecmapService.retrieveCohortTree(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Connected
        courseId = "Cs1Example";
        Mockito.when(tecmapServiceMock.retrieveCohortTree(anyString())).
                thenReturn(tecmapService.retrieveCohortTree(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        expected = Json.toJsonString(tecmapService.retrieveCohortTree(courseId));
        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);

        //Test Null
        courseId = "NoPath";
        Mockito.when(tecmapServiceMock.retrieveCohortTree(anyString())).
                thenReturn(tecmapService.retrieveCohortTree(courseId));

        result = mockMvc.perform(requestBuilder).andReturn();
        expected = "";
        assertEquals(expected, result.getResponse().getContentAsString());
    }

    @Test
    public void getConceptSuggestionsForUser() throws Exception {
        String courseId = "Cs1ExampleStructure";
        String userId = "s01";
        Mockito.when(tecmapServiceMock.retrieveConceptSuggestionsForUser(anyString(), anyString()))
                .thenReturn(tecmapService.retrieveConceptSuggestionsForUser(courseId, userId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/suggestConcepts/courseId/userId").accept(
                MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "";

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveConceptSuggestionsForUser(anyString(), anyString()))
                .thenReturn(tecmapService.retrieveConceptSuggestionsForUser(courseId, userId));

        result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Connected for User s01
        courseId = "Cs1Example";
        Mockito.when(tecmapServiceMock.retrieveConceptSuggestionsForUser(anyString(), anyString()))
                .thenReturn(tecmapService.retrieveConceptSuggestionsForUser(courseId, userId));

        result = mockMvc.perform(requestBuilder).andReturn();

        expected = Json.toJsonString(tecmapService.retrieveConceptSuggestionsForUser(courseId, userId));

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    //TODO: FIX TESTS WHEN SUGGESTINGAPI IS COMPLETE
    public void getResourceSuggestionsForUser() throws Exception {
        String courseId = "Cs1ExampleStructure";
        String userId = "s01";
        Mockito.when(tecmapServiceMock.retrieveResourceSuggestionsForUser(anyString(), anyString()))
                .thenReturn(tecmapService.retrieveResourceSuggestionsForUser(courseId, userId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/suggestResources/courseId/userId").accept(
                MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "";

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveResourceSuggestionsForUser(anyString(), anyString()))
                .thenReturn(tecmapService.retrieveResourceSuggestionsForUser(courseId, userId));

        result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Conncected
        courseId = "Cs1Example";
        Mockito.when(tecmapServiceMock.retrieveResourceSuggestionsForUser(anyString(), anyString()))
                .thenReturn(tecmapService.retrieveResourceSuggestionsForUser(courseId, userId));

        result = mockMvc.perform(requestBuilder).andReturn();

        expected = Json.toJsonString(tecmapService.retrieveResourceSuggestionsForUser(courseId, userId));

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    //TODO: FIX TESTS WHEN SUGGESTINGAPI IS COMPLETE
    public void getResourceSuggestionsForSpecificConceptForUser() throws Exception{
        String courseId = "Cs1ExampleStructure";
        String userId = "s01";
        String conceptId = "Loops";
        Mockito.when(tecmapServiceMock.retrieveResourceSuggestionsForSpecificConceptForUser(anyString(), anyString(), anyString()))
                .thenReturn(tecmapService.retrieveResourceSuggestionsForSpecificConceptForUser(courseId, userId, conceptId));

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/suggestResources/courseId/userId/conceptId").accept(
                MediaType.APPLICATION_JSON);

        //Test Structure
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = "";

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Added
        courseId = "Cs1ExampleAssessmentAdded";
        Mockito.when(tecmapServiceMock.retrieveResourceSuggestionsForSpecificConceptForUser(anyString(), anyString(), anyString()))
                .thenReturn(tecmapService.retrieveResourceSuggestionsForSpecificConceptForUser(courseId, userId, conceptId));

        result = mockMvc.perform(requestBuilder).andReturn();

        assertEquals(expected, result.getResponse().getContentAsString());

        //Test Assessment Conncected
        courseId = "Cs1Example";
        Mockito.when(tecmapServiceMock.retrieveResourceSuggestionsForSpecificConceptForUser(anyString(), anyString(), anyString()))
                .thenReturn(tecmapService.retrieveResourceSuggestionsForSpecificConceptForUser(courseId, userId, conceptId));

        result = mockMvc.perform(requestBuilder).andReturn();

        expected = Json.toJsonString(tecmapService.retrieveResourceSuggestionsForSpecificConceptForUser(courseId, userId, conceptId));

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    public void getValidIdsandActions() throws Exception {
        Mockito.when(tecmapServiceMock.retrieveValidIdsAndActions())
                .thenReturn(tecmapService.retrieveValidIdsAndActions());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/api/actions").accept(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Json.toJsonString(tecmapService.retrieveValidIdsAndActions());

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }
}
