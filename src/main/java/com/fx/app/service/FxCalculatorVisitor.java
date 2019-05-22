package com.fx.app.service;

public interface FxCalculatorVisitor<T> {

	public boolean visit(String type, T input);
}
