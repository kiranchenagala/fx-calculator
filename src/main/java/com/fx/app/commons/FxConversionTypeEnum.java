package com.fx.app.commons;

public enum FxConversionTypeEnum {
	DIRECT("D"),
	INVERSE("Inv"), 
	NUMBER("\\d+");
	
	private String value; 
	
	public String getValue() {
		return value;
	}

	FxConversionTypeEnum(String value){
		this.value=value;
	}
	

	
}
