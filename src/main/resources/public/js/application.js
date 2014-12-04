loadTable = function(url, displayItem) {
    $.getJSON(url, function(data) {
        var items = [];
        $.each(data, function() {
            var id = this.id;
            var display = displayItem(this);

            var removeButton = $("<span/>", {
                "class" : "glyphicon glyphicon-remove pull-right"
            });

            removeButton.click(function() {
                $.ajax({
                    url : url + "/" + id + "/delete",
                    type : 'DELETE',
                    success : function(result) {
                        loadTable(url, displayItem);
                    }
                });
            });

            items.push($("<tr/>").append($("<td/>").append(display)).append($("<td/>").append(removeButton)));
        });

        items = items.sort(function(a, b) {
            // this can be done better..
            return a.children("td:first")[0].outerText.localeCompare(b.children("td:first")[0].outerText);
        });

        $("table").children().remove();
        $("table").append(items);
    });
};

overrideFormSubmits = function(success) {
    // setup the on submit trigger
    var frm = $("form");
    frm.submit(function(ev) {
        var request = $.ajax({
            type : frm.attr('method'),
            url : frm.attr('action'),
            data : frm.serialize()
        });

        request.done(function(msg) {
            frm[0].reset();
            markForm($("div.form-group"), "has-success", "glyphicon-ok");
            success();
        });

        request.fail(function(jqXHR, textStatus) {
            markForm($("div.form-group"), "has-error", "glyphicon-remove");
            showError(jqXHR.responseJSON.message);
        });

        ev.preventDefault();
    });
}

markForm = function(element, className, glyphicon) {
    element.removeClass();
    element.addClass("form-group " + className + " has-feedback");

    element.children("span.glyphicon").remove();
    element.append($("<span/>", {
        "class" : "glyphicon " + glyphicon + " form-control-feedback",
        "aria-hidden" : "true"
    }));
}

showError = function(message) {
    var button = $("<button/>", {
        "type" : "button",
        "class" : "close",
        "data-dismiss" : "alert"
    }).append($("<span/>", {
        "text" : "×",
        "aria-hidden" : "true"
    })).append($("<span/>", {
        "class" : "sr-only",
        "text" : "Close"
    }));

    var icon = $("<span/>", {
        "class" : "glyphicon glyphicon-exclamation-sign",
        "aria-hidden" : "true"
    });

    $("<div/>", {
        "class" : "alert alert-danger alert-dismissible",
    }).append(icon).append("<span> " + message + "</span>").append(button).prependTo($("form"));
}