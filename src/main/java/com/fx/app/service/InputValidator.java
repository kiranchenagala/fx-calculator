package com.fx.app.service;

import org.springframework.stereotype.Service;

@Service 
public class InputValidator<T> implements Visitable{
	
	private T input;
	private String type;
	
	public void validate(String type, T input, FxCalculatorVisitor visitor){
		this.input=input;
		this.type=type;
		this.accept(visitor);
	}

	@Override
	public void accept(FxCalculatorVisitor visitor) {
		visitor.visit(type, input);
	}

}
