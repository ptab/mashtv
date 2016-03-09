var url = "api/shows";

$(document).ready(function() {
    loadTable(url, displayItem);
    overrideFormSubmits(function(highlightItemId) {
        loadTable(url, displayItem, highlightItemId);
    });
});

displayItem = function(item) {
    return item.title;
};