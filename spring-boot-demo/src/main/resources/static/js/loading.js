/// <reference path="jquery-3.1.1.js" />
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
        'width': this.process + '%',
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
    $(document.body).append(this.content);
    this.content.show();
    this.content.fadeIn('fast');
    if ('number' == typeof (process)) {
        this.update(process);
    }
}

ProcessBar.prototype.finish = function () {
    this.content.animate({ 'width': '100%' }, 'fast');
    var content = this.content;
    this.content.fadeOut('fast', function () {
        content.hide();
        content.remove();
    });
}