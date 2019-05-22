package com.fx.app.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fx.app.commons.ExcelParser;
import com.fx.app.commons.FxRuntimeException;
import com.fx.app.service.FxConfiguration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class ReferentialData {
	
	@Autowired
	private FxConfiguration config;
	
	private List<String> inputFormats;
	private String outputFormat;
	private CurrencyPairs currencyPairs = new CurrencyPairs();
	private CrossCurrencyPairs crossCurrencyPairs = new CrossCurrencyPairs();
	private DecimalPrecision decimalPrecision = new DecimalPrecision();
	
	@Data
	public static class DecimalPrecision{
		private Map<String, Integer> decimalPrecisionMap = new HashMap<>();
		
	}
	
	@Data
	public static class CurrencyPairs{
		private Map<String, Double> currencyPairsMap = new HashMap<>();
		
	}
	
	@Data
	public static class CrossCurrencyPairs{
		private Map<String, Map<String, String>> crossCurrencyPairsMap = new HashMap<>();
		
	}

	public void initialize() {
		this.currencyPairs.setCurrencyPairsMap(config.getCurrencyPairs());
		try {
			this.crossCurrencyPairs.setCrossCurrencyPairsMap(getCrossCurrencyMap());
		} catch (Exception e) {
			e.printStackTrace();
		}
		setInputFormats(config.getInputPatterns());
		setOutputFormat(config.getOutputPattern());
	}
	
	private Map<String, Map<String, String>> getCrossCurrencyMap() throws IOException{
		File tempFile = File.createTempFile("temp", ".xlsx");
		
		InputStream inputStream=getClass().getClassLoader().getResourceAsStream("fx-cross-pairs.xlsx");
	
		OutputStream outputStream = new FileOutputStream(tempFile);
		IOUtils.copy(inputStream, outputStream);		
		
		Map<String, Map<String, String>> crossCurrencyPairsMap = new LinkedHashMap<String, Map<String, String>>();
		ExcelParser parser = new ExcelParser().withHeader("/", "AUD","CAD","CNY","CZK","DKK","EUR","GBP","JPY","NOK","NZD","USD");
		try {
			Iterator<Map<String, Object>> values = parser.parse(tempFile, 0).iterator();
			values.next();
			while(values.hasNext()){
				Map<String, Object> nextVal = values.next();
				String key = (String) nextVal.get("/");
				Map<String, String> valueMap = new HashMap<>();
				for(Map.Entry<String, Object> entry: nextVal.entrySet()){
					if(entry.getKey().equals("/")){
						continue;
					}
					valueMap.put(entry.getKey(), String.valueOf(entry.getValue()));
					crossCurrencyPairsMap.put(key, valueMap);
				}
			}
		} catch (InvalidFormatException | IOException e) {
			e.printStackTrace();
			throw new FxRuntimeException("Error: "+ e.getMessage());
		}
		finally{
			inputStream.close();
			outputStream.close();
		}
		
		return crossCurrencyPairsMap;
	}
}
