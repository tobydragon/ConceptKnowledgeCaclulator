'use strict';

describe("ConceptMatrixFunctionsSpecs", function() {
    var resourceRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");

    it("readJson for resourceRecords", function() {
        expect(resourceRecords.length).toEqual(10);
        expect(resourceRecords[0].learningResourceId).toEqual("Q1");
    });
    
    it("updateResourceRecords to add a connection", function() {  
      updateResourceRecords(resourceRecords, "Q1", "While Loops");
      expect(resourceRecords[0].conceptIds).toEqual(["If Statements", "While Loops"]);
    });
    
    it("updateResourceRecords to remove a connection", function() {  
      updateResourceRecords(resourceRecords, "Q2", "While Loops");
      expect(resourceRecords[1].conceptIds.length).toEqual(0);
    });
    
    it("createResourceIdsList", function() {  
      var resourceIds = createResourceIdsList(resourceRecords);
      expect(resourceIds).toEqual(["Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"]);
    });
    
    it("createResourceCheckedListForConcept", function() {  
      var resourceIds = createResourceCheckedListForConcept("For Loops", resourceRecords);
      expect(resourceIds).toEqual([false, false, true, false, false, false, false, false, false, true]);
    });
    
    it("buildTableHeaderHtmlString", function(){
        expect(buildTableHeaderHtmlString(["Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"]))
              .toEqual("<thead><tr><th class='col-xs-1' scope='col'></th><th class='col-xs-1' scope='col'> Q1 </th><th class='col-xs-1' scope='col'> Q2 </th><th class='col-xs-1' scope='col'> Q3 </th><th class='col-xs-1' scope='col'> Q4 </th><th class='col-xs-1' scope='col'> Q5 </th><th class='col-xs-1' scope='col'> HW1 </th><th class='col-xs-1' scope='col'> HW2 </th><th class='col-xs-1' scope='col'> HW3 </th><th class='col-xs-1' scope='col'> HW4 </th><th class='col-xs-1' scope='col'> HW5 </th></tr></thead>");
    });
    
    it("buildCheckboxHtmlString unchecked box", function(){
        expect(buildTableCheckboxHtmlString("If Statements", "Q1", false)).toEqual("<td class='text-center'><input id='If Statements_Q1' type='checkbox' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q1')'></td>");
    });
    
    it("buildCheckboxHtmlString checked box", function(){
        expect(buildTableCheckboxHtmlString("If Statements", "Q1", true)).toEqual("<td class='text-center'><input id='If Statements_Q1' type='checkbox' checked='true' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q1')'></td>");
    });
    
    it("buildTableRowHtmlString", function(){
        console.log(buildTableRowHtmlString("If Statements", ["Q1", "Q2", "Q3"], [true, false, false]));
        expect(buildTableRowHtmlString("If Statements", ["Q1", "Q2", "Q3"], [true, false, false]))
              .toEqual("<tr><th scope='row'>If Statements</th><td class='text-center'><input id='If Statements_Q1' type='checkbox' checked='true' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q1')'></td><td class='text-center'><input id='If Statements_Q2' type='checkbox' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q2')'></td><td class='text-center'><input id='If Statements_Q3' type='checkbox' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q3')'></td></tr>");
    });
    
//    it("buildTableBodyHtmlString", function(){
//        var conceptList = ["While Loops","For Loops","Boolean Expressions","Intro CS", "Loops", "If Statements"];
//        console.log(buildTableBodyHtmlString(conceptList, resourceRecords));
//        expect(buildTableBodyHtmlString(conceptList, resourceRecords))
//              .toContain("<tr><th scope='row'>If Statements</th><td class='text-center'><input id='If Statements_Q1' type='checkbox' checked='true' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q1')'></td><td class='text-center'><input id='If Statements_Q2' type='checkbox' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q2')'></td><td class='text-center'><input id='If Statements_Q3' type='checkbox' onclick='updateResourceRecords(resourceRecords, 'If Statements', 'Q3')'></td></tr>");
//    });
});

