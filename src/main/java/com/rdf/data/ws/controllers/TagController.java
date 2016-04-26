package com.rdf.data.ws.controllers;

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

import com.rdf.data.ws.comparators.sorter.TagSorter;
import com.rdf.data.ws.model.ErrorWS;
import com.rdf.data.ws.model.Tag;
import com.rdf.data.ws.model.enums.Continent;
import com.rdf.data.ws.model.enums.Country;
import com.rdf.data.ws.model.enums.TagMeasurement;
import com.rdf.data.ws.model.enums.Order;
import com.rdf.data.ws.tdb.TagDataProcessor;

@RestController
public class TagController {

	@Autowired
	private TagDataProcessor tagData;
	@Autowired
	private TagSorter sorter;
	@Autowired
	private ErrorHandler errorHandler;

	@RequestMapping(value = "/tags/{tag}", method = RequestMethod.GET)
	public Tag getTags(@PathVariable String tag,
			@RequestParam(value = "startTime", defaultValue = "01011970") @DateTimeFormat(pattern = "ddMMyyyy") Date startTime,
			@RequestParam(value = "endTime", defaultValue = "01012100") @DateTimeFormat(pattern = "ddMMyyyy") Date endTime,
			@RequestParam(value = "country", defaultValue = "ALL") Country country,
			@RequestParam(value = "continent", defaultValue = "ALL") Continent continent) {
		if (country.equals(Country.ALL)) {
			if (continent.equals(Continent.ALL)) {
				return getTagFromString(startTime, endTime, tag.toLowerCase());
			} else {
				return getTagFromStringContinent(tag.toLowerCase(), continent.toString(), startTime, endTime);
			}
		} else {
			return getTagFromStringCountry(tag.toLowerCase(), country.toString(), startTime, endTime);
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tags", method = RequestMethod.GET)
	public List<Tag> getAllTags(@RequestParam(value = "order", defaultValue = "desc") Order order,
			@RequestParam(value = "country", defaultValue = "ALL") Country country,
			@RequestParam(value = "continent", defaultValue = "ALL") Continent continent,
			@RequestParam(value = "orderBy", defaultValue = "ta") TagMeasurement measurement,
			@RequestParam(value = "limit", defaultValue = "25") Integer limit,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "startTime", defaultValue = "01011970") @DateTimeFormat(pattern = "ddMMyyyy") Date startTime,
			@RequestParam(value = "endTime", defaultValue = "01012100") @DateTimeFormat(pattern = "ddMMyyyy") Date endTime) {
		List<Tag> list;
		if (country.equals(Country.ALL)) {
			if (continent.equals(Continent.ALL)) {
				list = getTagsListSorted(startTime, endTime, measurement);
			} else {
				list = getTagsListSortedContinent(continent.toString(), startTime, endTime, measurement);
			}
		} else {
			list = getTagsListSortedCountry(country.toString(), startTime, endTime, measurement);
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

	@ExceptionHandler(Exception.class)
	public ErrorWS enumError() {
		return errorHandler.getError();
	}

	private Tag getTagFromString(Date startTime, Date endTime, String tag) {
		try {
			return tagData.getTagTotalsAndAveragesByTagAndDates(tag, startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
			return new Tag();
		}
	}

	private List<Tag> getTagsListSorted(Date startTime, Date endTime, TagMeasurement measurement) {
		List<Tag> list;
		try {
			list = tagData.getAllTagTotalsAndAveragesByDates(startTime, endTime);
			list.sort(sorter.getComparator(measurement));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Tag>();
		}
	}

	private Tag getTagFromStringContinent(String tag, String continent, Date startTime, Date endTime) {
		try {
			return tagData.getTagTotalsAndAveragesByTagDatesCountry(tag, continent, startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
			return new Tag();
		}
	}

	private Tag getTagFromStringCountry(String tag, String country, Date startTime, Date endTime) {
		try {
			return tagData.getTagTotalsAndAveragesByTagDatesCountry(tag, country, startTime, endTime);
		} catch (Exception e) {
			e.printStackTrace();
			return new Tag();
		}
	}

	private List<Tag> getTagsListSortedContinent(String continent, Date startTime, Date endTime,
			TagMeasurement measurement) {
		List<Tag> list;
		try {
			list = tagData.getAllTagTotalsAndAveragesByDatesContinent(continent, startTime, endTime);
			list.sort(sorter.getComparator(measurement));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Tag>();
		}
	}

	private List<Tag> getTagsListSortedCountry(String country, Date startTime, Date endTime,
			TagMeasurement measurement) {
		List<Tag> list;
		try {
			list = tagData.getAllTagTotalsAndAveragesByDatesCountry(country, startTime, endTime);
			list.sort(sorter.getComparator(measurement));
			return list;
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Tag>();
		}
	}

}
