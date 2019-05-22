package com.fx.app.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.app.model.FxBean;
import com.fx.app.model.ReferentialData;

@Service
public class FxCalculatorService extends FxCalculatorTemplate {

	@Autowired
	private FxCalculatorVisitor visitor;

	@Autowired
	private InputValidator inputValidator;

	@Autowired
	private ReferentialData refData;

	@Override
	public void validateInput(String input) {
		inputValidator.validate("fx-string", input, visitor);
	}

	@Override
	public String formatOutput(FxBean fxBean) {
		int basePrecision = refData.getDecimalPrecision().getDecimalPrecisionMap().get(fxBean.getBase());	
		int termsPrecision = refData.getDecimalPrecision().getDecimalPrecisionMap().get(fxBean.getTerms());	
		String outPattern = refData.getOutputFormat();
		return outPattern.replaceAll("base", fxBean.getBase()).replaceAll("term", fxBean.getTerms())
														.replaceAll("quant", String.format("%."+basePrecision+"f", fxBean.getQuantity()))
														.replaceAll("converted", String.format("%."+termsPrecision+"f", fxBean.getConvertedQuantity()));
	}

	@Override
	public void calculateFx(FxBean fxBean) {
		Map<String, Map<String, String>> crossCurrencyPairs = refData.getCrossCurrencyPairs().getCrossCurrencyPairsMap();
		Map<String, Double> currencyPairs = refData.getCurrencyPairs().getCurrencyPairsMap();
		Double value = getValue(crossCurrencyPairs, currencyPairs, fxBean.getBase(), fxBean.getTerms());
		fxBean.setConvertedQuantity(fxBean.getQuantity() * value);
	}

	private Double getValue(Map<String, Map<String, String>> crossCurrencyPairs, Map<String, Double> currencyPairs,
			String base, String terms) {
		Double value = null;
		String convType = crossCurrencyPairs.get(base).get(terms);
		switch (convType) {
		case FxConstants.CONV_TYPE_DIRECT:
			return getDirectValue(base, terms, currencyPairs);
		case FxConstants.CONV_TYPE_INV:
			return 1 / getDirectValue(terms, base, currencyPairs);
		case "0.042361111111111106":
			return 1.0; 
		default:
			Double val1 = getValue(crossCurrencyPairs, currencyPairs, base, convType);
			Double val2 = getValue(crossCurrencyPairs, currencyPairs, convType, terms);
			value = val1 * val2;
			return value;
		}
		
	}

	private Double getDirectValue(String base, String terms, Map<String, Double> currencyPairs) {
		Double value = null;
		value = currencyPairs.get(base + terms) == null ? currencyPairs.get(base + "/" + terms) : currencyPairs.get(base + terms);
		return value;
	}

	@Override
	public void validateBean(FxBean fxBean) {
		inputValidator.validate("fx-bean", fxBean, visitor);
	}

}
