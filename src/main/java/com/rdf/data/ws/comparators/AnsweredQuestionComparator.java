package com.rdf.data.ws.comparators;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;

@Component
public class AnsweredQuestionComparator implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		if (o1.getTotals().getAnsweredQuestionCount() > o2.getTotals().getAnsweredQuestionCount()) {
			return 1;
		} else if (o1.getTotals().getAnsweredQuestionCount() < o2.getTotals().getAnsweredQuestionCount()) {
			return 1;
		} else {
			return 0;
		}
	}

}
