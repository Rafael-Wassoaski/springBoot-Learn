package com.springboot.rafael.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.rafael.exception.RuleException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(RuleException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private APIErrors handleRuleException(RuleException ex) {
		String errMessage = ex.getMessage();
		
		return new APIErrors(errMessage);
	}
}
