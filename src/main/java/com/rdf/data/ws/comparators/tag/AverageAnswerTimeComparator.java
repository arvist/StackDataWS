package com.rdf.data.ws.comparators.tag;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import com.rdf.data.ws.model.Tag;

@Component
public class AverageAnswerTimeComparator implements Comparator<Tag> {

	@Override
	public int compare(Tag o1, Tag o2) {
		if (o1.getAverages().getAverageAnswerTime() > o2.getAverages().getAverageAnswerTime()) {
			return 1;
		} else if (o1.getAverages().getAverageAnswerTime() > o2.getAverages().getAverageAnswerTime()) {
			return -1;
		} else {
			return 0;
		}
	}

}
