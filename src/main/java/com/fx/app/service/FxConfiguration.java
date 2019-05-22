package com.fx.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "fx")
public class FxConfiguration {	
	
	private Map<String, Double> currencyPairs;
	private List<String> inputPatterns;
	private String outputPattern;
	

}
