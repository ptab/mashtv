loadTable = function(url, displayItem, highlight) {
    $.getJSON(url, function(data) {
        var items = [];
        $.each(data, function() {
            var id = this.id;

            var removeButton = $('<span/>', {
                'class' : 'fa fa-trash-o pull-right'
            });

            removeButton.click(function() {
                $.ajax({
                    url : url + '/' + id,
                    type : 'DELETE',
                    success: function () {
                        loadTable(url, displayItem);
                    }
                });
            });

            var tr = $('<tr/>');
            if (this.id === highlight) {
                flashElement(tr, 'success');
            }

            items.push(tr.append(displayItem(this)).append($('<td/>').append(removeButton)));
        });

        items = items.sort(function(a, b) {
            // this can be done better..
            return a.children('td:first')[0].outerText.localeCompare(b.children('td:first')[0].outerText);
        });

        var table = $('table');
        table.children().remove();
        table.append(items);
    });
};

overrideFormSubmits = function(success) {
    // setup the on submit trigger
    var frm = $('form');
    frm.submit(function(ev) {
        var request = $.ajax({
            type : frm.attr('method'),
            url : frm.attr('action'),
            data : frm.serialize()
        });

        request.done(function(data) {
            frm[0].reset();
            success(data.id);
        });

        request.fail(function(jqXHR) {
            markForm($("div.form-group"), "has-error", "glyphicon-remove");
            showError(jqXHR.responseJSON.message);
        });

        ev.preventDefault();
    });
};

flashElement = function(element, classname) {
    element.addClass(classname).delay(3000).queue(function() {
        $(this).removeClass(classname).dequeue();
    });
};

markForm = function(element, className, icon) {
    element.removeClass();
    element.addClass('form-group ' + className + ' has-feedback');

    element.children('span.glyphicon').remove();
    element.append($('<span/>', {
        'class' : 'glyphicon ' + icon + ' form-control-feedback',
        'aria-hidden' : 'true'
    }));
};

showError = function(message) {
    var button = $('<button/>', {
        'type' : 'button',
        'class' : 'close',
        'data-dismiss' : 'alert'
    }).append($('<span/>', {
        'text' : '×',
        'aria-hidden' : 'true'
    })).append($('<span/>', {
        'class' : 'sr-only',
        'text' : 'Close'
    }));

    var icon = $('<span/>', {
        'class' : 'fa fa-exclamation-circle',
        'aria-hidden' : 'true'
    });

    $('<div/>', {
        'class' : 'alert alert-danger alert-dismissible'
    }).append(icon).append('<span> ' + message + '</span>').append(button).prependTo($('form'));
};