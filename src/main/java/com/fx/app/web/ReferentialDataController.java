package com.fx.app.web;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fx.app.commons.FxApiResponse;
import com.fx.app.model.ReferentialData;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/referentials")
@Api(tags={ "referentials" }, description="Operations pertaining to upload referential data")
public class ReferentialDataController {
	
	@Autowired
	private ReferentialData refData;
	
	@ApiOperation(value = "fetch currency pairs", response = Map.class)
	@FxApiResponse
	@RequestMapping(path = "/currency-pairs", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Double>> getAllCurrencyPairs() {
		Map<String, Double> currencyPairs  = refData.getCurrencyPairs().getCurrencyPairsMap();
		ResponseEntity<Map<String, Double>> response = null;
		response = new ResponseEntity<>(currencyPairs , HttpStatus.OK);
		return response;
	}
	
	@ApiOperation(value = "update currency pairs", response = Map.class)
	@FxApiResponse
	@RequestMapping(path = "/currency-pairs", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Double>> updateCurrencyPairs(@RequestBody Map<String, Double> updated) {
		refData.getCurrencyPairs().getCurrencyPairsMap().clear();
		refData.getCurrencyPairs().setCurrencyPairsMap(updated);
		ResponseEntity<Map<String, Double>> response = null;
		response = new ResponseEntity<>(refData.getCurrencyPairs().getCurrencyPairsMap() , HttpStatus.OK);
		return response;
	}
	
	@ApiOperation(value = "fetch cross currency pairs", response = Map.class)
	@FxApiResponse
	@RequestMapping(path = "/cross-currency-pairs", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Map<String, String>>> getAllCrossCurrencyPairs() {
		Map<String, Map<String, String>> crossCurrencyPairs  = refData.getCrossCurrencyPairs().getCrossCurrencyPairsMap();
		ResponseEntity<Map<String, Map<String, String>>> response = null;
		response = new ResponseEntity<>(crossCurrencyPairs , HttpStatus.OK);
		return response;
	}
	
	@ApiOperation(value = "update cross currency pairs", response = Map.class)
	@FxApiResponse
	@RequestMapping(path = "/cross-currency-pairs", method = RequestMethod.PUT)
	public ResponseEntity<Map<String, Map<String, String>>> updateCrossCurrencyPairs(@RequestBody Map<String, Map<String, String>> updated) {
		refData.getCrossCurrencyPairs().getCrossCurrencyPairsMap().clear();
		refData.getCrossCurrencyPairs().setCrossCurrencyPairsMap(updated);
		Map<String, Map<String, String>> crossCurrencyPairs  = refData.getCrossCurrencyPairs().getCrossCurrencyPairsMap();
		ResponseEntity<Map<String, Map<String, String>>> response = null;
		response = new ResponseEntity<>(crossCurrencyPairs , HttpStatus.OK);
		return response;
	}
	
	
}
