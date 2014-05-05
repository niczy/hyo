package com.cloudstone.emenu.ctrl;

import com.cloudstone.emenu.exception.BadRequestError;
import com.cloudstone.emenu.exception.HttpStatusError;
import com.cloudstone.emenu.util.JsonUtils;
import com.cloudstone.emenu.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by charliez on 4/28/14.
 */
@Controller
public class ExceptionController {

    private static final Logger LOG = LoggerFactory.getLogger(ExceptionController.class);

    @ResponseStatus(value=HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestError.class)
    public void badRequest(HttpServletRequest req, HttpServletResponse resp,
                           ModelMap model, Throwable error) throws IOException {
        HttpStatusError e = (HttpStatusError) error;
        resp.setStatus(e.getStatusCode());
        resp.setContentType("application/json; charset=UTF-8");
        model.put("code", e.getStatusCode());
        if (e.getStatusCode() == 500 && StringUtils.isBlank(e.getMessage())) {
            model.put("message", "Server Error");
        } else {
            model.put("message", e.getMessage());
        }
        resp.getWriter().write(JsonUtils.toJson(model));
        resp.getWriter().flush();
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleError(HttpServletRequest req, Exception e) {
        LOG.error("Request: " + req.getRequestURL() + " raised " + e);

        ModelAndView mav = new ModelAndView();
        mav.addObject("exception", e);
        mav.addObject("url", req.getRequestURL());
        mav.setViewName("error");
        return mav;
    }
}
