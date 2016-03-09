var url = "api/feeds";

$(document).ready(function() {
    loadTable(url, displayItem);
    overrideFormSubmits(function(highlightItemId) {
        loadTable("api/feeds", displayItem, highlightItemId);
    });
});

displayItem = function(item) {
    return item.url;
};