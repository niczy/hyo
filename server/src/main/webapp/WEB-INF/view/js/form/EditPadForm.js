/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var BaseForm = require('./BaseForm');
    
    var Required = require('./validator/Required');
    var Text = require('./item/Text');

    var EditPadForm = BaseForm.extend({
        url: '/api/pads',

        itemConfig: [{
            name: 'name',
            type: Text,
            validators: [{
                type: Required,
                errorMessage: 'Device name must not be empty'
            }]
        }, {
            name: 'imei',
            type: Text,
            validators: [{
                type: Required,
                errorMessage: 'IMEI code must not be empty'
            }]
        }, {
            name: 'desc',
            type: Text
        }]
    });
    
    return EditPadForm;
    
});

