/**
 * @(#)NotFoundException.java, Aug 2, 2013. 
 *
 */
package com.cloudstone.emenu.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author xuhongfeng
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class NotFoundException extends HttpStatusError {

    private static final long serialVersionUID = -2868964411310955205L;

    public NotFoundException(String msg) {
        super(400, msg);
    }

}
