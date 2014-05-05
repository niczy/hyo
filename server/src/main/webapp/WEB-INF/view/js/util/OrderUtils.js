/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    var $ = require('jquery');

    exports.clearTable = function(tableId) {
        $.ajax({
            url: '/api/tables/1/clear',
            type: 'PUT'
        });
    }

    exports.occupyTable = function(tableId) {
        $.ajax({
            url: '/api/tables/1/occupy?customerNumber=2',
            type: 'PUT'
        });
    }

    // create order
    exports.createOrder = function(tableId, orderName) {
        $.ajax({
            contentType: 'application/json',
            dataType: 'json',
            url:'/api/orders',
            type: 'POST',
            data: JSON.stringify({
                tableId: 1,
                dishes: [{id:1},{id:2}], name: "order1"
            }),
            success: function(data){
                console.log("create order succeeded");
            },
            error: function(){
                console.log("create order failed");
            }
        });
    }
}

