/**
 * @(#)BadRequestError.java, Aug 3, 2013. 
 *
 */
package com.cloudstone.emenu.exception;

/**
 * @author xuhongfeng
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class BadRequestError extends HttpStatusError {
    private static final long serialVersionUID = 8040163874106379282L;

    public BadRequestError() {
        super(400, "", null);
    }

    public BadRequestError(String message) {
        super(400, message, null);
    }

}
