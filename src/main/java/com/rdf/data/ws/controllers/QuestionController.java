package com.rdf.data.ws.controllers;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.jena.ext.com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rdf.data.ws.comparators.sorter.QuestionSorter;
import com.rdf.data.ws.model.ErrorWS;
import com.rdf.data.ws.model.Question;
import com.rdf.data.ws.model.enums.Continent;
import com.rdf.data.ws.model.enums.Country;
import com.rdf.data.ws.model.enums.Order;
import com.rdf.data.ws.model.enums.QuestionMeasurement;
import com.rdf.data.ws.tdb.QuestionDataProcessor;

@RestController
public class QuestionController {

	@Autowired
	private QuestionDataProcessor questionData;
	@Autowired
	private QuestionSorter sorter;
	@Autowired
	private ErrorHandler errorHandler;

	@RequestMapping(value = "/questions/{id}", method = RequestMethod.GET)
	public Question getQuestionById(@PathVariable int id) throws ParseException {
		return questionData.getQuestionById(id);
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/questions")
	public List<Question> getQuestionsBy(@RequestParam(value = "order", defaultValue = "desc") Order order,
			@RequestParam(value = "country", defaultValue = "ALL") Country country,
			@RequestParam(value = "continent", defaultValue = "ALL") Continent continent,
			@RequestParam(value = "orderBy", defaultValue = "tt") QuestionMeasurement measurement,
			@RequestParam(value = "limit", defaultValue = "25") Integer limit,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "startTime", defaultValue = "01011970") @DateTimeFormat(pattern = "ddMMyyyy") Date startTime,
			@RequestParam(value = "endTime", defaultValue = "01012100") @DateTimeFormat(pattern = "ddMMyyyy") Date endTime,
			@RequestParam(value = "tag", defaultValue = "ALL") String tag) {
		List<Question> list;
		System.out.println(measurement);
		if (country.equals(Country.ALL)) {
			if (continent.equals(Continent.ALL)) {
				list = getQuestionsByTagDates(tag, startTime, endTime, measurement);
			} else {
				list = getQuestionListSortedContinent(tag, continent.toString(), startTime, endTime, measurement);
			}
		} else {
			list = getQuestionListSortedCountry(tag, country.toString(), startTime, endTime, measurement);
		}
		if (order.equals(Order.desc)) {
			list = Lists.reverse(list);
		}
		return getSubList(list, offset, limit);
	}

	@SuppressWarnings("rawtypes")
	private List getSubList(List list, int offset, int limit) {
		if (offset >= 0 && limit > 0) {
			if (offset + limit < list.size() && offset < offset + limit)
				return list.subList(offset, offset + limit);
			if (offset < list.size())
				return list.subList(offset, list.size());
			return new ArrayList<Object>();
		}
		return new ArrayList<Object>();
	}

	private List<Question> getQuestionListSortedCountry(String tag, String country, Date startTime, Date endTime,
			QuestionMeasurement measurement) {
		List<Question> list;
		try {
			list = questionData.getQuestionsByTagDatesCountry(tag, country, startTime, endTime);
			list.sort(sorter.getComparator(measurement));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Question>();
		}
	}

	private List<Question> getQuestionListSortedContinent(String tag, String continent, Date startTime, Date endTime,
			QuestionMeasurement measurement) {
		List<Question> list;
		try {
			list = questionData.getQuestionsByTagDatesContinent(tag, continent, startTime, endTime);
			list.sort(sorter.getComparator(measurement));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Question>();
		}
	}

	private List<Question> getQuestionsByTagDates(String tag, Date startTime, Date endTime,
			QuestionMeasurement measurement) {
		List<Question> list;
		try {
			list = questionData.getQuestionsByTagDates(tag, startTime, endTime);
			list.sort(sorter.getComparator(measurement));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Question>();
		}
	}

	@ExceptionHandler(Exception.class)
	public ErrorWS enumError() {
		return errorHandler.getError();
	}

}
