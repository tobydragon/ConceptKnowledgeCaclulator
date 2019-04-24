'use strict';

describe("ConnectMaterialsListSpecs", function() {

    var materials = readJson("../resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json");
    var resourceRecords = readJson("../resources/datastore/Cs1Example/Cs1ExampleResources.json");

    it("updateNavString", function() {
        expect(loadNavString(10)).toEqual("0/10");
    });

    it("createListOfLearningRecordsString", function () {
        expect(createListOfLearningRecordsString(materials, resourceRecords)).toContain("HW2");
    });

    it("isMaterialLinked", function () {
        expect(isMaterialLinked("Q1", resourceRecords)).toEqual(true);
        expect(isMaterialLinked("Q10", resourceRecords)).toEqual(false);
    });

});