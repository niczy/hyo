/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var FormDialog = require('./FormDialog');
    var BaseContent = require('./BaseContent');

    var Content = BaseContent.extend({
        tmpl: require('./PrintDialog.handlebars')
    });

    var PrintDialog = FormDialog.extend({
        header: 'Print',

        FormType: require('../form/PrintForm'),

        ContentType: Content,

        formEl: '.form-print'
    });
    
    return PrintDialog;
    
});
