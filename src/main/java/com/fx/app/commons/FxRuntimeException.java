package com.fx.app.commons;

public class FxRuntimeException extends RuntimeException{

	public FxRuntimeException(){
		super();
	}
	
	public FxRuntimeException(String errorMsg){
		super(errorMsg);
	}
}
