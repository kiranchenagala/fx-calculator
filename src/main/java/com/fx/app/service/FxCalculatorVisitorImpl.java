package com.fx.app.service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fx.app.commons.FxRuntimeException;
import com.fx.app.model.FxBean;
import com.fx.app.model.ReferentialData;

@Service
public class FxCalculatorVisitorImpl<T> implements FxCalculatorVisitor<T> {

	@Autowired
	private ReferentialData refData;

	@Override
	public boolean visit(String type, T input) {
		switch (type) {
		case FxConstants.FX_STRING:
			List<String> patterns = refData.getInputFormats();
			for(String regex: patterns){
				Pattern pattern = Pattern.compile(regex);
				Matcher matcher = pattern.matcher(String.valueOf(input));
				while (matcher.find()) {
					return true;
				}	
			}
			return false;
		case FxConstants.FX_BEAN:
			FxBean fxBean = (FxBean) input;
			if (refData.getCrossCurrencyPairs().getCrossCurrencyPairsMap().containsKey(fxBean.getBase())
					&& refData.getCrossCurrencyPairs().getCrossCurrencyPairsMap().get(fxBean.getBase())
							.containsKey(fxBean.getTerms())) {
				return true;
			} else {
				throw new FxRuntimeException("Unable to find rate for " + fxBean.getBase() + "/" + fxBean.getTerms());
			}
		default:
			break;
		}
		return false;
	}
}
