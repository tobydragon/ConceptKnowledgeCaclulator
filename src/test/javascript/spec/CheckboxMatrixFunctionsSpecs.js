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

    it("addResourceToRecords adding a new resource", function() {
        expect(resourceRecords.length).toEqual(10);
        addResourceToRecords(resourceRecords, "newResource", 1);
        expect(resourceRecords.length).toEqual(11);
        expect(resourceRecords[10].conceptIds.length).toEqual(0);
        expect(resourceRecords[10].resourceTypes.length).toEqual(0);
        expect(resourceRecords[10].maxPossibleKnowledgeEstimate).toEqual(1);
        //reset resourceRecords afterwards
        resourceRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");
    });

    it("updateResourceType to add a resource type", function() {
        updateResourceType(resourceRecords, "Q1", "INFORMATION");
        expect(resourceRecords[0].resourceTypes.length).toEqual(3);
    });

    it("updateResourceType to remove a resource type", function() {
        updateResourceType(resourceRecords, "Q2", "PRACTICE");
        expect(resourceRecords[1].resourceTypes.length).toEqual(1);
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
        //Pass/fail relies on the order that other tests run in because the string changes, make sure it works by resetting the state of resource records here
        resourceRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");
        console.log(buildTableHeaderHtmlString(resourceRecords, ["Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"]));
        expect(buildTableHeaderHtmlString(resourceRecords, ["Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"]))
              .toEqual("<thead><tr><th class='col-xs-1' scope='col'></th><th class='col-xs-1' scope='col'> Q1<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q1\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q1\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q1\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> Q2<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q2\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q2\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q2\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> Q3<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q3\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q3\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q3\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> Q4<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q4\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q4\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q4\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> Q5<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q5\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q5\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"Q5\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> HW1<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW1\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW1\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW1\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> HW2<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW2\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW2\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW2\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> HW3<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW3\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW3\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW3\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> HW4<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW4\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW4\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW4\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th><th class='col-xs-1' scope='col'> HW5<br><div class='col-lg-1'><div class='button-group'><button type=\"button\" class=\"btn btn-default btn-sm dropdown-toggle\" data-toggle=\"dropdown\"><span class=\"glyphicon glyphicon-cog\"></span> <span class=\"caret\"></span></button><ul class='dropdown-menu'><a class='small' data-value='PRACTICE' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW5\", \"PRACTICE\")' type='checkbox' checked='true' />&nbsp;PRACTICE</a></li><a class='small' data-value='ASSESSMENT' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW5\", \"ASSESSMENT\")' type='checkbox' checked='true' />&nbsp;ASSESSMENT</a></li><a class='small' data-value='INFORMATION' tabindex='-1'><li><input onclick='updateResourceType(resourceRecords, \"HW5\", \"INFORMATION\")' type='checkbox'/>&nbsp;INFORMATION</a></li></ul></div></div></th></tr></thead>");
    });
    
    it("buildCheckboxHtmlString unchecked box", function(){
        expect(buildTableCheckboxHtmlString("If Statements", "Q1", false)).toEqual("<td class='text-center'><input id='If Statements_Q1' type='checkbox' onclick='updateResourceRecords(resourceRecords, \"Q1\", \"If Statements\")'></td>");
    });
    
    it("buildCheckboxHtmlString checked box", function(){
        expect(buildTableCheckboxHtmlString("If Statements", "Q1", true)).toEqual("<td class='text-center'><input id='If Statements_Q1' type='checkbox' checked='true' onclick='updateResourceRecords(resourceRecords, \"Q1\", \"If Statements\")'></td>");
    });
    
    it("buildTableRowHtmlString", function(){
        console.log(buildTableRowHtmlString("If Statements", ["Q1", "Q2", "Q3"], [true, false, false]));
        expect(buildTableRowHtmlString("If Statements", ["Q1", "Q2", "Q3"], [true, false, false]))
              .toEqual("<tr><th scope='row'>If Statements</th><td class='text-center'><input id='If Statements_Q1' type='checkbox' checked='true' onclick='updateResourceRecords(resourceRecords, \"Q1\", \"If Statements\")'></td><td class='text-center'><input id='If Statements_Q2' type='checkbox' onclick='updateResourceRecords(resourceRecords, \"Q2\", \"If Statements\")'></td><td class='text-center'><input id='If Statements_Q3' type='checkbox' onclick='updateResourceRecords(resourceRecords, \"Q3\", \"If Statements\")'></td></tr>");
    });
    
   it("buildTableBodyHtmlString", function(){
       var conceptList = ["While Loops","For Loops","Boolean Expressions","Intro CS", "Loops", "If Statements"],
           tableString = buildTableBodyHtmlString(conceptList, resourceRecords),
           rowCount = (tableString.match(/<th scope='row'>/g) || []).length;
       console.log(tableString);

       expect(rowCount).toEqual(6);
   });
});

