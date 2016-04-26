package com.rdf.data.ws.comparators.question;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Question;

@Component
public class TitleComparatorQ implements Comparator<Question>{

	@Override
	public int compare(Question o1, Question o2) {
		return o1.getTitle().compareTo(o2.getTitle());
	}

}
