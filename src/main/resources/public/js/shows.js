var url = "api/shows" ;

$(document).ready(function() {
    loadTable(url, displayItem);
    overrideFormSubmits(function() { loadTable(url, displayItem); });
});

displayItem = function(item) {
    return item.title;
}