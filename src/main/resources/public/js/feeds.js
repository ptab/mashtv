var url = "api/feeds" ;

$(document).ready(function() {
    loadTable(url, displayItem);
    overrideFormSubmits(function() { loadTable("api/feeds", displayItem); });
});

displayItem = function(item) {
    return item.url;
}