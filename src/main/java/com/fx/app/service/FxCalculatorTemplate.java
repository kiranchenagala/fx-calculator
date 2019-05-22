package com.fx.app.service;

import com.fx.app.model.FxBean;

public abstract class FxCalculatorTemplate {
	
	public abstract void validateInput(String input);

	public final String calculateFx(String input) {
		validateInput(input);
		FxBean fxBean = parse(input);
		validateBean(fxBean);
		calculateFx(fxBean);
		String output = formatOutput(fxBean);
		return output;
	}

	public abstract void validateBean(FxBean fxBean);
	
	public abstract void calculateFx(FxBean currencyExchangeBean);

	private FxBean parse(String input) {
		String[] inputs = input.replace("in", "").split("\\s+");
		FxBean bean = new FxBean(inputs[0], inputs[2], Double.valueOf(inputs[1]), null);
		return bean;
	}

	public abstract String formatOutput(FxBean currencyExchangeBean);

}
