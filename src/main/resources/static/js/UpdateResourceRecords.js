'use strict';
function updateConceptId(resourceRecords, resourceId, conceptId) {
    var toChange,
        i = 0;

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

function addResourceToRecords(resourceRecords, newResourceId, maxKnowledgeEstimate) {
    var newRecord = {
        learningResourceId: newResourceId,
        resourceTypes: [],
        conceptIds: [],
        dataImportance: 0,
        maxPossibleKnowledgeEstimate: maxKnowledgeEstimate
    };

    resourceRecords.push(newRecord);

}

function updateResourceType(resourceRecords, resourceId, resourceType) {
    console.log(resourceRecords);
    var toChange,
        i = 0;

    for (i = 0; i < resourceRecords.length; i += 1) {
        if (resourceRecords[i].learningResourceId === resourceId) {
            toChange = resourceRecords[i].resourceTypes;
            if (toChange.includes(resourceType)) {
                toChange.pop(resourceType);
            } else {
                toChange.push(resourceType);
            }
        }
    }
}

function createResourceIdsList(resourceRecords) {
    var resourceIds = [],
        i = 0;

    for (i = 0; i < resourceRecords.length; i += 1) {
        resourceIds.push(resourceRecords[i].learningResourceId);
    }
    return resourceIds;
}