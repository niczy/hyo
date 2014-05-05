/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var BaseForm = require('./BaseForm');

    var Required = require('./validator/Required');
    var Integer = require('./validator/Integer');
    var Number = require('./validator/Number');
    var MoreThan = require('./validator/MoreThan');
    var TableTipValidator = require('./validator/TableTipValidator');

    var Text = require('./item/Text');
    var Radio = require('./item/Radio');
    var TableTip = require('./item/TableTip');

    var EditTableForm = BaseForm.extend({
        url: '/api/tables',
        
        itemConfig: [{
            name: 'name',
            type: Text,
            validators: [{
                type: Required,
                errorMessage: 'Table name must no be empty'
            }]
        }, {
            name: 'type',
            type: Radio,
            el: '.input-type'
        }, {
            name: 'capacity',
            type: Text,
            validators: [{
                type: Required,
                errorMessage: 'Maximum guests must not be empty'
            }, {
                type: MoreThan,
                other: 0,
                errorMessage: 'Please input a positive number'
            }, {
                type: Integer,
                errorMessage: 'Please input an integer'
            }]
        }, {
            name: 'minCharge',
            type: Text,
            validators: [{
                type: Required,
                errorMessage: 'Please specify minimum charge'
            }, {
                type: MoreThan,
                other: 0,
                including: true,
                errorMessage: 'Please input a positive number'
            }, {
                type: Number,
                errorMessage: 'Please input a number'
            }]
        }, {
            name: 'tip',
            type: TableTip,
            validators: [{
                type: TableTipValidator
            }]
        }],

        
        /* -------------------- Event Listener ----------------------- */
        
        onFailed: function (xhr) {
            var msg = this.model.isNew() ? 'New' : 'Edit';
            msg = msg + 'Failed to edit table';
            window.alert(msg);
        }
    });
    
    return EditTableForm;
    
});

