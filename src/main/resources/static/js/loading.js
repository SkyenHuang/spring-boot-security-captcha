/// <reference path="jquery-3.1.1.js" />
/// <reference path="../bootstrap-3.3.7-dist/js/bootstrap.js" />
/**
 * 
 */
function ProcessBar(position) {
    if (position == 'bottom') {
        this.content.css({ 'bottom': 0 });
    } else {
        this.content.css({ 'top': 0 });
    }
    this.process = 0;
    this.content.css({
        'position': 'absolute',
        'z-index': '999',
        'height': 5,
        'background-color': '#428bca',
        'display': 'none',
        'left': 0,
        'box-shadow': '0px 0px 5px #333'
    });

}

ProcessBar.prototype.content = $('<div class="loading"></div>');

ProcessBar.prototype.update = function (process) {
    this.process = process
    this.content.animate({ 'width': this.process + '%' }, 'fast');
}

ProcessBar.prototype.load = function (process) {
    this.init();
    $(document.body).append(this.content);
    this.content.show();
    this.content.fadeIn('fast');
    if ('number' == typeof (process)) {
        this.update(process);
    }
}

ProcessBar.prototype.init = function () {
    this.process = 0;
    this.content.css('width', '0%');
}

ProcessBar.prototype.finish = function () {
    this.content.animate({ 'width': '100%' }, 'fast');
    this.content.css('background-color', '#5cb85c');
    var content = this.content;
    this.content.fadeOut('fast', function () {
        this.remove();
    });
}

ProcessBar.prototype.cancel = function () {
    this.content.css('background-color', '#d9534f');
    this.content.fadeOut('fast', function () {
        this.remove();
    });
}

var content = $(document.body).find(".content").html();
window.history.replaceState({
    'content': content,
    'title': $('title').html()
}, '', window.location.href);

window.onpopstate = function (event) {
    if (event.state) {
        event.preventDefault();
        $(document.body).find(".content").children().remove();
        $(document.body).find(".content").append(event.state.content);
        $('title').html(event.state.title);
    }
}
var processBar = new ProcessBar();
function loadPage(uri, title, callback) {
    processBar.load(50);
    $.get(uri, function (data) {
        window.history.pushState(
            {
                'content': data,
                'title': title
            },
            '', "http://" + window.location.host + uri);
        $(document.body).find(".content").children().remove();
        processBar.update(80);
        $(document.body).find(".content").append($(data));

        processBar.finish();
        if (title != undefined) {
            $('title').html(title);
        }
        if ('function' == typeof (callback)) {
            callback();
        }
    }, "html");
}

var tip = $('<div class="alert" role="alert"><button type="button" class="close" data-dismiss="alert"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button><p></p></div>');
tip.css({
    'position': 'fixed',
    'top': 0,
    'left': 0,
    'width': '100%'
})

$.ajaxSetup({
    error: function (xhr, status, error) {
        processBar.cancel();
        var cloneTip = tip.clone();
        cloneTip.find('p').html(error + status);
        cloneTip.addClass('alert-danger');
        $(document.body).append(cloneTip);
        cloneTip.fadeIn('fast');
        setTimeout(function () {
            cloneTip.fadeOut('fast', function () {
                cloneTip.remove();
            });
        }, 3000);
    }
})



function PageLoader(uri, title, success, failture, state) {
    this.uri = uri;
    this.success = success;
    this.failture = failture;
    this.state = state;
    this.title = title;
}
PageLoader.prototype.load = function () {
    window.history.replaceState(this.state, '', window.location.href);
    var loader = this;
    $.ajax({
        url: this.uri,
        type: 'get',
        dataType: 'html',
        success: function (data) {
            window.history.pushState({
                content: data,
                title: loader.title
            }, '', window.location.protocol + '//' + window.location.host + uri);
            loader.success(data);
        },
        error: function (e) {
            loader.failture(e);
        }
    })
}