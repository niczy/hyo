/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";

    var BaseForm = require('./BaseForm');
    var Required = require('./validator/Required');
    var Text = require('./item/Text');
    var Image = require('./item/Image');
    var Textarea = require('./item/Textarea');
    var Radio = require('./item/Radio');
    var AjaxSelect = require('./item/AjaxSelect');

    var Number = require('./validator/Number');
    var MoreThan = require('./validator/MoreThan');

    var EditDishForm = BaseForm.extend({
        url: '/api/dishes',

        itemConfig: [{
            name: 'name',
            type: Text,
            el: '.input-name',
            validators: [{
                type: Required,
                errorMessage: 'Name is required'
            }]
        }, {
            name: 'price',
            type: Text,
            el: '.input-price',
            validators: [{
                type: Required,
                errorMessage: 'Price is required'
            }, {
                type: Number,
                errorMessage: 'Please input a number'
            }, {
                type: MoreThan,
                other: 0,
                including: true,
                errorMessage: 'Please input a positive number'
            }]
        }, {
            name: 'memberPrice',
            type: Text,
            el: '.input-memberPrice',
            validators: [{
                type: Number,
                errorMessage: 'Please input price'
            }, {
                type: MoreThan,
                other: 0,
                including: true,
                errorMessage: 'Invalid price'
            }]
        }, {
            name: 'unit',
            type: Radio,
            el: '.input-unit'
        }, {
            name: 'spicy',
            type: Radio,
            el: '.input-spicy'
        }, {
            name: 'specialPrice',
            type: Radio,
            el: '.input-specialPrice'
        }, {
            name: 'nonInt',
            type: Radio,
            el: '.input-nonInt'
        }, {
            name: 'desc',
            type: Textarea,
            el: '.input-desc'
        }, {
            name: 'uriData',
            type: Image,
            el: '.input-uriData'
        }]
        
    });
    
    return EditDishForm;
    
});

