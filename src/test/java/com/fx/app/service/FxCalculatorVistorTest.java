package com.fx.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import com.fx.app.commons.FxRuntimeException;
import com.fx.app.model.FxBean;
import com.fx.app.model.ReferentialData;

@RunWith(MockitoJUnitRunner.class)
public class FxCalculatorVistorTest {
	
	@InjectMocks
	private FxCalculatorVisitor visitor = new FxCalculatorVisitorImpl<>();
	
	@Spy
	private ReferentialData refData ;
	
	@Test
	public void shouldValidateStringInput(){
		
		List<String> inputFormats = new ArrayList<>();
		inputFormats.add("([A-Z]+\\s+[0-9]+[.][0-9]+\\s*in*\\s+[A-Z]+)");
		refData.setInputFormats(inputFormats);
		
		String input = "AUD 100.00 in USD";
		boolean valid = visitor.visit("fx-string", input);
		assertThat(true).isEqualTo(valid);
		
		input = "AUD        100.00    in                USD";
		valid = visitor.visit("fx-string", input);
		assertThat(true).isEqualTo(valid);
		
		input = "AUD 100.00 into USD";
		valid = visitor.visit("fx-string", input);
		assertThat(false).isEqualTo(valid);
		
	}
	
	@Test
	public void shouldValidateBeanInput(){
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
		
		FxBean fxBean = new FxBean("AUD", "USD", 100.00, null);
		boolean valid = visitor.visit("fx-bean", fxBean);
		assertThat(true).isEqualTo(valid);
		
		String errorMsg = null;
		fxBean = new FxBean("KRW", "FJD", 100.00, null);
		try{
			valid = visitor.visit("fx-bean", fxBean);			
		}catch(FxRuntimeException e){
			errorMsg = e.getMessage();
		}
		assertThat("Unable to find rate for KRW/FJD").isEqualTo(errorMsg);			


	}
}
