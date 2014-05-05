/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var $ = window.jQuery.noConflict();

    var ajax = $.ajax;

    $.ajax = function (url, options) {
        if (typeof url === "object") {
            options = url;
            url = undefined;
        }
        options = options || {};
        var error = options.error;
        options.error = function (xhr) {
            var status = xhr.status;
            if (options.statusCode && options.statusCode[String(status)]) {
                options.statusCode[String(status)].call(this);
                return;
            }
            if (options.url.indexOf('/api/polling') !== -1) {
                return;
            }
            if (status === 500 || status === 412 || status === 409) {
                if (status === 500 && !xhr.responseText) {
                    window.alert("Server error");
                } else {
                    var resp = JSON.parse(xhr.responseText);
                    window.alert(resp.message);
                }
            } else if (status === 504) {
                window.alert('Server busy, please try again');
            } else if (status === 401) {
                window.alert('Please login again');
                window.location = '/login';
            } else if (status === 512) {
                window.location = '/upgrading';
            } else if (status === 0) {
                window.alert("Browser error, try restarting your browser");
            } else {
                if (error) {
                    error.apply(this, arguments);
                } else {
                    window.alert('Network error');
                }
            }
        };
        return ajax.call(this, options);
    };

    return $;
});
