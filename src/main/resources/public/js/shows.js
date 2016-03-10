$(document).ready(function() {
    loadTable('api/shows', displayItem);
    overrideFormSubmits(function(highlightItemId) {
        loadTable('api/shows', displayItem, highlightItemId);
    });
});

displayItem = function(item) {
    return $('<td/>').append($('<a/>', {
        href : 'shows/' + item.id,
        'class' : 'link-to-show'
    }).append(item.title));
};