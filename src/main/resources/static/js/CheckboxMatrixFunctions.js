'use strict';

function updateResourceRecords(resourceRecords, resourceId, conceptId) {
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

function createResourceIdsList(resourceRecords) {
    var resourceIds = [],
        i = 0;
    
    for (i = 0; i < resourceRecords.length; i += 1) {
        resourceIds.push(resourceRecords[i].learningResourceId);
    }
    return resourceIds;
}

function createResourceCheckedListForConcept(conceptId, resourceRecords) {
    var checks = [],
        i = 0;
    for (i = 0; i < resourceRecords.length; i += 1) {
        if (resourceRecords[i].conceptIds.includes(conceptId)) {
            checks.push(true);
        } else {
            checks.push(false);
        }
    }
    return checks;
}

function buildTableHeaderHtmlString(headerNames) {
    var tableHTML = "<thead><tr><th class='col-xs-1' scope='col'></th>",
        i = 0;
    for (i = 0; i < headerNames.length; i += 1) {
        tableHTML += "<th class='col-xs-1' scope='col'> " + headerNames[i] + " </th>";
    }
    tableHTML += "</tr></thead>";
    return tableHTML;
}

//TODO: should be getting done different probably
//https://stackoverflow.com/questions/9643311/pass-string-parameter-in-an-onclick-function
function buildTableCheckboxHtmlString(conceptId, resourceId, isChecked) {
    var boxString = "<td class='text-center'><input id='" + conceptId + "_" + resourceId + "' type='checkbox' ";
    if (isChecked) {
        boxString += "checked='true' ";
    }
    //need double quotes around param literals because single quotes are around the whole onclick
    boxString += "onclick='updateResourceRecords(resourceRecords, \"" + resourceId + "\", \"" + conceptId + "\")\'></td>";
    return boxString;
}

function buildTableRowHtmlString(rowId, columnIds, columnIsCheckedList) {
    var rowString = "<tr><th scope='row'>" + rowId + "</th>",
        c = 0;
    for (c = 0; c < columnIds.length; c += 1) {
        rowString += buildTableCheckboxHtmlString(rowId, columnIds[c], columnIsCheckedList[c]);
    }
    rowString += "</tr>";
    return rowString;
}

function buildTableBodyHtmlString(conceptList, resourceRecords) {
    var bodyString = "<tbody>",
        resourceIds = createResourceIdsList(resourceRecords),
        resourcesCheckedList = [],
        r = 0;
    
    for (r = 0; r < conceptList.length; r += 1) {
        resourcesCheckedList = createResourceCheckedListForConcept(conceptList[r], resourceRecords);
        bodyString += buildTableRowHtmlString(conceptList[r], resourceIds, resourcesCheckedList);
    }
    bodyString += "</tbody>";
    return bodyString;
}

function buildTableHtmlString(conceptList, resourceRecords) {
    return buildTableHeaderHtmlString(createResourceIdsList(resourceRecords)) +
        buildTableBodyHtmlString(conceptList, resourceRecords);
}
