package com.fx.app.service;

public interface Visitable {
	
	public void accept(FxCalculatorVisitor visitor);

}
