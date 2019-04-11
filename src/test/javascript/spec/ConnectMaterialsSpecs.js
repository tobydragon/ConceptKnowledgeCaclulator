'use strict';

describe("ConnectMaterialsSpecs", function() {

    var materials = readJson("../resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json");

    it("updateIndex", function () {
        expect(updateIndex(true, 0, 6)).toEqual(1);
        expect(updateIndex(false, 4, 6)).toEqual(3);
        expect(updateIndex(true, 6, 6)).toEqual(6);
    });

    it("updateNavString", function() {
        expect(updateNavString(6, 6)).toEqual("6/6");
        expect(updateNavString(3, 6)).toEqual("3/6");
    });

    it("updateConceptsString", function () {
        var conceptList = ["While Loops","For Loops"];
        expect(updateConceptsString(conceptList)).toEqual(
            "<tr><td>While Loops</td><td><input type='checkbox'></td></tr><tr><td>For Loops</td><td><input type='checkbox'></td></tr>"
        );
    });

    it("updateMaterialsString", function () {
        var id = 123;
        var content = "The quick brown fox";
        var tags = {"Quick":1};
        expect(updateMaterialsString(id, content, tags)).toEqual("<p class=\"learningMaterialID\">123</p><p class=\"learningMaterialContent\">The quick brown fox</p><p class=\"suggestedTags\">Suggested Tags:</p><ul class=\"suggestedTags\"><li>Quick</li></ul>"
        );
    })

});