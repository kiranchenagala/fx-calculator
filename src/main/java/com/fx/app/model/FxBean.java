package com.fx.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FxBean {

	private String base;
	private String terms;
	private Double quantity;
	private Double convertedQuantity;


}
