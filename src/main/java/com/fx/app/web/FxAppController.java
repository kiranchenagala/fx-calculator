package com.fx.app.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fx.app.commons.FxApiResponse;
import com.fx.app.service.FxCalculatorTemplate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/calculator")
@Api(tags={ "calculator" }, description="Operations pertaining to calculate FX")
public class FxAppController {
	
	@Autowired
	private FxCalculatorTemplate fxCalculator;
	
	@ApiOperation(value = "calculate FX for the given input", response = String.class)
	@FxApiResponse
	@RequestMapping(path = "/calculate", method = RequestMethod.GET)
	public ResponseEntity<String> calculate(@RequestParam String input) {
		ResponseEntity<String> response = null;
		String output = fxCalculator.calculateFx(input);
		response = new ResponseEntity<>(output , HttpStatus.OK);
		return response;
	}
	
}
