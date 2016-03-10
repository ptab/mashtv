$(document).ready(function() {
    loadTable('api/feeds', displayItem);
    overrideFormSubmits(function(highlightItemId) {
        loadTable('api/feeds', displayItem, highlightItemId);
    });
});

displayItem = function(item) {
    return $('<td/>').append(item.url);
};