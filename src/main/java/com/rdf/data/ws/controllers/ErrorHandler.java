package com.rdf.data.ws.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.rdf.data.ws.model.ErrorWS;
import com.rdf.data.ws.model.enums.Continent;
import com.rdf.data.ws.model.enums.Measurement;
import com.rdf.data.ws.model.enums.Order;

@Component
public class ErrorHandler {

	private ErrorWS error;
	
	public ErrorHandler() {

		error = new ErrorWS();
		error.setMessage(
				"Failed to convert one or more of given filter values, see all allowed values below. Values are case-sensitive");
		Map<String, Object> descriptionMap = new HashMap<String, Object>();
		descriptionMap.put("order", allowedOrderValues());
		descriptionMap.put("orderBy", allowedOrderByValues());
		descriptionMap.put("continent", allowedContinentValues());
		descriptionMap.put("country", allowedContryValues());
		error.setDescription(descriptionMap);
	}

	public ErrorWS getError(){
		return this.error;
	}
	
	private List<Object> allowedContryValues() {
		List<Object> allowedContries = new ArrayList<Object>();
		allowedContries.add("Countries can be queried using ISO 3166-1 alpha 3 codes");
		allowedContries.add("See https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3");
		return allowedContries;
	}

	private List<Object> allowedContinentValues() {
		List<Object> allowedContinents = new ArrayList<Object>();
		allowedContinents.add(Continent.AF);
		allowedContinents.add(Continent.AN);
		allowedContinents.add(Continent.AS);
		allowedContinents.add(Continent.EU);
		allowedContinents.add(Continent.NA);
		allowedContinents.add(Continent.OC);
		allowedContinents.add(Continent.SA);
		return allowedContinents;
	}

	private List<Object> allowedOrderValues() {
		List<Object> allowedOrder = new ArrayList<Object>();
		allowedOrder.add(Order.asc);
		allowedOrder.add(Order.desc);
		return allowedOrder;
	}

	private List<Object> allowedOrderByValues() {
		List<Object> allowedOrder = new ArrayList<Object>();
		allowedOrder.add(Measurement.aat);
		allowedOrder.add(Measurement.aqc);
		allowedOrder.add(Measurement.aqp);
		allowedOrder.add(Measurement.qc);
		allowedOrder.add(Measurement.ta);
		allowedOrder.add(Measurement.vic);
		allowedOrder.add(Measurement.voc);
		return allowedOrder;
	}

}
