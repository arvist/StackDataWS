package com.rdf.data.ws.comparators.question;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Question;

@Component
public class AnswerCountComparator implements Comparator<Question>{

	@Override
	public int compare(Question o1, Question o2) {
		if (o1.getAnswerCount() > o2.getAnswerCount()) {
			return 1;
		} else if (o1.getAnswerCount() < o2.getAnswerCount()) {
			return -1;
		} else {
			return 0;
		}
	}

}
