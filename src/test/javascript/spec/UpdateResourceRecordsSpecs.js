'use strict';

describe("UpdateResourceRecordsSpecs", function() {
    var resourceRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");

    it("readJson for resourceRecords", function () {
        expect(resourceRecords.length).toEqual(10);
        expect(resourceRecords[0].learningResourceId).toEqual("Q1");
    });

    it("updateConceptId to add a connection", function () {
        updateConceptId(resourceRecords, "Q1", "While Loops");
        expect(resourceRecords[0].conceptIds).toEqual(["If Statements", "While Loops"]);
    });

    it("updateConceptId to remove a connection", function () {
        updateConceptId(resourceRecords, "Q2", "While Loops");
        expect(resourceRecords[1].conceptIds.length).toEqual(0);
    });

    it("addResourceToRecords adding a new resource", function () {
        expect(resourceRecords.length).toEqual(10);
        addResourceToRecords(resourceRecords, "newResource", 1);
        expect(resourceRecords.length).toEqual(11);
        expect(resourceRecords[10].conceptIds.length).toEqual(0);
        expect(resourceRecords[10].resourceTypes.length).toEqual(0);
        expect(resourceRecords[10].maxPossibleKnowledgeEstimate).toEqual(1);
        //reset resourceRecords afterwards
        resourceRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");
    });

    it("updateResourceType to add a resource type", function () {
        updateResourceType(resourceRecords, "Q1", "INFORMATION");
        expect(resourceRecords[0].resourceTypes.length).toEqual(3);
    });

    it("updateResourceType to remove a resource type", function () {
        updateResourceType(resourceRecords, "Q2", "PRACTICE");
        expect(resourceRecords[1].resourceTypes.length).toEqual(1);
    });

    it("createResourceIdsList", function () {
        var resourceIds = createResourceIdsList(resourceRecords);
        expect(resourceIds).toEqual(["Q1", "Q2", "Q3", "Q4", "Q5", "HW1", "HW2", "HW3", "HW4", "HW5"]);
    });

    it("checkForChangesInRecords", function () {
        var originalRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");
        var newRecords = JSON.parse(JSON.stringify(originalRecords));
        expect(checkForChangesInRecords(originalRecords, newRecords)).toEqual(true);
        expect(checkForChangesInRecords(resourceRecords, addResourceToRecords(newRecords, "newResource", 1))).toEqual(false);
    });



});