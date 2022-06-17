package com.study.projects;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Body;

public class Calculations {
	
	public Map<String, Object> addItem(@Body Map<String, Object> body) {
		// String operation= body.get("operation");

		int sum = 0;
		int firstN = (int) body.get("first_number");
		int secondN = (int) body.get("second_number");
		if ((body.get("operation").equals("add"))) {
			System.out.println("Addition Operation-------");
			sum = (firstN + secondN);

		}
		HashMap<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("result", sum);
		return resultMap;
	}

}
