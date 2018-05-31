package edu.ithaca.dragon.tecmap.ui.springbootui.controller;

import edu.ithaca.dragon.tecmap.tecmapExamples.Cs1ExampleJsonStrings;
import edu.ithaca.dragon.tecmap.ui.springbootui.service.TecmapService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(TecmapController.class)
public class TecmapControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TecmapService tecmapServiceMock;

    private TecmapService tecmapService;

    @Test
    public void getStructureTree() throws Exception {
        tecmapService = new TecmapService();
        
        Mockito.when(tecmapServiceMock.retrieveStructureTree())
                .thenReturn(tecmapService.retrieveStructureTree());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/map/structureTree").accept(
                 MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.structureAsTreeString;

        JSONAssert.assertEquals(expected, result.getResponse().getContentAsString(), false);
    }

    @Test
    public void getConceptIdList() throws Exception {
        tecmapService = new TecmapService();

        Mockito.when(tecmapServiceMock.retrieveConceptIdList()).
                thenReturn(tecmapService.retrieveConceptIdList());

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "/map/conceptList").accept(
                 MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        String expected = Cs1ExampleJsonStrings.allConceptsString;

        assertEquals(expected, result.getResponse().getContentAsString());
    }
}
