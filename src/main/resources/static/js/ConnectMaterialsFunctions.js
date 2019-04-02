'use strict';

function addLearningMaterialToRecords(resourceRecords, newResourceId) {
    var newRecord = {
        learningResourceId: newResourceId,
        resourceTypes: [],
        conceptIds: [],
        dataImportance: 1,
        maxPossibleKnowledgeEstimate: 10
    };

    resourceRecords.push(newRecord);

}

function updateResourceRecords(resourceRecords, resourceId, conceptId) {
    var toChange;
    var i;

    for (i = 0; i < resourceRecords.length; i += 1) {
        if (resourceRecords[i].learningResourceId === resourceId) {
            toChange = resourceRecords[i].conceptIds;
            if (toChange.includes(conceptId)) {
                toChange.pop(conceptId);
            } else {
                toChange.push(conceptId);
            }
        }
    }
}