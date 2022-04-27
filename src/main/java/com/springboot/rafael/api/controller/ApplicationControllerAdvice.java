package com.springboot.rafael.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.springboot.rafael.exception.PurchaseNotFoundException;
import com.springboot.rafael.exception.RuleException;

@RestControllerAdvice
public class ApplicationControllerAdvice {
	
	@ExceptionHandler(RuleException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private APIErrors handleRuleException(RuleException ex) {
		String errMessage = ex.getMessage();
		
		return new APIErrors(errMessage);
	}
	
	@ExceptionHandler(PurchaseNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	private APIErrors handlePurchaseNotFoundException(PurchaseNotFoundException ex) {
		return new APIErrors(ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public APIErrors handleMethodNotValidException(MethodArgumentNotValidException exception){
		List<String> messages = exception.getBindingResult()
			.getAllErrors()
			.stream()
			.map(erro -> erro.getDefaultMessage()).collect(Collectors.toList());
		return new APIErrors(messages);
	}
}
