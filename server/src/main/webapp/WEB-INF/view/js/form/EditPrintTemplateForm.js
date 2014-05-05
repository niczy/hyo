/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var BaseForm = require('./BaseForm');

    var Required = require('./validator/Required');
    var Text = require('./item/Text');
    var Radio = require('./item/Radio');
    var AjaxSelect = require('./item/AjaxSelect');
    var AjaxCheckboxGroup = require('./item/AjaxCheckboxGroup');

    var Integer = require('./validator/Integer');
    var MoreThan = require('./validator/MoreThan');

    var PrintTemplateForm = BaseForm.extend({

        itemConfig: [{
            name: 'name',
            type: Text,
            el: '.input-name',
            validators: [{
                type: Required,
                errorMessage: 'Template name is required'
            }]
        }, {
            name: 'headerId',
            type: AjaxSelect,
            el: '.input-headerId',
            wrapId: true,
            CollectionType: require('../collection/PrintComponentCollection')
        }, {
            name: 'footerId',
            type: AjaxSelect,
            el: '.input-footerId',
            wrapId: true,
            CollectionType: require('../collection/PrintComponentCollection')
        }, {
            name: 'cutType',
            type: Radio,
            el: '.input-cutType'
        }, {
            name: 'fontSize',
            type: Text,
            el: '.input-fontSize',
            validators: [{
                type: Required,
                errorMessage: 'This is required'
            }, {
                type: Integer,
                errorMessage: 'Please input an integer'
            }, {
                type: MoreThan,
                other: 1,
                including: true,
                errorMessage: 'Most be greater than 1'
            }]
        }, {
            name: 'chapterIds',
            type: AjaxCheckboxGroup,
            CollectionType: require('../collection/ChapterCollection')
        }]
    });
    
    return PrintTemplateForm;
    
});

