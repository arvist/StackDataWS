package com.rdf.data.ws.model;

import java.util.List;
import java.util.Map;

public class ErrorWS {

	private String message;
	private Map<String,Object> description;
	
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Map<String, Object> getDescription() {
		return description;
	}
	public void setDescription(Map<String, Object> description) {
		this.description = description;
	}
  
	
}
