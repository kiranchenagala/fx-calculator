package com.fx.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import com.fx.app.model.ReferentialData;

@RunWith(MockitoJUnitRunner.class)
public class FxCalculatorServiceTest {

	@InjectMocks
	private FxCalculatorTemplate service = new FxCalculatorService();

	@Spy
	private InputValidator inputValidator;

	@Spy
	private FxCalculatorVisitor visitor;

	@Spy
	private ReferentialData refData ;

	@Test
	public void shouldCalculateFx() {
		Map<String, Double> currencyPairsMap = new HashMap<>();
		currencyPairsMap.put("AUDUSD", Double.valueOf("0.8371"));
		refData.getCurrencyPairs().setCurrencyPairsMap(currencyPairsMap);

		Map<String, Map<String, String>> crossCurrencyPairsMap = new HashMap<>();
		Map<String, String> map = new HashMap<>();

		map.put("AUD", "0.042361111111111106");
		map.put("CAD", "USD");
		map.put("CNY", "USD");
		map.put("CZK", "USD");
		map.put("DKK", "USD");
		map.put("EUR", "USD");
		map.put("GBP", "USD");
		map.put("JPY", "USD");
		map.put("NOK", "USD");
		map.put("NZD", "USD");
		map.put("USD", "D");
		crossCurrencyPairsMap.put("AUD", map);
		refData.getCrossCurrencyPairs().setCrossCurrencyPairsMap(crossCurrencyPairsMap);

		Map<String, Integer> decimalPrecisionMap = new HashMap<>();
		decimalPrecisionMap.put("AUD", 2);
		decimalPrecisionMap.put("USD", 2);

		refData.getDecimalPrecision().setDecimalPrecisionMap(decimalPrecisionMap);
		refData.setOutputFormat("base quant = term converted");

		String input = "AUD 100.00 in USD";

		String output = service.calculateFx(input);
		assertThat("AUD 100.00 = USD 83.71").isEqualTo(output);

		input = "AUD 100.00 in AUD";
		output = service.calculateFx(input);
		assertThat("AUD 100.00 = AUD 100.00").isEqualTo(output);

		input = "AUD 100.00 in USD";
		refData.setOutputFormat("base quant : term converted");
		output = service.calculateFx(input);
		assertThat("AUD 100.00 : USD 83.71").isEqualTo(output);

		decimalPrecisionMap.clear();
		decimalPrecisionMap.put("AUD", 4);
		decimalPrecisionMap.put("USD", 5);

		input = "AUD 100.00 in USD";
		refData.setOutputFormat("base quant = term converted");
		output = service.calculateFx(input);
		assertThat("AUD 100.0000 = USD 83.71000").isEqualTo(output);
		/*
		
		input = "KRW 1000.00 in FJD";
		output = service.calculateFx(input);
		assertThat("AUD 100.0000 = USD 83.71000").isEqualTo(output);*/
	}

}
