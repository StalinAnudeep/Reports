/**
 * 
 *//*
package com.spdcl.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ModelAndView handleCustomException(CustomException ex) {
		ModelAndView model = new ModelAndView("error/generic_error");
		model.addObject("errorCode", ex.getErrorCode());
		model.addObject("exception", ex.getExceptionMsg());
		return model;

	}
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(Exception.class)
	public ModelAndView handleAllException(Exception ex) {
		ModelAndView model = new ModelAndView("error/404");
		model.addObject("errorCode", "404");
		model.addObject("exception", ex);
		return model;		
	}

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // 500
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleInternalServerError(Exception ex) {
		ModelAndView model = new ModelAndView("error/exception_error");
		model.addObject("errorCode", "500");
		model.addObject("exception", ex);
		return model;		
    }
}
*/