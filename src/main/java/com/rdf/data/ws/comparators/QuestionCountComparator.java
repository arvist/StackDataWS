package com.rdf.data.ws.comparators;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;

@Component
public class QuestionCountComparator implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		if (o1.getTotals().getQuestionCount() == o2.getTotals().getQuestionCount())
			return 0;
		if (o1.getTotals().getQuestionCount() > o2.getTotals().getQuestionCount())
			return 1;
		return -1;
	}

}
