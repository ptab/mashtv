$(document).ready(function() {
    loadShows();
    loadFeeds();
       
    overrideFormSubmits("add-feed", loadFeeds) ;
    overrideFormSubmits("add-show", loadShows) ;  
});

loadFeeds = function() {
    $.getJSON("api/feeds", function(data) {
        var items = [];
        $.each(data, function() {
            items.push("<li class=\"list-group-item\">" + this.url + "</li>");
        });
        
        items.sort();
        
        $("#feeds-placeholder").replaceWith($("<ul/>", {
            "id" : "feeds-placeholder",
            "class" : "list-group",
            html : items.join("")
        })) ;
    });
};

loadShows = function() {
    $.getJSON("api/shows", function(data) {
        var items = [];
        $.each(data, function() {
            items.push("<li class=\"list-group-item\">" + this.title + "</li>");
        });
        
        items.sort();
        
        $("#shows-placeholder").replaceWith($("<ul/>", {
            "id" : "shows-placeholder",
            "class" : "list-group",
            html : items.join("")
        })) ;
    });
};

overrideFormSubmits = function(formId, success, callback) {
    // setup the on submit trigger
    var frm = $("form#" + formId) ;
    frm.submit(function(ev) {        
        $.ajax({
            type : frm.attr('method'),
            url : frm.attr('action'),
            data : frm.serialize(),
            success : function(data) {
                success(data) ;
            },
            failure : function(data) {
               failure(data) ;
            }
        });
        ev.preventDefault();
    });
}