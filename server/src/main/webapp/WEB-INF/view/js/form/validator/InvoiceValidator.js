/*jslint browser: true, devel: true, indent: 4, nomen:true, vars: true */
/*global define */

define(function (require, exports, module) {
    "use strict";
    
    var BaseValidator = require('./BaseValidator');

    var InvoiceValidator = BaseValidator.extend({
        errorMessage: 'Invalid invoice number',
        

        doValidate: function (item) {
            var invoice = item.getValue().invoice;
            if (!invoice) {
                return true;
            }
            var invoicePrice = item.getValue().invoicePrice;
            if (isNaN(invoicePrice)) {
                return false;
            }
            return invoicePrice >= 0;
        }
    });
    
    return InvoiceValidator;
    
});
