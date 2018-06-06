var actions;

function listActions() {
    var prefix = "/view/";
    for (var courseName in actions) {
        var courseActions = actions[courseName];
        if (courseActions.length > 0) {
            var codeList = "<ul><strong>" + courseName + ":" + "</strong>";
            for (var i = 0; i < courseActions.length; i++) {
                var dest = prefix + courseActions[i] + "/" + courseName;
                var listItem = "<li>";
                listItem += '<a href="' + dest + '">' + dest + "</a>";
                listItem += "</li>";
                codeList += listItem;
            }
            codeList += "</ul>";
            document.getElementById("courseList").innerHTML += codeList;
        } else {
            document.getElementById("courseList").innerHTML = "<p>There are no maps for you to view</p>"
        }
    }
}

listActions();
