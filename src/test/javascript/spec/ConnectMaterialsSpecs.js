'use strict';

describe('ConnectMaterialsSpecs', function() {

    var learningMaterials = readJson('../resources/datastore/Cs1Example/Cs1ExampleLearningMaterial.json');
    var resourceRecords = readJson('../resources/datastore/Cs1Example/Cs1ExampleMaterials.json');

    it('readJson for LearningMaterials', function(){
        expect(learningMaterials.length).toEqual(6);
        expect(learningMaterials[0].id).toEqual('[ASSESSMENT] 1 [Cs1]');
    });

    it('readJson for resourceRecords', function(){
        expect(resourceRecords.length).toEqual(1);
        expect(resourceRecords[0].learningResourceId).toEqual('[INFORMATION] 4 [Cs1]');
    });

    it('adding Resource Records with LearningMaterials', function(){
        expect(resourceRecords.length).toEqual(1);
        addLearningMaterialToRecords(resourceRecords, '[ASSESSMENT] 1 [Cs1]')
        expect(resourceRecords.length).toEqual(2);
        expect(resourceRecords[1].learningResourceId).toEqual('[ASSESSMENT] 1 [Cs1]');

        resourceRecords = readJson('../resources/datastore/Cs1Example/Cs1ExampleMaterials.json');
    });

    it('add conceptIds to existing LearningResourceRecord', function(){
        expect(resourceRecords.length).toEqual(1);
        expect(resourceRecords[0].conceptIds.length).toEqual(0);
        updateResourceRecords(resourceRecords, '[INFORMATION] 4 [Cs1]', 'Boolean Expressions');
        expect(resourceRecords[0].conceptIds.length).toEqual(1);
        resourceRecords = readJson('../resources/datastore/Cs1Example/Cs1ExampleMaterials.json');
    });

});