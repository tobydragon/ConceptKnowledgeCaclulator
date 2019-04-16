'use strict';

describe("ConnectMaterialsListSpecs", function() {

    var materials = readJson("../resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json");

    it("updateNavString", function() {
        expect(loadNavString(10)).toEqual("0/10");
    });

    it("updateMaterialsString", function () {
        expect(createListOfLearningRecordsString(materials)).toContain("[ASSESSMENT] 3 [Cs1]");
    })

});