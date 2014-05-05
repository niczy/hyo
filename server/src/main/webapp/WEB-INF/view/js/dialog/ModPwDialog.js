/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var FormDialog = require('./FormDialog');
    var BaseContent = require('./BaseContent');

    var Content = BaseContent.extend({
        tmpl: require('./ModPwDialog.handlebars')
    });

    var ModPwDialog = FormDialog.extend({
        header: 'Change password',

        ContentType: Content,

        FormType: require('../form/ModPwForm'),

        formEl: '.form-mod-pw'
    });
    
    return ModPwDialog;
    
});

