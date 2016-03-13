$(document).ready(function () {
    loadTable('api/shows', displayItem);
    overrideFormSubmits(function (highlightItemId) {
        loadTable('api/shows', displayItem, highlightItemId);
    });


    $('#show-input').find('.typeahead').typeahead({
            hint: true,
            highlight: true,
            minLength: 2
        },
        {
            name: 'shows',
            display: 'title',
            source: new Bloodhound({
                datumTokenizer: Bloodhound.tokenizers.obj.whitespace('value'),
                queryTokenizer: Bloodhound.tokenizers.whitespace,
                remote: {
                    url: '/api/shows/find/%QUERY',
                    wildcard: '%QUERY'
                }
            }),
            templates: {
                empty: "<div>No match</div>",
                suggestion: function (data) {
                    return '<div>' + data.title + ' (' + data.year + ')</div>';
                }
            }
        }
    );

});

displayItem = function (item) {
    return $('<td/>').append($('<a/>', {
        href: 'shows/' + item.id,
        'class': 'link-to-show'
    }).append(item.title));
};