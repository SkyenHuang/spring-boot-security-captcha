/// <reference path="jquery-3.1.1.js" />
(function (global) {
    var Skyen;
    Skyen = function () {

    };
    Skyen.fn = Skyen.prototype;
    Skyen.fn.get = function (url, success, error) {
        $.ajax({
            url: url,
            contentType: 'text/html',
            method: 'get',
            success: success(result),
            error: error(e)
        })
    }
    global.Skyen = Skyen;
})(typeof window !== 'undefinded' ? window : this)

