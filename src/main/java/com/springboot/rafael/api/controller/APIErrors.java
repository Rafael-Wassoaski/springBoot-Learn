package com.springboot.rafael.api.controller;

import java.util.Arrays;
import java.util.List;

import lombok.Data;
import lombok.Getter;

public class APIErrors {
	@Getter
	public List<String> errors;
	
	public APIErrors(String errorMessage) {
		this.errors = Arrays.asList(errorMessage);
	}
}
